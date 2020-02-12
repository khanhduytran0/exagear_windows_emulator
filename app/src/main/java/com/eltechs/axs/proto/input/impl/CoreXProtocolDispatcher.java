package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.proto.input.ConfigurableRequestsDispatcher;
import com.eltechs.axs.proto.input.OpcodeHandler;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.errors.BadRequest;
import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;

public class CoreXProtocolDispatcher implements ConfigurableRequestsDispatcher {
    private final OpcodeHandlersRegistry handlersRegistry = new OpcodeHandlersRegistry();

    public byte getAssignedMajorOpcode() {
        return 0;
    }

    public byte getFirstAssignedErrorId() {
        return 0;
    }

    public byte getFirstAssignedEventId() {
        return 0;
    }

    public String getName() {
        return "CORE";
    }

    public void handleRequest(XClient xClient, byte b, byte b2, int i, XRequest xRequest, XResponse xResponse) throws XProtocolError, IOException {
        OpcodeHandler handler = this.handlersRegistry.getHandler(b);
        xRequest.setMinorOpcode((short) 0);
        if (handler == null) {
            throw new BadRequest();
        }
        handler.handleRequest(xClient, i, b2, xRequest, xResponse);
    }

    public void installRequestHandler(int i, OpcodeHandler opcodeHandler) {
        this.handlersRegistry.installRequestHandler(i, opcodeHandler);
    }
}
