package com.eltechs.axs.sysvipc;

import com.eltechs.axs.annotations.UsedByNativeCode;
import com.eltechs.axs.helpers.Assert;
import java.io.IOException;

public class Emulator {
    @UsedByNativeCode
    private int controlPipeFd;
    @UsedByNativeCode
    private int emulatorPid;

    private static native boolean initialiseNativeParts();

    private native int startEmulator(String str, String str2, String str3, String str4);

    private native void stopEmulator(int i, int i2);

    static {
        System.loadLibrary("ipc-helpers");
        Assert.state(initialiseNativeParts(), "Managed and native parts of Emulator do not match one another.");
    }

    public Emulator(String str, String str2, String str3) throws IOException {
        int startEmulator = startEmulator(str2, str3, str, "/mnt/sdcard/ipc-emulator.txt");
        if (startEmulator < 0) {
            throw new IOException(String.format("Failed to start the Sys V IPC emulator; errno = %d.", new Object[]{Integer.valueOf(-startEmulator)}));
        }
    }

    public void stop() {
        stopEmulator(this.emulatorPid, this.controlPipeFd);
        this.emulatorPid = -1;
        this.controlPipeFd = -1;
    }
}
