package com.eltechs.axs.xconnectors.impl;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface SocketWriter {
    int write(ByteBuffer byteBuffer) throws IOException;
}
