package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadCursor extends XProtocolError {
    public BadCursor(int i) {
        super((byte) 6, i, String.format("Bad cursor id %d.", new Object[]{Integer.valueOf(i)}));
    }

    public int getId() {
        return getData();
    }
}
