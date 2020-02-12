package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.annotations.ParamLength;
import com.eltechs.axs.proto.input.annotations.ParamName;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xserver.XServer;

public class FontManipulationRequests extends HandlerObjectBase {
    @RequestHandler(opcode = 46)
    public void CloseFont(@RequestParam int i) {
    }

    public FontManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 45)
    public void OpenFont(@RequestParam int i, @RequestParam @ParamName("nameLength") short s, @RequestParam short s2, @RequestParam @ParamLength("nameLength") String str) {
        if (!str.equals("cursor")) {
            Assert.notImplementedYet(String.format("OpenFont supports only font='cursor', but got '%s'.", new Object[]{str}));
        }
    }
}
