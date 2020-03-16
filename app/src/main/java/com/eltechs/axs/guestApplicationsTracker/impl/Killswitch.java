package com.eltechs.axs.guestApplicationsTracker.impl;

import com.eltechs.axs.annotations.UsedByNativeCode;
import com.eltechs.axs.helpers.Assert;
import java.io.IOException;

public class Killswitch {
    @UsedByNativeCode
    private int controlPipeFd;
    @UsedByNativeCode
    private int killswitchPid;

    private static native boolean initialiseNativeParts();

    private native int startKillswitch(String str, String str2, String str3, String str4);

    private native void stopKillswitch(int i, int i2);

    static {
        System.loadLibrary("ubt-helpers");
        Assert.state(initialiseNativeParts(), "Managed and native parts of Killswitch do not match one another.");
    }

    public Killswitch(String str, String str2, String str3) throws IOException {
        int startKillswitch = startKillswitch(str, str2, str3, "/mnt/sdcard/killswitch.txt");
        if (startKillswitch < 0) {
            throw new IOException(String.format("Failed to start the killswitch; errno = %d.", new Object[]{Integer.valueOf(-startKillswitch)}));
        }
    }

    public void stop() {
        stopKillswitch(this.killswitchPid, this.controlPipeFd);
        this.killswitchPid = -1;
        this.controlPipeFd = -1;
    }
}
