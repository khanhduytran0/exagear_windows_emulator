package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadAtom extends XProtocolError {
    public BadAtom(int i) {
        super((byte) 5, i, String.format("Bad atom id %d.", new Object[]{Integer.valueOf(i)}));
    }

    public int getId() {
        return getErrorCode();
    }
}
