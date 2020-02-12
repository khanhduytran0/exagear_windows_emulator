package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xserver.XServer;

public class ForceScreenSaverRequest extends HandlerObjectBase {

    public enum Mode {
        RESET,
        ACTIVATE
    }

    @RequestHandler(opcode = 115)
    public void ForceScreenSaver(@OOBParam @RequestParam Mode mode) {
    }

    public ForceScreenSaverRequest(XServer xServer) {
        super(xServer);
    }
}
