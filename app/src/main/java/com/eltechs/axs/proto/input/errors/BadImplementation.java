package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadImplementation extends XProtocolError {
    public BadImplementation() {
        super(CoreErrorCodes.IMPLEMENTATION, 0);
    }
}
