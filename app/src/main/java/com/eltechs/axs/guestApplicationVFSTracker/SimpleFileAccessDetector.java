package com.eltechs.axs.guestApplicationVFSTracker;

import com.eltechs.axs.environmentService.components.VFSTrackerComponent;
import com.eltechs.axs.guestApplicationVFSTracker.impl.VFSTrackerRequestsDispatcher;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.io.Serializable;
import java.io.*;

public abstract class SimpleFileAccessDetector implements SyscallReportHandler, Serializable {
    private final String[] fileList;
    private final VFSTrackerComponent ownerComponent;

    public abstract boolean fileAccessed(String str);

    protected SimpleFileAccessDetector(String[] strArr, VFSTrackerComponent vFSTrackerComponent) {
        this.fileList = strArr;
        this.ownerComponent = vFSTrackerComponent;
    }

    public boolean handleSyscall(SyscallReportData syscallReportData, XOutputStream xOutputStream) {
        try {
			String trackedFileByIndex = this.ownerComponent.getTrackedFileByIndex(syscallReportData.getFileIndex());
			for (String equals : this.fileList) {
				if (equals.equals(trackedFileByIndex)) {
					if (syscallReportData.getSyscallNr() == 5) {
						sendReply(xOutputStream, fileAccessed(trackedFileByIndex));
					} else {
						sendReply(xOutputStream, true);
					}
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0019 */
    private void sendReply(XOutputStream xOutputStream, boolean z) throws IOException {
        xOutputStream.lock();
        xOutputStream.writeInt(VFSTrackerRequestsDispatcher.MAGIC);
        xOutputStream.writeShort((short) 10);
        xOutputStream.writeInt(z ^ true ? 1 : 0);
        xOutputStream.flush();
        Assert.unreachable();
    }
}
