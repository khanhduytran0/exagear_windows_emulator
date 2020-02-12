package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadCursor;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.XServer;

public class CursorParameterReader extends ReferenceToObjectParameterReader {
    public CursorParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor);
    }

    /* access modifiers changed from: protected */
    public Object getReference(XServer xServer, int i) throws XProtocolError {
        Cursor cursor = xServer.getCursorsManager().getCursor(i);
        if (cursor != null) {
            return cursor;
        }
        throw new BadCursor(i);
    }
}
