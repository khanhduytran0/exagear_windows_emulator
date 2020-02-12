package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;

public class RequestDataRetrievalContext {
    public final byte oobParameter;
    public int remainingBytesCount;
    public final XRequest req;
    public final XResponse resp;

    public RequestDataRetrievalContext(XRequest xRequest, XResponse xResponse, byte b, int i) {
        this.req = xRequest;
        this.resp = xResponse;
        this.oobParameter = b;
        this.remainingBytesCount = i;
    }
}
