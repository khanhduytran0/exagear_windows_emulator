package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.EventParsersRegistry;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.AnnotationDrivenEventParserConfigurer;
import com.eltechs.axs.proto.input.annotations.impl.EventBuilders;
import com.eltechs.axs.proto.input.annotations.impl.EventParser;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadValue;

public class EventParameterReader extends ParameterReaderBase {
    private static final EventParsersRegistry parsersRegistry = AnnotationDrivenEventParserConfigurer.createParserRegistry(EventBuilders.class);

    public EventParameterReader(RequestDataReader requestDataReader) {
        super(requestDataReader);
    }

    /* access modifiers changed from: protected */
    public Object readParameterImpl(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int extendAsUnsigned = ArithHelpers.extendAsUnsigned(this.dataReader.readByte(parametersCollectionContext.getDataRetrievalContext()));
        if (extendAsUnsigned > 34 || extendAsUnsigned < 2) {
            throw new BadValue(extendAsUnsigned);
        }
        EventParser parser = parsersRegistry.getParser(extendAsUnsigned);
        Assert.isTrue(parser != null, String.format("Event parser for event code %d not found.", new Object[]{Integer.valueOf(extendAsUnsigned)}));
        return parser.parse(parametersCollectionContext);
    }
}
