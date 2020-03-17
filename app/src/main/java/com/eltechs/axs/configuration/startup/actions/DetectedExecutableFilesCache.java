package com.eltechs.axs.configuration.startup.actions;

import android.util.Log;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.SafeFileHelpers.FileCallback;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class DetectedExecutableFilesCache implements Serializable {
    private static final String CACHE_FILE_NAME_PREFIX = "executable_files_list_cache_";
    private final Set<DirectoryCacheEntry> cachedDirectories = new HashSet();
    private final Set<FileCacheEntry> cachedFiles = new HashSet();
    private File rootDir;

    public static class DirectoryCacheEntry implements Serializable {
        public final File dir;
        public final long modificationTime;

        public DirectoryCacheEntry(File file, long j) {
            this.dir = file;
            this.modificationTime = j;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof DirectoryCacheEntry)) {
                return false;
            }
            return this.dir.equals(((DirectoryCacheEntry) obj).dir);
        }

        public int hashCode() {
            return this.dir.hashCode();
        }
    }

    public static class FileCacheEntry implements Serializable {
        public final String name;
        public final File parentDir;

        FileCacheEntry(File file, String str) {
            this.parentDir = file;
            this.name = str;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof FileCacheEntry)) {
                return false;
            }
            FileCacheEntry fileCacheEntry = (FileCacheEntry) obj;
            if (this.parentDir.equals(fileCacheEntry.parentDir) && this.name.equals(fileCacheEntry.name)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.name.hashCode();
        }
    }

    private DetectedExecutableFilesCache(File file) {
        this.rootDir = file;
    }

    public static DetectedExecutableFilesCache load(File file) {
        Throwable th;
        Throwable th2;
        StringBuilder sb = new StringBuilder();
        sb.append(CACHE_FILE_NAME_PREFIX);
        sb.append(file.getAbsolutePath().replace(IOUtils.DIR_SEPARATOR_UNIX, '_'));
        try {
            FileInputStream openPrivateFileForReading = AndroidHelpers.openPrivateFileForReading(sb.toString());
            try {
                DetectedExecutableFilesCache detectedExecutableFilesCache = (DetectedExecutableFilesCache) new ObjectInputStream(openPrivateFileForReading).readObject();
                detectedExecutableFilesCache.rootDir = file;
                if (!detectedExecutableFilesCache.isUpToDate()) {
                    detectedExecutableFilesCache = null;
                }
                if (openPrivateFileForReading != null) {
                    openPrivateFileForReading.close();
                }
                return detectedExecutableFilesCache;
            } catch (Throwable th3) {
                th2 = th3;
            }
            // throw th2;
            if (openPrivateFileForReading != null) {
                if (th2 != null) {
                    try {
                        openPrivateFileForReading.close();
                    } catch (Throwable th5) {
                        th2.addSuppressed(th5);
                    }
                } else {
                    openPrivateFileForReading.close();
                }
            }
            throw th2;
        } catch (Throwable e) {
            Log.w("DetectedExecutableFilesCache", String.format("There was an error reading the cache for '%s'.", new Object[]{file.getAbsolutePath()}), e);
            return null;
        }
    }

    public static DetectedExecutableFilesCache createEmpty(File file) {
        DetectedExecutableFilesCache detectedExecutableFilesCache = new DetectedExecutableFilesCache(file);
        detectedExecutableFilesCache.cachedDirectories.add(new DirectoryCacheEntry(file, file.lastModified()));
        return detectedExecutableFilesCache;
    }

    public void persist() {
        FileOutputStream openPrivateFileForWriting;
        StringBuilder sb = new StringBuilder();
        sb.append(CACHE_FILE_NAME_PREFIX);
        sb.append(this.rootDir.getAbsolutePath().replace(IOUtils.DIR_SEPARATOR_UNIX, '_'));
        try {
            openPrivateFileForWriting = AndroidHelpers.openPrivateFileForWriting(sb.toString());
            new ObjectOutputStream(openPrivateFileForWriting).writeObject(this);
            if (openPrivateFileForWriting != null) {
                openPrivateFileForWriting.close();
                return;
            }
            return;
        } catch (Exception unused) {
            return;
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
        // throw th;
    }

    public boolean isUpToDate() throws IOException {
        String absolutePath = this.rootDir.getAbsolutePath();
        for (DirectoryCacheEntry directoryCacheEntry : this.cachedDirectories) {
            if (!directoryCacheEntry.dir.isDirectory() || directoryCacheEntry.dir.lastModified() != directoryCacheEntry.modificationTime) {
                return false;
            }
            if (!directoryCacheEntry.dir.getAbsolutePath().startsWith(absolutePath)) {
                return false;
            }
        }
        for (FileCacheEntry fileCacheEntry : this.cachedFiles) {
            File file = new File(fileCacheEntry.parentDir, fileCacheEntry.name);
            if (!file.isFile()) {
                return false;
            }
            if (!file.getAbsolutePath().startsWith(absolutePath)) {
                return false;
            }
        }
        return true;
    }

    public void doWithFiles(FileCallback fileCallback) throws IOException {
        for (FileCacheEntry fileCacheEntry : this.cachedFiles) {
            fileCallback.apply(fileCacheEntry.parentDir, fileCacheEntry.name);
        }
    }

    public void addFile(File file, String str) throws IOException {
        Assert.isTrue(file.getAbsolutePath().startsWith(this.rootDir.getAbsolutePath()));
        this.cachedFiles.add(new FileCacheEntry(file, str));
        while (!file.equals(this.rootDir)) {
            this.cachedDirectories.add(new DirectoryCacheEntry(file, file.lastModified()));
            file = file.getParentFile();
        }
    }
}
