package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import com.eltechs.axs.xserver.XServer;

public class RequestStreamParser {
    private final ParameterReader[] parameterReaders;

    RequestStreamParser(ParameterReader[] parameterReaderArr) {
        this.parameterReaders = parameterReaderArr;
    }

    public Object[] parse(XServer xServer, Object obj, RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError {
        ParametersCollectionContext parametersCollectionContext = new ParametersCollectionContext(obj, xServer, requestDataRetrievalContext, this.parameterReaders.length);
        for (ParameterReader readParameter : this.parameterReaders) {
            readParameter.readParameter(parametersCollectionContext);
        }
        return parametersCollectionContext.getCollectedParameters();
    }
}
