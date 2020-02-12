package com.eltechs.axs.sysvipc;

public interface SHMEngine {
    AttachedSHMSegment attachSegment(int i, boolean z);

    void detachSHMSegment(AttachedSHMSegment attachedSHMSegment);
}
