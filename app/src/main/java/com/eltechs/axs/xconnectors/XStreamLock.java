package com.eltechs.axs.xconnectors;

import java.io.IOException;

public interface XStreamLock extends AutoCloseable {
    void close() throws IOException;
}
