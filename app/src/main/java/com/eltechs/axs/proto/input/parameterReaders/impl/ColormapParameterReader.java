package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadColormap;
import com.eltechs.axs.xserver.Colormap;
import com.eltechs.axs.xserver.XServer;

public class ColormapParameterReader extends ReferenceToObjectParameterReader {
    public ColormapParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor);
    }

    /* access modifiers changed from: protected */
    public Object getReference(XServer xServer, int i) throws XProtocolError {
        Colormap colormap = xServer.getColormapsManager().getColormap(i);
        if (colormap != null) {
            return colormap;
        }
        throw new BadColormap(i);
    }
}
