package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadLength extends XProtocolError {
    public BadLength() {
        super(CoreErrorCodes.LENGTH, 0);
    }
}
