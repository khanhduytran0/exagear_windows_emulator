package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.Colormap;
import com.eltechs.axs.xserver.ColormapLifecycleListener;
import com.eltechs.axs.xserver.ColormapLifecycleListenerList;
import com.eltechs.axs.xserver.ColormapsManager;
import java.util.HashMap;
import java.util.Map;

public class ColormapsManagerImpl implements ColormapsManager {
    private final ColormapLifecycleListenerList colormapLifecycleListenerList = new ColormapLifecycleListenerList();
    private final Map<Integer, Colormap> colormaps = new HashMap();

    public Colormap createColormap(int i) {
        if (this.colormaps.containsKey(Integer.valueOf(i))) {
            return null;
        }
        ColormapImpl colormapImpl = new ColormapImpl(i);
        this.colormaps.put(Integer.valueOf(i), colormapImpl);
        this.colormapLifecycleListenerList.sendColormapCreated(colormapImpl);
        return colormapImpl;
    }

    public Colormap getColormap(int i) {
        return (Colormap) this.colormaps.get(Integer.valueOf(i));
    }

    public void freeColormap(Colormap colormap) {
        this.colormaps.remove(Integer.valueOf(colormap.getId()));
        this.colormapLifecycleListenerList.sendColormapFreed(colormap);
    }

    public void addColormapLifecycleListener(ColormapLifecycleListener colormapLifecycleListener) {
        this.colormapLifecycleListenerList.addListener(colormapLifecycleListener);
    }

    public void removeColormapLifecycleListener(ColormapLifecycleListener colormapLifecycleListener) {
        this.colormapLifecycleListenerList.removeListener(colormapLifecycleListener);
    }
}
