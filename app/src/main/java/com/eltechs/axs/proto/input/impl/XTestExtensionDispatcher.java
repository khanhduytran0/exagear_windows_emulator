package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.proto.input.TrivialExtensionDispatcher;

public class XTestExtensionDispatcher extends TrivialExtensionDispatcher {
    public XTestExtensionDispatcher() {
        super((byte) -114, "XTEST", (byte) 0, (byte) 0);
    }
}
