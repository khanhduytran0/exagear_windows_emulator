package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadPixmap;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.XServer;

public class PixmapParameterReader extends ReferenceToObjectParameterReader {
    public PixmapParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor);
    }

    /* access modifiers changed from: protected */
    public Object getReference(XServer xServer, int i) throws XProtocolError {
        Pixmap pixmap = xServer.getPixmapsManager().getPixmap(i);
        if (pixmap != null) {
            return pixmap;
        }
        throw new BadPixmap(i);
    }
}
