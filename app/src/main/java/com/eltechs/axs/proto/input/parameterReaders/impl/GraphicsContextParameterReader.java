package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadGraphicsContext;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.XServer;

public class GraphicsContextParameterReader extends ReferenceToObjectParameterReader {
    public GraphicsContextParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor);
    }

    /* access modifiers changed from: protected */
    public Object getReference(XServer xServer, int i) throws XProtocolError {
        GraphicsContext graphicsContext = xServer.getGraphicsContextsManager().getGraphicsContext(i);
        if (graphicsContext != null) {
            return graphicsContext;
        }
        throw new BadGraphicsContext(i);
    }
}
