package com.eltechs.axs.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public abstract class FileHelpers {
    private static final String UBT_FAKE_SYMLINK_SUFFIX = "_symlink";

    public static boolean checkCaseInsensitivityInDirectory(File file) throws IOException {
        return false;
    }

    private FileHelpers() {
    }

    public static void copyFilesInDirectoryNoReplace(File file, File file2) throws IOException {
        String[] list;
        if (!file.isDirectory()) {
            throw new IOException("Copy source is not a directory.");
        } else if (file2.exists() || file2.mkdir()) {
            for (String str : file.list()) {
                File file3 = new File(file, str);
                File file4 = new File(file2, str);
                if (!file4.exists()) {
                    copyFile(file3, file4);
                }
            }
        } else {
            throw new IOException("Failed to create destination directory.");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x005c  */
    public static void copyFile(File file, File file2) throws IOException {
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        if (!file.exists() || !file.isFile()) {
            throw new IOException("Copy source is not an existing regular file.");
        }
        if (file2.exists()) {
            if (!file2.isFile() || !file2.canWrite()) {
                throw new IOException("Destination is not a file or is a read-only file..");
            }
        } else if (!file2.createNewFile()) {
            throw new IOException("Can't create destination file.");
        }
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            try {
                fileOutputStream = new FileOutputStream(file2);
            } catch (Throwable th) {
                th = th;
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                throw th;
            }
            try {
                IOStreamHelpers.copy(inputStream, fileOutputStream);
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Throwable th2) {
                // th = th2;
                outputStream = fileOutputStream;
                if (inputStream != null) {
                }
                if (outputStream != null) {
                }
                throw new RuntimeException(th2);
            }
        } catch (Throwable th3) {
            // th = th3;
            inputStream = null;
            if (inputStream != null) {
            }
            if (outputStream != null) {
            }
            throw new RuntimeException(th3);
        }
    }

    public static void copyDirectory(File file, File file2) throws IOException {
        if (!file.isDirectory()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Source '");
            sb.append(file);
            sb.append("' does not exist or is not a directory");
            throw new IOException(sb.toString());
        } else if (file.getCanonicalPath().equals(file2.getCanonicalPath())) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Source '");
            sb2.append(file);
            sb2.append("' and destination '");
            sb2.append(file2);
            sb2.append("' are the same");
            throw new IOException(sb2.toString());
        } else if (file2.getCanonicalPath().startsWith(file.getCanonicalPath())) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Destination '");
            sb3.append(file2);
            sb3.append("' is a subfolder of source '");
            sb3.append(file);
            sb3.append("'");
            throw new IOException(sb3.toString());
        } else {
            doCopyDirectory(file, file2);
        }
    }

    private static void doCopyDirectory(File file, File file2) throws IOException {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to list contents of ");
            sb.append(file);
            throw new IOException(sb.toString());
        }
        if (file2.exists()) {
            if (!file2.isDirectory()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Destination '");
                sb2.append(file2);
                sb2.append("' exists but is not a directory");
                throw new IOException(sb2.toString());
            }
        } else if (!file2.mkdirs() && !file2.isDirectory()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Destination '");
            sb3.append(file2);
            sb3.append("' directory cannot be created");
            throw new IOException(sb3.toString());
        }
        if (!file2.canWrite()) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Destination '");
            sb4.append(file2);
            sb4.append("' cannot be written to");
            throw new IOException(sb4.toString());
        }
        for (File file3 : listFiles) {
            File file4 = new File(file2, file3.getName());
            if (file3.isDirectory()) {
                doCopyDirectory(file3, file4);
            } else {
                copyFile(file3, file4);
            }
        }
    }

    public static boolean doesFileExist(File file, String str) {
        return new File(file, str).exists();
    }

    public static boolean doesDirectoryExist(String str) {
        return new File(str).isDirectory();
    }

    public static void createFakeSymlink(String str, String str2, String str3) throws IOException {
        File file = new File(String.format("%s/%s%s", new Object[]{StringHelpers.removeTrailingSlashes(str), str2, UBT_FAKE_SYMLINK_SUFFIX}));
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(str3.getBytes());
        fileOutputStream.write(10);
        fileOutputStream.close();
    }

    public static String fixPathForVFAT(String str) {
        return str.replace(':', '_');
    }

    public static void moveDirectory(File file, File file2) throws IOException {
        if (!file.exists() || !file.isDirectory()) {
            throw new IOException("Copy source is not an existing directory.");
        }
        if (file2.exists()) {
            if (!file2.isDirectory()) {
                throw new IOException("Destination is an existing file.");
            } else if (file2.list().length != 0) {
                throw new IOException("Destination directory is not empty.");
            } else if (!file2.delete()) {
                throw new IOException("Failed to delete existing destination directory.");
            }
        }
        if (!file.renameTo(file2)) {
            throw new IOException("Failed to rename source directory.");
        }
    }

    public static void createDirectory(File file) throws IOException {
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IOException(String.format("Path '%s' already exists and it is not a directory.", new Object[]{file.getAbsolutePath()}));
            }
        } else if (!file.mkdirs()) {
            throw new IOException("Can't create directory.");
        }
    }

    public static File createDirectory(File file, String str) {
        File file2 = new File(file, str);
        file2.mkdirs();
        return file2;
    }

    public static File touch(String str) throws IOException {
        File file = new File(str);
        file.createNewFile();
        return file;
    }

    public static File touch(File file, String str) throws IOException {
        File file2 = new File(file, str);
        file2.createNewFile();
        return file2;
    }

    public static File getSuperParent(File file) {
        File parentFile = file.getParentFile();
        return parentFile.getPath().equals("/") ? file : getSuperParent(parentFile);
    }

    public static String cutExagearComponentFromPath(File file) {
        String[] split = file.getAbsolutePath().split("ExaGear", 2);
        boolean z = false;
        Assert.isTrue(split.length >= 1, "cutExagearComponentFromPath : Wrong Exagear path");
        if (split.length < 2) {
            return "";
        }
        if (split.length == 2) {
            z = true;
        }
        Assert.isTrue(z, "cutExagearComponentFromPath: Something goes wrong");
        return split[1];
    }

    public static String getExagearRootFromPath(File file) {
        String[] split = file.getAbsolutePath().split("ExaGear");
        boolean z = true;
        if (split.length < 1) {
            z = false;
        }
        Assert.isTrue(z, "getExagearRootFromPath : Path without Exagear component");
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("ExaGear");
        return sb.toString();
    }

    public static String cutRootPrefixFromPath(File file, File file2) {
        String absolutePath = file.getAbsolutePath();
        String absolutePath2 = file2.getAbsolutePath();
        String substring = absolutePath.substring(absolutePath2.length());
        boolean startsWith = absolutePath.startsWith(absolutePath2);
        StringBuilder sb = new StringBuilder();
        sb.append(absolutePath2);
        sb.append(" isn't a prefix of ");
        sb.append(absolutePath);
        Assert.state(startsWith, sb.toString());
        boolean z = false;
        if (substring.charAt(0) == '/') {
            z = true;
        }
        Assert.state(z);
        return substring;
    }

    public static List<String> readAsLines(File file) throws IOException {
        LinkedList linkedList = new LinkedList();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		
		try {
			while (true) {
				String readLine = bufferedReader.readLine();
				if (readLine == null) {
					return linkedList;
				}
				linkedList.add(readLine);

			}
		} finally {
			bufferedReader.close();
		}
    }

    public static boolean replaceStringInFile(File file, String str, String str2) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[((int) file.length())];
        fileInputStream.read(bArr);
        fileInputStream.close();
        String str3 = new String(bArr);
        if (!str3.contains(str)) {
            return false;
        }
        String replace = str3.replace(str, str2);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(replace.getBytes());
        fileOutputStream.close();
        return true;
    }
}
