package com.eltechs.axs.guestApplicationVFSTracker;

import com.eltechs.axs.guestApplicationVFSTracker.impl.VFSTrackerRequestsDispatcher;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.io.Serializable;
import java.io.*;

public class StupidSyscallReportHandler implements SyscallReportHandler, Serializable {
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0042 */
    public boolean handleSyscall(SyscallReportData syscallReportData, XOutputStream xOutputStream) {
        System.out.printf("Tracker: syscall = %d, flags: %08x, index = %d\n", new Object[]{Integer.valueOf(syscallReportData.getSyscallNr()), Integer.valueOf(syscallReportData.getFlags()), Integer.valueOf(syscallReportData.getFileIndex())});
        xOutputStream.lock();
        try {
			xOutputStream.writeInt(VFSTrackerRequestsDispatcher.MAGIC);
			xOutputStream.writeShort((short) 10);
        	xOutputStream.writeInt(0);
        	xOutputStream.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			Assert.unreachable();
		}
    }
}
