package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataRetrievalContext;

public class RemainingRequestDataAsByteBufferParameterReader extends ParameterReaderBase {
    public RemainingRequestDataAsByteBufferParameterReader() {
        super(null);
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        RequestDataRetrievalContext dataRetrievalContext = parametersCollectionContext.getDataRetrievalContext();
        int i = dataRetrievalContext.remainingBytesCount;
        dataRetrievalContext.remainingBytesCount = 0;
        return dataRetrievalContext.req.readAsByteBuffer(i);
    }
}
