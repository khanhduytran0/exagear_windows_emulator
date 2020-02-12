package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.proto.input.ExtensionRequestHandler;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.errors.BadRequest;
import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;

public class BigReqExtensionHandler implements ExtensionRequestHandler {
    private static final int REQUEST_SIZE_LIMIT_HINT = 16777212;

    public byte getAssignedMajorOpcode() {
        return -113;
    }

    public byte getFirstAssignedErrorId() {
        return 0;
    }

    public byte getFirstAssignedEventId() {
        return 0;
    }

    public String getName() {
        return "BIG-REQUESTS";
    }

    public void handleRequest(XClient xClient, byte b, byte b2, int i, XRequest xRequest, XResponse xResponse) throws XProtocolError, IOException {
        xRequest.setMinorOpcode((short) ArithHelpers.extendAsUnsigned(b2));
        if (b2 != 0) {
            throw new BadRequest();
        }
        xResponse.sendSimpleSuccessReply((byte) 0, Integer.valueOf(4194303), Short.valueOf((short) 0));
    }
}
