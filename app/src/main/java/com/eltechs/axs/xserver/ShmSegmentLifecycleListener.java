package com.eltechs.axs.xserver;

public interface ShmSegmentLifecycleListener {
    void segmentAttached(ShmSegment shmSegment);

    void segmentDetached(ShmSegment shmSegment);
}
