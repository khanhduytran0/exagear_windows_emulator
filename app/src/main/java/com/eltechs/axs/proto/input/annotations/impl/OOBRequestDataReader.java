package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;

public class OOBRequestDataReader implements RequestDataReader {
    public static final RequestDataReader INSTANCE = new OOBRequestDataReader();

    private OOBRequestDataReader() {
    }

    public byte readByte(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError {
        return requestDataRetrievalContext.oobParameter;
    }

    public short readShort(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError {
        Assert.state(false, "Attempted to read more than 1 byte of OOB request data.");
        return -1;
    }

    public int readInt(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError {
        Assert.state(false, "Attempted to read more than 1 byte of OOB request data.");
        return -1;
    }

    public byte[] read(RequestDataRetrievalContext requestDataRetrievalContext, int i) throws XProtocolError {
        if (i == 1) {
            return new byte[]{requestDataRetrievalContext.oobParameter};
        }
        Assert.state(false, "Attempted to read more than 1 byte of OOB request data.");
        return null;
    }

    public void skip(RequestDataRetrievalContext requestDataRetrievalContext, int i) throws XProtocolError {
        Assert.state(false, "Attempted to read more than 1 byte of OOB request data.");
    }
}
