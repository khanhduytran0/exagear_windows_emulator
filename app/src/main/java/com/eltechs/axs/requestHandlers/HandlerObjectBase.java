package com.eltechs.axs.requestHandlers;

import com.eltechs.axs.proto.input.annotations.BoundToXServer;
import com.eltechs.axs.xserver.XServer;

public abstract class HandlerObjectBase implements BoundToXServer {
    /* access modifiers changed from: protected */
    public final XServer xServer;

    protected HandlerObjectBase(XServer xServer2) {
        this.xServer = xServer2;
    }

    public final XServer getXServer() {
        return this.xServer;
    }
}
