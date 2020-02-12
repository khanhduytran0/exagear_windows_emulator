package com.eltechs.axs.guestApplicationVFSTracker.impl;

import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.nio.ByteOrder;

public class VFSTrackerConnectionHandler implements ConnectionHandler<VFSTrackerConnection> {
    public void handleConnectionShutdown(VFSTrackerConnection vFSTrackerConnection) {
    }

    public VFSTrackerConnection handleNewConnection(XInputStream xInputStream, XOutputStream xOutputStream) {
        xInputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        xOutputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        return new VFSTrackerConnection(xOutputStream);
    }
}
