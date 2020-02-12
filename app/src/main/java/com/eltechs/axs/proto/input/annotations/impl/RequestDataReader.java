package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.proto.input.XProtocolError;

public interface RequestDataReader {
    byte[] read(RequestDataRetrievalContext requestDataRetrievalContext, int i) throws XProtocolError;

    byte readByte(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError;

    int readInt(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError;

    short readShort(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError;

    void skip(RequestDataRetrievalContext requestDataRetrievalContext, int i) throws XProtocolError;
}
