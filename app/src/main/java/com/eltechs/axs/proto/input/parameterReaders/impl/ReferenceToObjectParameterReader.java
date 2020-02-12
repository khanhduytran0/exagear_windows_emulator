package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.xserver.XServer;

public abstract class ReferenceToObjectParameterReader extends ParameterReaderBase {
    private final boolean nullable;
    private final int specialNullValue;

    /* access modifiers changed from: protected */
    public abstract Object getReference(XServer xServer, int i) throws XProtocolError;

    public ReferenceToObjectParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader);
        SpecialNullValue specialNullValue2 = (SpecialNullValue) parameterDescriptor.getAnnotation(SpecialNullValue.class);
        if (specialNullValue2 != null) {
            this.nullable = true;
            this.specialNullValue = specialNullValue2.value();
            return;
        }
        this.nullable = false;
        this.specialNullValue = -1;
    }

    /* access modifiers changed from: protected */
    public final Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int readInt = this.dataReader.readInt(parametersCollectionContext.getDataRetrievalContext());
        if (!this.nullable || readInt != this.specialNullValue) {
            return getReference(parametersCollectionContext.getXServer(), readInt);
        }
        return null;
    }
}
