package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Signed;
import com.eltechs.axs.proto.input.annotations.Unsigned;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataRetrievalContext;

public abstract class PrimitiveTypeParameterReader extends ParameterReaderBase {
    private final boolean isZXT;
    private final int naturalWidth;
    private final int width;

    protected PrimitiveTypeParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor, int i, boolean z) {
        super(requestDataReader);
        this.naturalWidth = i;
        Width width2 = (Width) parameterDescriptor.getAnnotation(Width.class);
        if (width2 == null) {
            this.width = i;
        } else {
            this.width = width2.value();
        }
        
        Signed signed = (Signed) parameterDescriptor.getAnnotation(Signed.class);
        Unsigned unsigned = (Unsigned) parameterDescriptor.getAnnotation(Unsigned.class);
        boolean z2 = false;
        boolean z3 = width2 != null && i > width2.value();
        Assert.isTrue(z || !z3 || (signed == null && unsigned != null) || (signed != null && unsigned == null), "Primitive type with extension must be specified with extension type and extension type must be specified only once.");
        this.isZXT = z || (z3 && unsigned != null);
        Assert.isTrue(this.naturalWidth == 1 || this.naturalWidth == 2 || this.naturalWidth == 4, "Primitive types can only be 1, 2 or 4 bytes wide.");
        if (this.width == 1 || this.width == 2 || this.width == 4) {
            z2 = true;
        }
        Assert.isTrue(z2, "Primitive types can only be 1, 2 or 4 bytes wide.");
    }

    /* access modifiers changed from: protected */
    public final int getUnderlyingValue(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int i;
        RequestDataRetrievalContext dataRetrievalContext = parametersCollectionContext.getDataRetrievalContext();
        if (this.width > this.naturalWidth) {
            if (this.naturalWidth == 1) {
                i = ArithHelpers.extendAsUnsigned(this.dataReader.readByte(dataRetrievalContext));
            } else if (this.naturalWidth == 2) {
                i = ArithHelpers.extendAsUnsigned(this.dataReader.readShort(dataRetrievalContext));
            } else {
                i = this.dataReader.readInt(dataRetrievalContext);
            }
            this.dataReader.skip(dataRetrievalContext, this.width - this.naturalWidth);
            return i;
        } else if (this.width == 1) {
            byte readByte = this.dataReader.readByte(dataRetrievalContext);
            if (this.isZXT) {
                return ArithHelpers.extendAsUnsigned(readByte);
            }
            return readByte;
        } else if (this.width != 2) {
            return this.dataReader.readInt(dataRetrievalContext);
        } else {
            short readShort = this.dataReader.readShort(dataRetrievalContext);
            if (this.isZXT) {
                return ArithHelpers.extendAsUnsigned(readShort);
            }
            return readShort;
        }
    }
}
