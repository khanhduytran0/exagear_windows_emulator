package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.proto.input.TrivialExtensionDispatcher;

public class MITShmExtensionDispatcher extends TrivialExtensionDispatcher {
    public MITShmExtensionDispatcher() {
        super((byte) -116, "MIT-SHM", (byte) 64, Byte.MIN_VALUE);
    }
}
