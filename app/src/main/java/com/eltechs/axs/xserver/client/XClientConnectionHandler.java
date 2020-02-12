package com.eltechs.axs.xserver.client;

import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xserver.XServer;

public class XClientConnectionHandler implements ConnectionHandler<XClient> {
    private XServer xServer;

    public XClientConnectionHandler(XServer xServer2) {
        this.xServer = xServer2;
    }

    public XClient handleNewConnection(XInputStream xInputStream, XOutputStream xOutputStream) {
        return new XClient(this.xServer, xOutputStream);
    }

    public void handleConnectionShutdown(XClient xClient) {
        xClient.freeAssociatedResources();
    }
}
