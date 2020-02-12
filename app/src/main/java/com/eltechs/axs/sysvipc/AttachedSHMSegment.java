package com.eltechs.axs.sysvipc;

import java.nio.MappedByteBuffer;

public interface AttachedSHMSegment {
    MappedByteBuffer getContent();
}
