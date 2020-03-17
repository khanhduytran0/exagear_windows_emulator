package com.eltechs.axs.guestApplicationVFSTracker;

import android.util.Log;
import com.eltechs.axs.environmentService.components.VFSTrackerComponent;
import com.eltechs.axs.guestApplicationVFSTracker.impl.VFSTrackerRequestsDispatcher;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.io.*;

public class SyscallReportsReceiver implements SyscallReportHandler {
    private final String file;
    private boolean isOn;
    private final SyscallReportsReceiverListenersList listeners = new SyscallReportsReceiverListenersList();
    private int numCloseCalled;
    private int numOpenRDCalled;
    private int numOpenWRCalled;
    private final VFSTrackerComponent vfsTracker;

    public SyscallReportsReceiver(VFSTrackerComponent vFSTrackerComponent, String str) {
        this.vfsTracker = vFSTrackerComponent;
        this.file = str;
    }

    public void startReceiving() {
        this.isOn = true;
        this.numOpenRDCalled = 0;
        this.numOpenWRCalled = 0;
        this.numCloseCalled = 0;
    }

    public void stopReceiving() {
        this.isOn = false;
    }

    public boolean isReceiving() {
        return this.isOn;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x00a8 */
    public boolean handleSyscall(SyscallReportData syscallReportData, XOutputStream xOutputStream) {
        Log.d("SYSCALL", String.format("handle some syscall! file_index: %d, syscall: %d flags: %08x", new Object[]{Integer.valueOf(syscallReportData.getFileIndex()), Integer.valueOf(syscallReportData.getSyscallNr()), Integer.valueOf(syscallReportData.getFlags())}));
        if (!this.vfsTracker.getTrackedFileByIndex(syscallReportData.getFileIndex()).equals(this.file)) {
            return false;
        }
        Log.d("SYSCALL", String.format("handle %s!", new Object[]{this.file}));
        if (this.isOn) {
            Log.d("SYSCALL", String.format("FSM: on %s!\n", new Object[]{this.file}));
            switch (syscallReportData.getSyscallNr()) {
                case 5:
                    if ((syscallReportData.getFlags() & 3) == 0) {
                        this.numOpenRDCalled++;
                        break;
                    } else {
                        this.numOpenWRCalled++;
                        break;
                    }
                case 6:
                    this.numCloseCalled++;
                    break;
                default:
                    Assert.unreachable("Only open() and close() are supported in ubt");
                    break;
            }
        }
        this.listeners.notifySyscallReported(this);
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

    public int getNumOpenRDCalled() {
        return this.numOpenRDCalled;
    }

    public int getNumOpenWRCalled() {
        return this.numOpenWRCalled;
    }

    public int getNumCloseCalled() {
        return this.numCloseCalled;
    }

    public void addListener(SyscallReportsReceiverListener syscallReportsReceiverListener) {
        this.listeners.addListener(syscallReportsReceiverListener);
    }

    public void removeListener(SyscallReportsReceiverListener syscallReportsReceiverListener) {
        this.listeners.removeListener(syscallReportsReceiverListener);
    }
}
