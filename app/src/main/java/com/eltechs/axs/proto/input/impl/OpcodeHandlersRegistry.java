package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.OpcodeHandler;

public class OpcodeHandlersRegistry {
    private OpcodeHandler[] opcodeHandlers = new OpcodeHandler[0];

    public void installRequestHandler(int i, OpcodeHandler opcodeHandler) {
        if (this.opcodeHandlers.length <= i) {
            OpcodeHandler[] opcodeHandlerArr = new OpcodeHandler[(i + 1)];
            System.arraycopy(this.opcodeHandlers, 0, opcodeHandlerArr, 0, this.opcodeHandlers.length);
            this.opcodeHandlers = opcodeHandlerArr;
        }
        Assert.state(this.opcodeHandlers[i] == null, String.format("A handler for the opcode %d is already registered.", new Object[]{Integer.valueOf(i)}));
        this.opcodeHandlers[i] = opcodeHandler;
    }

    public OpcodeHandler getHandler(int i) {
        if (i < this.opcodeHandlers.length) {
            return this.opcodeHandlers[i];
        }
        return null;
    }
}
