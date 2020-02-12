package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.impl.drawables.Visual;

public class VisualParameterReader extends ReferenceToObjectParameterReader {
    public VisualParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor);
    }

    /* access modifiers changed from: protected */
    public Object getReference(XServer xServer, int i) throws XProtocolError {
        Visual visual = xServer.getDrawablesManager().getVisual(i);
        if (visual != null) {
            return visual;
        }
        throw new BadMatch();
    }
}
