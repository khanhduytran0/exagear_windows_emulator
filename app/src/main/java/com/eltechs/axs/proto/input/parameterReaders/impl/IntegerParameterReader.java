package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.xserver.client.XClient;

public class IntegerParameterReader extends PrimitiveTypeParameterReader {
    private final boolean newXId;

    public IntegerParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor, 4, false);
		boolean z = false;
        if (((NewXId) parameterDescriptor.getAnnotation(NewXId.class)) != null) {
            z = true;
        }
        this.newXId = z;
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int underlyingValue = getUnderlyingValue(parametersCollectionContext);
        if (!this.newXId || ((XClient) parametersCollectionContext.getConnectionContext()).getIdInterval().isInInterval(underlyingValue)) {
            return Integer.valueOf(underlyingValue);
        }
        throw new BadIdChoice(underlyingValue);
    }
}
