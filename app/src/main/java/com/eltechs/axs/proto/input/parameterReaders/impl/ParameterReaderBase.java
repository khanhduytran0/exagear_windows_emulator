package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;

public abstract class ParameterReaderBase implements ParameterReader {
    protected final RequestDataReader dataReader;

    /* access modifiers changed from: protected */
    public abstract Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError;

    protected ParameterReaderBase(RequestDataReader requestDataReader) {
        this.dataReader = requestDataReader;
    }

    public final void readParameter(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        parametersCollectionContext.parameterCollected(readParameterImpl(parametersCollectionContext));
    }
}
