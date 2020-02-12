package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;

public class ShortParameterReader extends PrimitiveTypeParameterReader {
    public ShortParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor, 2, false);
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        return Short.valueOf((short) getUnderlyingValue(parametersCollectionContext));
    }
}
