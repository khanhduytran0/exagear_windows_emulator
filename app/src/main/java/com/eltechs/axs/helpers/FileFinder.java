package com.eltechs.axs.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {
    public static List<File> findDirectory(File file, int i, String str) throws IOException {
        Assert.isTrue(file.isDirectory());
        ArrayList arrayList = new ArrayList();
        File file2 = new File(file, str);
        if (file2.isDirectory()) {
            arrayList.add(file2.getCanonicalFile());
            return arrayList;
        }
        if (i > 0) {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                return arrayList;
            }
            for (File file3 : listFiles) {
                if (file3.isDirectory()) {
                    List findDirectory = findDirectory(file3, i - 1, str);
                    if (!findDirectory.isEmpty()) {
                        arrayList.addAll(findDirectory);
                    }
                }
            }
        }
        return arrayList;
    }
}
