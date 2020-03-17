package com.eltechs.axs.helpers;

public class StringHelpers {
    public static String removeTrailingSlashes(String str) {
        int length = str.length() - 1;
        while (str.charAt(length) == '/' && length > 0) {
            length--;
        }
        return str.substring(0, length + 1);
    }

    public static String appendTrailingSlash(String str) {
        if (str.charAt(str.length() - 1) == '/') {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append('/');
        return stringBuilder.toString();
    }
}

