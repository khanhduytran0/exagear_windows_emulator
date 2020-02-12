package com.eltechs.axs.proto.input;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.proto.input.errors.BadRequest;
import com.eltechs.axs.proto.input.impl.OpcodeHandlersRegistry;
import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;

public abstract class TrivialExtensionDispatcher implements ConfigurableRequestsDispatcher {
    private final byte firstAssignedErrorId;
    private final byte firstAssignedEventId;
    private final OpcodeHandlersRegistry handlersRegistry = new OpcodeHandlersRegistry();
    private final byte majorOpcode;
    private final String name;

    protected TrivialExtensionDispatcher(byte b, String str, byte b2, byte b3) {
        this.majorOpcode = b;
        this.name = str;
        this.firstAssignedEventId = b2;
        this.firstAssignedErrorId = b3;
    }

    public final byte getAssignedMajorOpcode() {
        return this.majorOpcode;
    }

    public final String getName() {
        return this.name;
    }

    public final byte getFirstAssignedEventId() {
        return this.firstAssignedEventId;
    }

    public final byte getFirstAssignedErrorId() {
        return this.firstAssignedErrorId;
    }

    public final void handleRequest(XClient xClient, byte b, byte b2, int i, XRequest xRequest, XResponse xResponse) throws XProtocolError, IOException {
        short extendAsUnsigned = (short) ArithHelpers.extendAsUnsigned(b2);
        OpcodeHandler handler = this.handlersRegistry.getHandler(extendAsUnsigned);
        xRequest.setMinorOpcode(extendAsUnsigned);
        if (handler == null) {
            throw new BadRequest();
        }
        handler.handleRequest(xClient, i, b2, xRequest, xResponse);
    }

    public final void installRequestHandler(int i, OpcodeHandler opcodeHandler) {
        this.handlersRegistry.installRequestHandler(i, opcodeHandler);
    }
}
