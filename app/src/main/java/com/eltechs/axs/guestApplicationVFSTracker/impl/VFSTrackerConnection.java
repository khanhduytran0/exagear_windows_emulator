package com.eltechs.axs.guestApplicationVFSTracker.impl;

import com.eltechs.axs.xconnectors.XOutputStream;

public class VFSTrackerConnection {
    private final XOutputStream outputStream;

    public VFSTrackerConnection(XOutputStream xOutputStream) {
        this.outputStream = xOutputStream;
    }
}
