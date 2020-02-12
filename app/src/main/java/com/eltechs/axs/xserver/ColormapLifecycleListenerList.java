package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class ColormapLifecycleListenerList {
    private final Collection<ColormapLifecycleListener> listeners = new ArrayList();

    public void addListener(ColormapLifecycleListener colormapLifecycleListener) {
        this.listeners.add(colormapLifecycleListener);
    }

    public void removeListener(ColormapLifecycleListener colormapLifecycleListener) {
        this.listeners.remove(colormapLifecycleListener);
    }

    public void sendColormapCreated(Colormap colormap) {
        for (ColormapLifecycleListener colormapCreated : this.listeners) {
            colormapCreated.colormapCreated(colormap);
        }
    }

    public void sendColormapFreed(Colormap colormap) {
        for (ColormapLifecycleListener colormapFreed : this.listeners) {
            colormapFreed.colormapFreed(colormap);
        }
    }
}
