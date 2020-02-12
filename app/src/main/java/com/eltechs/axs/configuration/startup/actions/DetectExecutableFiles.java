package com.eltechs.axs.configuration.startup.actions;

import android.util.Log;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.AvailableExecutableFilesAware;
import com.eltechs.axs.applicationState.UserApplicationsDirectoryNameAware;
import com.eltechs.axs.configuration.startup.AsyncStartupAction;
import com.eltechs.axs.configuration.startup.AvailableExecutableFiles;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;
import com.eltechs.axs.configuration.startup.ExecutableFileDetectorsCollection;
import com.eltechs.axs.configuration.startup.PerApplicationSettingsStore;
import com.eltechs.axs.configuration.startup.StartupActionInfo;
import com.eltechs.axs.helpers.FileFinder;
import com.eltechs.axs.helpers.SafeFileHelpers;
import com.eltechs.axs.helpers.SafeFileHelpers.FileCallback;
import com.eltechs.axs.helpers.UiThread;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class DetectExecutableFiles<StateClass extends AvailableExecutableFilesAware<StateClass> & UserApplicationsDirectoryNameAware> extends AbstractStartupAction<StateClass> implements AsyncStartupAction<StateClass> {
    private static final int DIR_WITH_USER_APPLICATION_SEARCH_DEPTH = 1;
    private static final int EXE_FILES_SEARCH_DEPTH = 3;
    private static final String[] typicalHelperExeNames = {"setup", "install", "unins", "autorun", "msiexec", "update"};
    /* access modifiers changed from: private */
    public final ExecutableFileDetectorsCollection<StateClass> detectors;
    private final boolean useCache;

    public interface ExecutableFileDetector<StateClass> {
        DetectedExecutableFile<StateClass> detect(File file, String str);
    }

    private class FilesAccumulator implements FileCallback {
        private final List<DetectedExecutableFile<StateClass>> detectedExecutableFiles;
        private final List<DetectedExecutableFile<StateClass>> otherExecutableFiles;
        private boolean sorted;

        private FilesAccumulator() {
            this.detectedExecutableFiles = new ArrayList();
            this.otherExecutableFiles = new ArrayList();
            this.sorted = false;
        }

        public void apply(File file, String str) throws IOException {
            for (ExecutableFileDetector detect : DetectExecutableFiles.this.detectors.getDetectors()) {
                DetectedExecutableFile detect2 = detect.detect(file, str);
                if (detect2 != null) {
                    this.detectedExecutableFiles.add(detect2);
                    return;
                }
            }
            DetectedExecutableFile detect3 = DetectExecutableFiles.this.detectors.getDefaultDetector().detect(file, str);
            if (detect3 != null) {
                this.otherExecutableFiles.add(detect3);
            }
        }

        /* access modifiers changed from: 0000 */
        public List<DetectedExecutableFile<StateClass>> getDetectedExecutableFiles() {
            sortIfNeed();
            return this.detectedExecutableFiles;
        }

        /* access modifiers changed from: 0000 */
        public List<DetectedExecutableFile<StateClass>> getOtherExecutableFiles() {
            sortIfNeed();
            return this.otherExecutableFiles;
        }

        private void sortIfNeed() {
            if (!this.sorted) {
                Comparator r0 = new Comparator<DetectedExecutableFile<StateClass>>() {
                    public int compare(DetectedExecutableFile<StateClass> detectedExecutableFile, DetectedExecutableFile<StateClass> detectedExecutableFile2) {
                        return detectedExecutableFile.getFileName().compareTo(detectedExecutableFile2.getFileName());
                    }
                };
                Collections.sort(this.detectedExecutableFiles, r0);
                Collections.sort(this.otherExecutableFiles, r0);
                this.sorted = true;
            }
        }
    }

    public DetectExecutableFiles(ExecutableFileDetectorsCollection<StateClass> executableFileDetectorsCollection) {
        this(executableFileDetectorsCollection, true);
    }

    public DetectExecutableFiles(ExecutableFileDetectorsCollection<StateClass> executableFileDetectorsCollection, boolean z) {
        this.detectors = executableFileDetectorsCollection;
        this.useCache = z;
    }

    private final File[] removeDuplicatedFiles(List<File> list) {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        for (File file : list) {
            if (!hashSet.contains(file.getAbsolutePath())) {
                arrayList.add(file);
                hashSet.add(file.getAbsolutePath());
            }
        }
        return (File[]) arrayList.toArray(new File[0]);
    }

    public StartupActionInfo getInfo() {
        return new StartupActionInfo(getAppContext().getString(R.string.sa_searching_for_exe_files));
    }

    public void execute() {
        try {
            final FilesAccumulator filesAccumulator = new FilesAccumulator();
            List findDirectory = FileFinder.findDirectory(new File("/mnt"), 1, ((UserApplicationsDirectoryNameAware) ((AvailableExecutableFilesAware) getApplicationState())).getUserApplicationsDirectoryName().getName());
            File file = new File("/storage");
            if (file.isDirectory()) {
                findDirectory.addAll(FileFinder.findDirectory(file, 1, ((UserApplicationsDirectoryNameAware) ((AvailableExecutableFilesAware) getApplicationState())).getUserApplicationsDirectoryName().getName()));
            }
            for (File file2 : removeDuplicatedFiles(findDirectory)) {
                if (file2.canWrite()) {
                    DetectedExecutableFilesCache load = DetectedExecutableFilesCache.load(file2);
                    if (load == null || !this.useCache) {
                        Log.i("DetectedExecutableFiles", String.format("Cache for '%s' is stale.", new Object[]{file2.getAbsolutePath()}));
                        final DetectedExecutableFilesCache createEmpty = DetectedExecutableFilesCache.createEmpty(file2);
                        SafeFileHelpers.doWithExecutableFiles(file2, 3, new FileCallback() {
                            public void apply(File file, String str) throws IOException {
                                filesAccumulator.apply(file, str);
                                createEmpty.addFile(file, str);
                            }
                        });
                        createEmpty.persist();
                    } else {
                        load.doWithFiles(filesAccumulator);
                    }
                }
            }
            applyPerApplicationSettings(filesAccumulator.getDetectedExecutableFiles());
            applyPerApplicationSettings(filesAccumulator.getOtherExecutableFiles());
            UiThread.post(new Runnable() {
                public void run() {
                    ((AvailableExecutableFilesAware) DetectExecutableFiles.this.getApplicationState()).setAvailableExecutableFiles(new AvailableExecutableFiles(filesAccumulator.getDetectedExecutableFiles(), filesAccumulator.getOtherExecutableFiles()));
                    DetectExecutableFiles.this.sendDone();
                }
            });
        } catch (IOException e) {
            sendError("Failed to enumerate executable files in /mnt/sdcard/ExaGear/.", e);
        }
    }

    public static boolean isInstallerOrUninstallerOrUpdater(String str) {
        String lowerCase = str.toLowerCase();
        for (String contains : typicalHelperExeNames) {
            if (lowerCase.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    private void applyPerApplicationSettings(List<DetectedExecutableFile<StateClass>> list) {
        for (DetectedExecutableFile detectedExecutableFile : list) {
            try {
                PerApplicationSettingsStore.get(detectedExecutableFile).updateDetectedExecutableFileConfiguration();
            } catch (IOException unused) {
            }
        }
    }
}
