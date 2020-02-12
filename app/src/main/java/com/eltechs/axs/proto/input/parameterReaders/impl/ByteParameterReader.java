package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;

public class ByteParameterReader extends PrimitiveTypeParameterReader {
    public ByteParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor, 1, true);
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        return Byte.valueOf((byte) getUnderlyingValue(parametersCollectionContext));
    }
}
