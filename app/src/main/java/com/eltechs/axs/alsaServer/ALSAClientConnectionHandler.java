package com.eltechs.axs.alsaServer;

import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.nio.ByteOrder;

public class ALSAClientConnectionHandler implements ConnectionHandler<ALSAClient> {
    final PCMPlayersManager soundServer;

    public ALSAClientConnectionHandler(PCMPlayersManager pCMPlayersManager) {
        this.soundServer = pCMPlayersManager;
    }

    public ALSAClient handleNewConnection(XInputStream xInputStream, XOutputStream xOutputStream) {
        xInputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        xOutputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        return new ALSAClient(this.soundServer);
    }

    public void handleConnectionShutdown(ALSAClient aLSAClient) {
        aLSAClient.reset();
    }
}
