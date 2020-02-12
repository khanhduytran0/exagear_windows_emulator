package com.eltechs.axs.guestApplicationVFSTracker;

import com.eltechs.axs.xconnectors.XOutputStream;

public interface SyscallReportHandler {
    boolean handleSyscall(SyscallReportData syscallReportData, XOutputStream xOutputStream);
}
