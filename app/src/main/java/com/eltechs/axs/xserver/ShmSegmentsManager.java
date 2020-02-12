package com.eltechs.axs.xserver;

import com.eltechs.axs.proto.input.errors.BadAccess;

public interface ShmSegmentsManager {
    void addShmSegmentLifecycleListener(ShmSegmentLifecycleListener shmSegmentLifecycleListener);

    ShmSegment attachSegment(int i, int i2, boolean z) throws BadAccess;

    void detachSegment(ShmSegment shmSegment);

    ShmSegment getSegment(int i);

    void removeShmSegmentLifecycleListener(ShmSegmentLifecycleListener shmSegmentLifecycleListener);
}
