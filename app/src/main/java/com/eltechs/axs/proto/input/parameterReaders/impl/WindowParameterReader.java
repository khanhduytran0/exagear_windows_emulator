package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.errors.BadWindow;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;

public class WindowParameterReader extends ReferenceToObjectParameterReader {
    public WindowParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor) {
        super(requestDataReader, parameterDescriptor);
    }

    /* access modifiers changed from: protected */
    public Object getReference(XServer xServer, int i) throws XProtocolError {
        Window window = xServer.getWindowsManager().getWindow(i);
        if (window != null) {
            return window;
        }
        throw new BadWindow(i);
    }
}
