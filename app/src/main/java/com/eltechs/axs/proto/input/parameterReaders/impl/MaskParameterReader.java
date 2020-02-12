package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.ConfigurationContext;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadRequest;
import com.eltechs.axs.proto.input.impl.XRequestParameterReaderFactories;
import com.eltechs.axs.xserver.impl.masks.FlagsEnum;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class MaskParameterReader extends PrimitiveTypeParameterReader {
    private final Class<? extends FlagsEnum> flagsClass;

    public MaskParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
        super(requestDataReader, parameterDescriptor, 4, true);
        this.flagsClass = (Class<? extends FlagsEnum>) XRequestParameterReaderFactories.getFlagsClass(parameterDescriptor.getType());
        Assert.notNull(this.flagsClass, String.format("Parameter %d of the request handler method %s has type Mask; Mask must be parametrised with the type of the underlying flags enum.", new Object[]{Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        Mask create = Mask.create(this.flagsClass, getUnderlyingValue(parametersCollectionContext));
        if (create != null) {
            return create;
        }
        throw new BadRequest();
    }
}
