package com.eltechs.axs.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public abstract class ZipUnpacker {

    public interface Callbacks {
        void reportProgress(long j);
    }

    private ZipUnpacker() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00dc A[SYNTHETIC, Splitter:B:37:0x00dc] */
    public static void unpackZip(File file, File file2, Callbacks callbacks) throws IOException {
        ZipFile zipFile;
        // Throwable th;
        File file3 = file2;
        final Callbacks callbacks2 = callbacks;
        Assert.isTrue(file2.isDirectory(), String.format("'%s' must be a directory.", new Object[]{file2.getAbsolutePath()}));
        long length = file.length();
        if (callbacks2 != null) {
            try {
                UiThread.post(new Runnable() {
                    public void run() {
                        callbacks2.reportProgress(-1);
                    }
                });
            } catch (/* IOException */ Throwable e) {
                // th = e;
                zipFile = null;
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    } catch (IOException unused) {
                    }
                }
                throw new RuntimeException(e);
            }
        }
        zipFile = new ZipFile(file);
        try {
            Enumeration entries = zipFile.getEntries();
            long j = 0;
            long j2 = 0;
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) entries.nextElement();
				File file4 = new File(file3, zipArchiveEntry.getName());
                if (zipArchiveEntry.isDirectory()) {
                    if (file4.exists()) {
                        if (!file4.isDirectory()) {
                            throw new IOException(String.format("Attempted to create directory over file with same name '%s'.", new Object[]{file4.getAbsolutePath()}));
                        }
                    } else if (!file4.mkdirs()) {
                        throw new IOException(String.format("Failed to create the directory '%s'.", new Object[]{file4.getAbsolutePath()}));
                    }
                } else if (file4.exists()) {
					/*
					 * MOD: Additional code.
					 *
					 * Do nothing and skip this exists file.
					 * Don't use 'continue' because ExaGear
					 * has update code it's progress in the
					 * end of this loop.
					 */
				} else if (zipArchiveEntry.isUnixSymlink()) {
                    extractOneSymlink(zipFile, zipArchiveEntry, new File(file3, zipArchiveEntry.getName()));
                    j += zipArchiveEntry.getCompressedSize();
                } else {
                    extractOneFile(zipFile, zipArchiveEntry, new File(file3, zipArchiveEntry.getName()));
                    j += zipArchiveEntry.getCompressedSize();
                }
                final long j3 = (100 * j) / length;
                if (j3 >= 5 + j2) {
                    if (callbacks2 != null) {
                        UiThread.post(new Runnable() {
                            public void run() {
                                callbacks2.reportProgress(j3);
                            }
                        });
                    }
                    j2 = j3;
                }
            }
            zipFile.close();
        } catch (IOException e2) {
            // th = e2;
            if (zipFile != null) {
            }
            throw new RuntimeException(e2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0041 A[SYNTHETIC, Splitter:B:22:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0046 A[SYNTHETIC, Splitter:B:26:0x0046] */
    private static void extractOneFile(ZipFile zipFile, ZipArchiveEntry zipArchiveEntry, File file) throws IOException {
        IOException e;
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        Assert.isFalse(zipArchiveEntry.isDirectory(), "extractOneFile() must be applied to file entries.");
        Assert.isFalse(zipArchiveEntry.isUnixSymlink(), "extractOneFile() must be applied to file entries.");
        OutputStream outputStream = null;
        try {
            inputStream = zipFile.getInputStream(zipArchiveEntry);
            try {
				file.getParentFile().mkdirs();
                fileOutputStream = new FileOutputStream(file);
            } catch (IOException e2) {
                e = e2;
                if (inputStream != null) {
                }
                if (outputStream != null) {
                }
                throw e;
            }
            try {
                IOStreamHelpers.copy(inputStream, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                file.setExecutable((zipArchiveEntry.getUnixMode() & 73) != 0);
            } catch (IOException e3) {
                e = e3;
                outputStream = fileOutputStream;
                if (inputStream != null) {
                }
                if (outputStream != null) {
                }
                throw e;
            }
        } catch (IOException e4) {
            e = e4;
            inputStream = null;
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
            throw e;
        }
    }

    private static void extractOneSymlink(ZipFile zipFile, ZipArchiveEntry zipArchiveEntry, File file) throws IOException {
        Assert.isFalse(zipArchiveEntry.isDirectory(), "extractOneSymlink() must be applied to symlinks.");
        Assert.isTrue(zipArchiveEntry.isUnixSymlink(), "extractOneSymlink() must be applied to symlinks.");
        SafeFileHelpers.symlink(zipFile.getUnixSymlink(zipArchiveEntry), file.getAbsolutePath());
    }
}
