package com.eltechs.ed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class WineRegistryEditor {
    private boolean mChanged = false;
    private String mContents;
    private File mFile;

    class KeyLocation {
        int mBegin;
        int mContentsBegin;
        int mEnd;

        KeyLocation(int i, int i2, int i3) {
            this.mBegin = i;
            this.mContentsBegin = i2;
            this.mEnd = i3;
        }
    }

    class ParamLocation {
        int mBegin;
        int mEnd;
        int mValBegin;

        ParamLocation(int i, int i2, int i3) {
            this.mBegin = i;
            this.mValBegin = i2;
            this.mEnd = i3;
        }
    }

    public WineRegistryEditor(File file) {
        this.mFile = file;
    }

    private String stringToInternal(String str) {
        return str.replace("\\", "\\\\");
    }

    private String stringFromInternal(String str) {
        return str.replace("\\\\", "\\");
    }

    private String insertString(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, i));
        sb.append(str2);
        sb.append(str.substring(i));
        return sb.toString();
    }

    private String replaceString(String str, int i, int i2, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, i));
        sb.append(str2);
        sb.append(str.substring(i2));
        return sb.toString();
    }

    public void read() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.mFile);
        byte[] bArr = new byte[((int) this.mFile.length())];
        fileInputStream.read(bArr);
        fileInputStream.close();
        this.mContents = new String(bArr);
        this.mChanged = false;
    }

    public void write() throws IOException {
        if (this.mChanged) {
            FileOutputStream fileOutputStream = new FileOutputStream(this.mFile);
            fileOutputStream.write(this.mContents.getBytes());
            fileOutputStream.close();
            this.mChanged = false;
        }
    }

    private KeyLocation getKeyLocation(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(stringToInternal(str));
        sb.append("]");
        int indexOf = this.mContents.indexOf(sb.toString());
        if (indexOf == -1) {
            return new KeyLocation(-1, -1, -1);
        }
        int indexOf2 = this.mContents.indexOf(10, indexOf) + 1;
        int indexOf3 = this.mContents.indexOf(91, indexOf2);
        if (indexOf3 == -1) {
            indexOf3 = this.mContents.length();
        }
        do {
            indexOf3--;
        } while (this.mContents.charAt(indexOf3) == 10);
        return new KeyLocation(indexOf, indexOf2, indexOf3 + 1);
    }

    private ParamLocation getParamLocation(KeyLocation keyLocation, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(stringToInternal(str));
        sb.append("\"=");
        String sb2 = sb.toString();
        String substring = this.mContents.substring(keyLocation.mContentsBegin, keyLocation.mEnd);
        int indexOf = substring.indexOf(sb2);
        if (indexOf == -1) {
            return new ParamLocation(-1, -1, -1);
        }
        int length = sb2.length() + indexOf;
        int indexOf2 = substring.indexOf(10, length);
        if (indexOf2 == -1) {
            indexOf2 = substring.length();
        }
        return new ParamLocation(keyLocation.mContentsBegin + indexOf, keyLocation.mContentsBegin + length, keyLocation.mContentsBegin + indexOf2);
    }

    private KeyLocation createKey(String str) {
        int length = this.mContents.length() + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mContents);
        sb.append("\n[");
        sb.append(stringToInternal(str));
        sb.append(String.format("] %d\n", new Object[]{Long.valueOf(Calendar.getInstance().getTimeInMillis() / 1000)}));
        this.mContents = sb.toString();
        int length2 = this.mContents.length() - 1;
        return new KeyLocation(length, length2, length2);
    }

    public boolean isParamExists(String str, String str2) {
        KeyLocation keyLocation = getKeyLocation(str);
        if (keyLocation.mBegin == -1 || getParamLocation(keyLocation, str2).mBegin == -1) {
            return false;
        }
        return true;
    }

    public String getStringParam(String str, String str2) {
        KeyLocation keyLocation = getKeyLocation(str);
        if (keyLocation.mBegin == -1) {
            return null;
        }
        ParamLocation paramLocation = getParamLocation(keyLocation, str2);
        if (paramLocation.mBegin == -1) {
            return null;
        }
        return stringFromInternal(this.mContents.substring(paramLocation.mValBegin + 1, paramLocation.mEnd - 1));
    }

    public void setStringParam(String str, String str2, String str3) {
        KeyLocation keyLocation = getKeyLocation(str);
        if (keyLocation.mBegin == -1) {
            keyLocation = createKey(str);
        }
        ParamLocation paramLocation = getParamLocation(keyLocation, str2);
        if (paramLocation.mBegin == -1) {
            int i = keyLocation.mEnd;
            StringBuilder sb = new StringBuilder();
            sb.append("\n\"");
            sb.append(stringToInternal(str2));
            sb.append("\"=\"\"");
            String sb2 = sb.toString();
            int length = (keyLocation.mEnd + sb2.length()) - 2;
            int i2 = length + 2;
            this.mContents = insertString(this.mContents, keyLocation.mEnd, sb2);
            paramLocation = new ParamLocation(i, length, i2);
        }
        this.mContents = replaceString(this.mContents, paramLocation.mValBegin + 1, paramLocation.mEnd - 1, stringToInternal(str3));
        this.mChanged = true;
    }

    public Integer getDwordParam(String str, String str2) {
        KeyLocation keyLocation = getKeyLocation(str);
        if (keyLocation.mBegin == -1) {
            return null;
        }
        ParamLocation paramLocation = getParamLocation(keyLocation, str2);
        if (paramLocation.mBegin == -1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(this.mContents.substring(paramLocation.mValBegin + "dword:".length(), paramLocation.mEnd));
        return Integer.decode(sb.toString());
    }

    public void setDwordParam(String str, String str2, Integer num) {
        KeyLocation keyLocation = getKeyLocation(str);
        if (keyLocation.mBegin == -1) {
            keyLocation = createKey(str);
        }
        ParamLocation paramLocation = getParamLocation(keyLocation, str2);
        if (paramLocation.mBegin == -1) {
            int i = keyLocation.mEnd;
            StringBuilder sb = new StringBuilder();
            sb.append("\n\"");
            sb.append(stringToInternal(str2));
            sb.append("\"=");
            String sb2 = sb.toString();
            int length = sb2.length() + i;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append("dword:00000000");
            String sb4 = sb3.toString();
            int length2 = sb4.length() + i;
            this.mContents = insertString(this.mContents, keyLocation.mEnd, sb4);
            paramLocation = new ParamLocation(i, length, length2);
        }
        this.mContents = replaceString(this.mContents, paramLocation.mValBegin + "dword:".length(), paramLocation.mEnd, String.format("%08x", new Object[]{num}));
        this.mChanged = true;
    }
}
