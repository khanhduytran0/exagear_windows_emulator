package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadShmSeg;
import com.eltechs.axs.xserver.ShmSegment;
import com.eltechs.axs.xserver.XServer;

public class ShmSegmentParameterReader extends ReferenceToObjectParameterReader {
    public ShmSegmentParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor);
    }

    /* access modifiers changed from: protected */
    public Object getReference(XServer xServer, int i) throws XProtocolError {
        ShmSegment segment = xServer.getShmSegmentsManager().getSegment(i);
        if (segment != null) {
            return segment;
        }
        throw new BadShmSeg(i);
    }
}
