package com.eltechs.axs.guestApplicationVFSTracker;

import android.util.Log;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class SyscallReportMultiplexor implements SyscallReportHandler, Serializable {
    private final Collection<SyscallReportHandler> handlers = new ArrayList();

    public void addHandler(SyscallReportHandler syscallReportHandler) {
        this.handlers.add(syscallReportHandler);
    }

    public void removeHandler(SyscallReportHandler syscallReportHandler) {
        this.handlers.remove(syscallReportHandler);
    }

    public boolean handleSyscall(SyscallReportData syscallReportData, XOutputStream xOutputStream) {
        Log.d("SYSCALL", String.format("Handling syscall for %d : nr : %d fl: %d", new Object[]{Integer.valueOf(syscallReportData.getFileIndex()), Integer.valueOf(syscallReportData.getSyscallNr()), Integer.valueOf(syscallReportData.getFlags())}));
        for (SyscallReportHandler handleSyscall : this.handlers) {
            if (handleSyscall.handleSyscall(syscallReportData, xOutputStream)) {
                return true;
            }
        }
        Assert.unreachable(String.format("No handlers found for file %d; ubt hung up!", new Object[]{Integer.valueOf(syscallReportData.getFileIndex())}));
        return false;
    }
}
