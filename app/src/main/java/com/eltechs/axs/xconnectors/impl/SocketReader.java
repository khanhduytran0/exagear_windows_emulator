package com.eltechs.axs.xconnectors.impl;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface SocketReader {
    int read(ByteBuffer byteBuffer) throws IOException;
}
