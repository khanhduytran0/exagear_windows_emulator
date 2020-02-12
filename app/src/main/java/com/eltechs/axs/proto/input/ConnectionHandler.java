package com.eltechs.axs.proto.input;

import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;

public interface ConnectionHandler<Context> {
    void handleConnectionShutdown(Context context);

    Context handleNewConnection(XInputStream xInputStream, XOutputStream xOutputStream);
}
