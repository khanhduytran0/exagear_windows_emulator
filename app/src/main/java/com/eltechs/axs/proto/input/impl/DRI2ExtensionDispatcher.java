package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.proto.input.TrivialExtensionDispatcher;

public class DRI2ExtensionDispatcher extends TrivialExtensionDispatcher {
    public DRI2ExtensionDispatcher() {
        super((byte) -103, "DRI2", (byte) 65, (byte) 0);
    }
}
