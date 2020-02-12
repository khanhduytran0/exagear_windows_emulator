package com.eltechs.axs.xserver;

import java.nio.MappedByteBuffer;

public interface ShmSegment {
    MappedByteBuffer getContent();

    int getXid();

    boolean isWritable();
}
