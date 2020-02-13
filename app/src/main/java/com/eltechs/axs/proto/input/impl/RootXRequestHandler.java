package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.activities.FatalErrorActivity;
import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ExtensionRequestHandler;
import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.errors.BadRequest;
import com.eltechs.axs.requestHandlers.X11ProtocolExtensionIds;
import com.eltechs.axs.xconnectors.RequestHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RootXRequestHandler implements RequestHandler<XClient> {
    private static final int SIZE_OF_INT = 4;
    private static final int X_REQUEST_PROLOGUE_LEN = 4;
    private final ExtensionRequestHandler[] extensionHandlers = new ExtensionRequestHandler[256];
    private final HandshakeHandler handshakeHandler;
    private final XServer target;

    public RootXRequestHandler(XServer xServer) {
        this.target = xServer;
        this.handshakeHandler = new HandshakeHandler(xServer);
        installExtensionHandler(X11ProtocolExtensionIds.BIGREQ, new BigReqExtensionHandler());
    }

    public ProcessingResult handleRequest(XClient xClient, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        if (xClient.isAuthenticated()) {
            return handleNormalRequest(xClient, xInputStream, xOutputStream);
        }
        return this.handshakeHandler.handleAuthRequest(xClient, xInputStream, xOutputStream);
    }

    private ProcessingResult handleNormalRequest(XClient xClient, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        int i;
        if (xInputStream.getAvailableBytesCount() < 4) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        byte b = xInputStream.getByte();
        byte b2 = xInputStream.getByte();
        int extendAsUnsigned = ArithHelpers.extendAsUnsigned(xInputStream.getShort());
        if (extendAsUnsigned != 0) {
            i = 4;
        } else if (xInputStream.getAvailableBytesCount() < 4) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        } else {
            extendAsUnsigned = xInputStream.getInt();
            i = 8;
        }
        int i2 = (extendAsUnsigned * 4) - i;
        if (i2 > xInputStream.getAvailableBytesCount()) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        XRequest xRequest = new XRequest(xClient.generateSequenceNumber(), xInputStream, i2);
        XResponse xResponse = new XResponse(xRequest, xOutputStream);
        xRequest.setMajorOpcode(b);
        dispatchRequest(b, b2, i2, xClient, xRequest, xResponse);
        Assert.state(xRequest.getRemainingBytesCount() == 0, "Request has not been parsed fully.");
        return ProcessingResult.PROCESSED;
    }

    private void dispatchRequest(byte b, byte b2, int i, XClient xClient, XRequest xRequest, XResponse xResponse) throws IOException {
        try {
			ExtensionRequestHandler extensionRequestHandler = this.extensionHandlers[b >= 0 ? 0 : ArithHelpers.extendAsUnsigned(b)];
			if (i < 0) {
				try {
					throw new BadRequest();
				} catch (XProtocolError e) {
					xRequest.skipRequest();
					xResponse.sendError(e);
				}
			} else if (extensionRequestHandler == null) {
				throw new BadRequest();
			} else {
				extensionRequestHandler.handleRequest(xClient, b, b2, i, xRequest, xResponse);
			}
		} catch (Throwable th) {
			th.printStackTrace();
			FatalErrorActivity.showFatalError(th.getMessage());
			// throw new RuntimeException(th);
		}
    }

    public void installExtensionHandler(int i, ExtensionRequestHandler extensionRequestHandler) {
        Assert.state(this.extensionHandlers[i] == null, String.format("A handler for the protocol extension %d is already installed.", new Object[]{Integer.valueOf(i)}));
        this.extensionHandlers[i] = extensionRequestHandler;
    }

    public List<ExtensionRequestHandler> getInstalledExtensionHandlers() {
        ArrayList arrayList = new ArrayList();
        int length = this.extensionHandlers.length;
        for (int i = 1; i < length; i++) {
            if (this.extensionHandlers[i] != null) {
                arrayList.add(this.extensionHandlers[i]);
            }
        }
        return arrayList;
    }
}
