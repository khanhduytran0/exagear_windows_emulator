package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class PixmapLifecycleListenerList {
    private final Collection<PixmapLifecycleListener> listeners = new ArrayList();

    public void addListener(PixmapLifecycleListener pixmapLifecycleListener) {
        this.listeners.add(pixmapLifecycleListener);
    }

    public void removeListener(PixmapLifecycleListener pixmapLifecycleListener) {
        this.listeners.remove(pixmapLifecycleListener);
    }

    public void sendPixmapCreated(Pixmap pixmap) {
        for (PixmapLifecycleListener pixmapCreated : this.listeners) {
            pixmapCreated.pixmapCreated(pixmap);
        }
    }

    public void sendPixmapFreed(Pixmap pixmap) {
        for (PixmapLifecycleListener pixmapFreed : this.listeners) {
            pixmapFreed.pixmapFreed(pixmap);
        }
    }
}
