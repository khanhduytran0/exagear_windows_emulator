package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.proto.input.errors.BadAccess;
import com.eltechs.axs.sysvipc.SHMEngine;
import com.eltechs.axs.xserver.ShmSegment;
import com.eltechs.axs.xserver.ShmSegmentLifecycleListener;
import com.eltechs.axs.xserver.ShmSegmentLifecycleListenerList;
import com.eltechs.axs.xserver.ShmSegmentsManager;
import java.util.HashMap;
import java.util.Map;

public class ShmSegmentsManagerImpl implements ShmSegmentsManager {
    private final Map<Integer, ShmSegment> segments = new HashMap();
    private final SHMEngine shmEngine;
    private final ShmSegmentLifecycleListenerList shmSegmentLifecycleListenerList;

    public ShmSegmentsManagerImpl(SHMEngine sHMEngine) {
        this.shmEngine = sHMEngine;
        this.shmSegmentLifecycleListenerList = new ShmSegmentLifecycleListenerList();
    }

    public SHMEngine getShmEngine() {
        return this.shmEngine;
    }

    public ShmSegment attachSegment(int i, int i2, boolean z) throws BadAccess {
        if (this.segments.containsKey(Integer.valueOf(i))) {
            detachSegment((ShmSegment) this.segments.get(Integer.valueOf(i)));
        }
        ShmSegmentImpl shmSegmentImpl = new ShmSegmentImpl(this.shmEngine, i, i2, z);
        this.segments.put(Integer.valueOf(i), shmSegmentImpl);
        this.shmSegmentLifecycleListenerList.sendSegmentAttached(shmSegmentImpl);
        return shmSegmentImpl;
    }

    public void detachSegment(ShmSegment shmSegment) {
        ((ShmSegmentImpl) shmSegment).detach();
        this.segments.remove(Integer.valueOf(shmSegment.getXid()));
        this.shmSegmentLifecycleListenerList.sendSegmentDetached(shmSegment);
    }

    public ShmSegment getSegment(int i) {
        return (ShmSegment) this.segments.get(Integer.valueOf(i));
    }

    public void addShmSegmentLifecycleListener(ShmSegmentLifecycleListener shmSegmentLifecycleListener) {
        this.shmSegmentLifecycleListenerList.addListener(shmSegmentLifecycleListener);
    }

    public void removeShmSegmentLifecycleListener(ShmSegmentLifecycleListener shmSegmentLifecycleListener) {
        this.shmSegmentLifecycleListenerList.removeListener(shmSegmentLifecycleListener);
    }
}
