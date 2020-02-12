package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.XServer;

public class AnnotationDrivenRequestParser {
    private final RequestStreamParser impl;

    public AnnotationDrivenRequestParser(ParameterReader[] parameterReaderArr) {
        this.impl = new RequestStreamParser(parameterReaderArr);
    }

    public Object[] getRequestHandlerParameters(Object obj, XServer xServer, XRequest xRequest, XResponse xResponse, int i, byte b) throws XProtocolError {
        return this.impl.parse(xServer, obj, new RequestDataRetrievalContext(xRequest, xResponse, b, i));
    }
}
