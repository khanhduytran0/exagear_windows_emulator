package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class ShmSegmentLifecycleListenerList {
    private final Collection<ShmSegmentLifecycleListener> listeners = new ArrayList();

    public void addListener(ShmSegmentLifecycleListener shmSegmentLifecycleListener) {
        this.listeners.add(shmSegmentLifecycleListener);
    }

    public void removeListener(ShmSegmentLifecycleListener shmSegmentLifecycleListener) {
        this.listeners.remove(shmSegmentLifecycleListener);
    }

    public void sendSegmentAttached(ShmSegment shmSegment) {
        for (ShmSegmentLifecycleListener segmentAttached : this.listeners) {
            segmentAttached.segmentAttached(shmSegment);
        }
    }

    public void sendSegmentDetached(ShmSegment shmSegment) {
        for (ShmSegmentLifecycleListener segmentDetached : this.listeners) {
            segmentDetached.segmentDetached(shmSegment);
        }
    }
}
