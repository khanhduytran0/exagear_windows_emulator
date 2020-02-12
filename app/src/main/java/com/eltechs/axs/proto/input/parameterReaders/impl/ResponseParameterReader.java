package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;

public class ResponseParameterReader extends ParameterReaderBase {
    public ResponseParameterReader() {
        super(null);
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        return parametersCollectionContext.getDataRetrievalContext().resp;
    }
}
