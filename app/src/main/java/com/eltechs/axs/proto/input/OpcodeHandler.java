package com.eltechs.axs.proto.input;

import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;

public interface OpcodeHandler {
    void handleRequest(XClient xClient, int i, byte b, XRequest xRequest, XResponse xResponse) throws XProtocolError, IOException;
}
