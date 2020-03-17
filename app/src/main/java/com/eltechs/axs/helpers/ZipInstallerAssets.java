package com.eltechs.axs.helpers;

import android.content.Context;
import android.os.AsyncTask;
import com.eltechs.ed.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ZipInstallerAssets {

    public interface InstallCallback {
        void installationFailed(String str);

        void installationFinished(String str);
    }

    public static void installIfNecessary(final Context context, final InstallCallback installCallback, final File file, final String str) {
        if (!file.exists()) {
            new AsyncTask() {
                /* access modifiers changed from: protected */
                /* JADX WARNING: Removed duplicated region for block: B:24:0x006a A[SYNTHETIC, Splitter:B:24:0x006a] */
                /* JADX WARNING: Removed duplicated region for block: B:28:0x006f A[SYNTHETIC, Splitter:B:28:0x006f] */
                /* JADX WARNING: Removed duplicated region for block: B:33:0x007a  */
                /* JADX WARNING: Removed duplicated region for block: B:36:0x0085  */
                public Object doInBackground(Object... objArr) {
                    OutputStream outputStream;
                    InputStream inputStream;
                    FileOutputStream fileOutputStream;
                    File fileObj = new File(file, str);
                    try {
                        if (!file.mkdirs()) {
                            throw new IOException(String.format("Failed to create the directory '%s'.", new Object[]{file.getAbsolutePath()}));
                        }
                        InputStream open = context.getAssets().open(str);
                        try {
                            fileOutputStream = new FileOutputStream(fileObj);
                        } catch (final IOException e) {
							e.printStackTrace();
                            outputStream = null;
                            // IOException iOException = e;
                            inputStream = open;
                            // e = iOException;
                            if (inputStream != null) {
                            }
                            if (outputStream != null) {
                            }
                            if (fileObj.exists()) {
                            }
                            if (fileObj.exists()) {
                            }
                            UiThread.post(new Runnable() {
                                public void run() {
                                    installCallback.installationFailed(String.format("%s; %s", new Object[]{context.getResources().getString(R.string.fail_to_unpack_zip), e.getMessage()}));
                                }
                            });
                            return null;
                        }
                        try {
                            IOStreamHelpers.copy(open, fileOutputStream);
                            open.close();
                        } catch (final IOException e2) {
							e2.printStackTrace();
                            OutputStream outputStream2 = fileOutputStream;
                            inputStream = open;
                            // e = e2;
                            outputStream = outputStream2;
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException unused) {
                                }
                            }
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException unused2) {
                                }
                            }
                            if (fileObj.exists()) {
                                fileObj.delete();
                            }
							/*
                            if (file.exists()) {
                                file.delete();
                            }
							*/
                            UiThread.post(new Runnable() {
                                public void run() {
                                    installCallback.installationFailed(String.format("%s; %s", new Object[]{context.getResources().getString(R.string.fail_to_unpack_zip), e2.getMessage()}));
                                }
                            });
                            return null;
                        }
                        try {
                            fileOutputStream.close();
                            ZipUnpacker.unpackZip(fileObj, file, null);
                            file.delete();
                            UiThread.post(new Runnable() {
                                public void run() {
                                    installCallback.installationFinished(file.getAbsolutePath());
                                }
                            });
                        } catch (final IOException e3) {
                            e3.printStackTrace();
                            outputStream = fileOutputStream;
                            inputStream = null;
                            if (inputStream != null) {
                            }
                            if (outputStream != null) {
                            }
                            if (file.exists()) {
                            }
                            if (file.exists()) {
                            }
                            UiThread.post(new Runnable() {
                                public void run() {
                                    installCallback.installationFailed(String.format("%s; %s", new Object[]{context.getResources().getString(R.string.fail_to_unpack_zip), e3.getMessage()}));
                                }
                            });
                            return null;
                        }
                        return null;
                    } catch (IOException e4) {
                        e4.printStackTrace();
                        inputStream = null;
                        outputStream = null;
                    }
					return null;
				}
            }.execute(new Object[0]);
        } else if (file.isDirectory()) {
            installCallback.installationFinished(file.getAbsolutePath());
        } else {
            installCallback.installationFailed(context.getResources().getString(R.string.directory_is_occupied));
        }
    }
}
