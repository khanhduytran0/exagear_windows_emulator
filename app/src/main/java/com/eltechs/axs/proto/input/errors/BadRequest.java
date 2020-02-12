package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadRequest extends XProtocolError {
    public BadRequest() {
        super((byte) 1, 0);
    }
}
