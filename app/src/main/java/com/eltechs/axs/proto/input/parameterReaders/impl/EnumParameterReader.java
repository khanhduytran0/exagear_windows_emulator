package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadValue;

public class EnumParameterReader extends PrimitiveTypeParameterReader {
    private final Class<Enum<?>> enumClass;

    public EnumParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor, 1, true);
        Assert.isTrue(Enum.class.isAssignableFrom(parameterDescriptor.getRawType()));
        this.enumClass = (Class) parameterDescriptor.getRawType();
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int underlyingValue = getUnderlyingValue(parametersCollectionContext);
        Enum[] enumArr = (Enum[]) this.enumClass.getEnumConstants();
        if (underlyingValue >= 0 && underlyingValue < enumArr.length) {
            return enumArr[underlyingValue];
        }
        throw new BadValue(underlyingValue);
    }
}
