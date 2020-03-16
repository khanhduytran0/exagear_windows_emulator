package com.eltechs.axs.ExagearImageConfiguration;

import java.io.Serializable;

public class WineDiskInfo implements Serializable {
    public final String diskLetter;
    public final String diskTargetPath;

    public WineDiskInfo(String str, String str2) {
        this.diskLetter = str;
        this.diskTargetPath = str2;
    }
}
