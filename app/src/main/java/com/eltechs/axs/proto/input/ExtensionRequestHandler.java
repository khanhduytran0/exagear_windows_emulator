package com.eltechs.axs.proto.input;

import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;

public interface ExtensionRequestHandler {
    byte getAssignedMajorOpcode();

    byte getFirstAssignedErrorId();

    byte getFirstAssignedEventId();

    String getName();

    void handleRequest(XClient xClient, byte b, byte b2, int i, XRequest xRequest, XResponse xResponse) throws XProtocolError, IOException;
}
