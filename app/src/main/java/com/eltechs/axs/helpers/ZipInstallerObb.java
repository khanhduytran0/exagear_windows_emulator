package com.eltechs.axs.helpers;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import com.eltechs.axs.ExagearImageConfiguration.ExagearImage;
import com.eltechs.axs.ExagearImageConfiguration.ExagearImagePaths;
import com.eltechs.axs.helpers.SafeFileHelpers.FileCallback;
import com.eltechs.axs.helpers.SafeFileHelpers.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ZipInstallerObb {
    /* access modifiers changed from: private */
    public final Callbacks callbacks;
    private final Context context;
    private final ExagearImage exagearImage;
    private int foundObbVersion;
    private final boolean isMain;
    /* access modifiers changed from: private */
    public final String[] keepOldFiles;
    private final boolean mayTakeFromSdcard;

    public interface Callbacks extends com.eltechs.axs.helpers.ZipUnpacker.Callbacks {
        void error(String str);

        void noObbFound();

        void unpackingCompleted(File file);

        void unpackingInProgress();
    }

    public ZipInstallerObb(Context context2, boolean z, boolean z2, ExagearImage exagearImage2, Callbacks callbacks2, String[] strArr) {
        this.context = context2;
        this.isMain = z;
        this.mayTakeFromSdcard = z2;
        this.exagearImage = exagearImage2;
        this.callbacks = callbacks2;
        this.keepOldFiles = strArr;
    }

    private File findObbFile() {
        try {
            this.foundObbVersion = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException unused) {
            this.foundObbVersion = 0;
        }
        while (this.foundObbVersion >= 0) {
            String str = "%s.%d.%s.obb";
            Object[] objArr = new Object[3];
            objArr[0] = this.isMain ? "main" : "patch";
            objArr[1] = Integer.valueOf(this.foundObbVersion);
            objArr[2] = this.context.getPackageName();
            String format = String.format(str, objArr);
            File file = new File(this.context.getObbDir(), format);
            if (!file.exists() && this.mayTakeFromSdcard) {
                file = new File(AndroidHelpers.getMainSDCard(), format);
            }
            if (file.exists()) {
                return file;
            }
            this.foundObbVersion--;
        }
        this.foundObbVersion = -1;
        return null;
    }


    private void createFileWithObbVersion(File file) throws IOException {
        File file2 = new File(file, ".exagear/.img_version");
        file2.createNewFile();
        FileWriter filew = new FileWriter(file2);
        filew.write(String.format("%d\n", new Object[]{Integer.valueOf(this.foundObbVersion)}));
        filew.close();
    }

    private boolean checkObbUnpackNeed() throws FileNotFoundException, IOException {
        boolean z = true;
        if (!this.exagearImage.isValid()) {
            return true;
        }
		
        if (this.exagearImage.getImageVersion() >= this.foundObbVersion) {
            z = false;
        }
        return z;
    }

    public void installImageFromObbIfNeeded() throws IOException {
        final File findObbFile = findObbFile();
        boolean checkObbUnpackNeed = checkObbUnpackNeed();
        final File path = this.exagearImage.getPath();
        if (!checkObbUnpackNeed) {
            this.callbacks.unpackingCompleted(path);
        } else if (findObbFile == null) {
            this.callbacks.noObbFound();
        } else {
            new AsyncTask() {
                protected Object doInBackground(Object... objArr) {
                    try {
						UiThread.post(new Runnable() {
								public void run() {
									ZipInstallerObb.this.callbacks.unpackingInProgress();
								}
							});
						if (ZipInstallerObb.this.keepOldFiles.length == 0) {
							SafeFileHelpers.removeDirectory(path);
							FileHelpers.createDirectory(path);
						} else {
							if (!path.exists()) {
								path.mkdir();
							} else {
								Assert.isTrue(path.isDirectory());
							}
							
							SafeFileHelpers.doWithFiles(path, 1, new FileFilter() {
									public boolean matches(File file, String str) throws IOException {
										File file2 = new File(file, str);
										for (Object equals : ZipInstallerObb.this.keepOldFiles) {
											if (file2.getName().equals(equals)) {
												return false;
											}
										}
										return true;
									}
								}, new FileCallback() {
									public void apply(File file, String str) throws IOException {
										File file2 = new File(file, str);
										if (file2.isDirectory()) {
											SafeFileHelpers.removeDirectory(file2);
										} else {
											file2.delete();
										}
									}
								});
						}
						ZipUnpacker.unpackZip(findObbFile, path, ZipInstallerObb.this.callbacks);
						FileHelpers.createDirectory(path, ExagearImagePaths.DOT_EXAGEAR);
						ZipInstallerObb.this.createFileWithObbVersion(path);
						UiThread.post(new Runnable() {
								public void run() {
									ZipInstallerObb.this.callbacks.unpackingCompleted(path);
								}
							});
                    } catch (final Throwable th) {
                        UiThread.post(new Runnable() {
								public void run() {
									ZipInstallerObb.this.callbacks.error(th.getMessage());
								}
							});
                    }
                    return null;
                }
            }.execute(new Object[0]);
        }
    }
}
