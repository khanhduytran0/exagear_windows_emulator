package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.impl.ProtoHelpers;
import java.nio.charset.Charset;

public class String8ParameterReader extends ParameterReaderBase {
    private static final Charset LATIN1_CHARSET = Charset.forName("latin1");
    private final int lengthParameterIdx;

    public String8ParameterReader(RequestDataReader requestDataReader, int i) {
        super(requestDataReader);
        this.lengthParameterIdx = i;
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int intValue = ((Number) parametersCollectionContext.getCollectedParameter(this.lengthParameterIdx)).intValue();
        byte[] read = this.dataReader.read(parametersCollectionContext.getDataRetrievalContext(), intValue);
        this.dataReader.skip(parametersCollectionContext.getDataRetrievalContext(), ProtoHelpers.calculatePad(intValue));
        return new String(read, LATIN1_CHARSET);
    }
}
