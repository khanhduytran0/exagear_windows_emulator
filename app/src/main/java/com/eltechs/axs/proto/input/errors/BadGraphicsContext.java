package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadGraphicsContext extends XProtocolError {
    public BadGraphicsContext(int i) {
        super(CoreErrorCodes.GCONTEXT, i, String.format("Bad graphics context %d.", new Object[]{Integer.valueOf(i)}));
    }

    public int getId() {
        return getErrorCode();
    }
}
