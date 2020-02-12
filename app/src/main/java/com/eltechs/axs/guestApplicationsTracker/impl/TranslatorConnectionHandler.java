package com.eltechs.axs.guestApplicationsTracker.impl;

import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.nio.ByteOrder;

public class TranslatorConnectionHandler implements ConnectionHandler<TranslatorConnection> {
    private final GuestApplicationsCollection guestApplicationsCollection;

    public TranslatorConnectionHandler(GuestApplicationsCollection guestApplicationsCollection2) {
        this.guestApplicationsCollection = guestApplicationsCollection2;
    }

    public TranslatorConnection handleNewConnection(XInputStream xInputStream, XOutputStream xOutputStream) {
        xInputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        xOutputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        return new TranslatorConnection(xOutputStream);
    }

    public void handleConnectionShutdown(TranslatorConnection translatorConnection) {
        translatorConnection.processShutdown();
    }
}
