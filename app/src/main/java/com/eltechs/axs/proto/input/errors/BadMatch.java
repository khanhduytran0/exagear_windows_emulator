package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadMatch extends XProtocolError {
    public BadMatch() {
        super((byte) 8, 0);
    }
}
