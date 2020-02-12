package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadAccess extends XProtocolError {
    public BadAccess() {
        super((byte) 10, 0);
    }
}
