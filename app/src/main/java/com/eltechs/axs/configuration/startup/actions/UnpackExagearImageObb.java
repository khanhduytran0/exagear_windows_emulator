package com.eltechs.axs.configuration.startup.actions;

import android.content.Context;
import android.util.AtomicFile;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.configuration.startup.StartupActionInfo;
import com.eltechs.axs.helpers.ZipInstallerObb;
import com.eltechs.axs.helpers.ZipInstallerObb.Callbacks;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

public class UnpackExagearImageObb<StateClass extends ExagearImageAware> extends AbstractStartupAction<StateClass> {
    private final String[] keepOldFiles;
    private final boolean mayTakeFromSdcard;
    private final String progressFileName;

    public UnpackExagearImageObb(boolean z, String[] strArr, String str) {
        this.mayTakeFromSdcard = z;
        this.keepOldFiles = strArr;
        this.progressFileName = str;
    }

    public UnpackExagearImageObb(boolean z, String[] strArr) {
        this.mayTakeFromSdcard = z;
        this.keepOldFiles = strArr;
        this.progressFileName = null;
    }

    public UnpackExagearImageObb(boolean z) {
        this.mayTakeFromSdcard = z;
        this.keepOldFiles = new String[0];
        this.progressFileName = null;
    }

    public StartupActionInfo getInfo() {
        return new StartupActionInfo("", this.progressFileName);
    }

    public void execute() {
        ExagearImageAware exagearImageAware = (ExagearImageAware) getApplicationState();
        final Context appContext = getAppContext();
        final File file = this.progressFileName != null ? new File(this.progressFileName) : null;
        if (file != null) {
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            } catch (IOException unused) {
            }
        }
        try {
            ZipInstallerObb zipInstallerObb = new ZipInstallerObb(appContext, true, this.mayTakeFromSdcard, exagearImageAware.getExagearImage(), new Callbacks() {
                public void unpackingInProgress() {
                }

                public void noObbFound() {
                    UnpackExagearImageObb.this.sendError(appContext.getResources().getString(R.string.no_obb_file_found));
                }

                public void unpackingCompleted(File file) {
                    UnpackExagearImageObb.this.sendDone();
                }

                public void error(String str) {
                    UnpackExagearImageObb.this.sendError(str);
                }

                public void reportProgress(long j) {
                    if (file != null) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append(Long.toString(j));
                            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                            sb.append(UnpackExagearImageObb.this.getString(R.string.sa_unpacking_guest_image));
                            String sb2 = sb.toString();
                            AtomicFile atomicFile = new AtomicFile(file);
                            FileOutputStream startWrite = atomicFile.startWrite();
                            startWrite.write(sb2.getBytes());
                            atomicFile.finishWrite(startWrite);
                        } catch (IOException unused) {
                        }
                    }
                }
            }, this.keepOldFiles);
            zipInstallerObb.installImageFromObbIfNeeded();
        } catch (IOException e) {
            sendError("Failed to unpack the exagear system image.", e);
        }
    }
}
