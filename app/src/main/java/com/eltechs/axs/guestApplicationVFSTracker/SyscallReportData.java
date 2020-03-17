package com.eltechs.axs.guestApplicationVFSTracker;

public class SyscallReportData {
    private int fileIndex;
    private int flags;
    private int syscallNr;

    public SyscallReportData(int i, int i2, int i3) {
        this.syscallNr = i;
        this.flags = i2;
        this.fileIndex = i3;
    }

    public int getSyscallNr() {
        return this.syscallNr;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getFileIndex() {
        return this.fileIndex;
    }
}
