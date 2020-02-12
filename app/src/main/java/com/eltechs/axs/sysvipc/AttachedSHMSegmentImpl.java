package com.eltechs.axs.sysvipc;

import java.nio.MappedByteBuffer;

public class AttachedSHMSegmentImpl implements AttachedSHMSegment {
    MappedByteBuffer content;
    long size;

    AttachedSHMSegmentImpl(MappedByteBuffer mappedByteBuffer, long j) {
        this.content = mappedByteBuffer;
        this.size = j;
    }

    public MappedByteBuffer getContent() {
        return this.content;
    }
}
