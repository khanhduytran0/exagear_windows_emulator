package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadWindow extends XProtocolError {
    public BadWindow(int i) {
        super((byte) 3, i, String.format("Bad window id %d.", new Object[]{Integer.valueOf(i)}));
    }

    public int getId() {
        return getErrorCode();
    }
}
