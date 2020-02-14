package com.eltechs.axs.xserver.impl;

import android.util.ArrayMap;

import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.DrawablesManager;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.PixmapLifecycleListener;
import com.eltechs.axs.xserver.PixmapLifecycleListenerList;
import com.eltechs.axs.xserver.PixmapsManager;
import java.util.HashMap;
import java.util.Map;

public class PixmapsManagerImpl implements PixmapsManager {
    private final DrawablesManager drawablesManager;
    private final PixmapLifecycleListenerList pixmapLifecycleListenersList;
    private final Map<Integer, Pixmap> pixmaps = new ArrayMap<Integer, Pixmap>();

    public PixmapsManagerImpl(DrawablesManager drawablesManager2) {
        this.drawablesManager = drawablesManager2;
        this.pixmapLifecycleListenersList = new PixmapLifecycleListenerList();
    }

    public Pixmap getPixmap(int i) {
        return (Pixmap) this.pixmaps.get(Integer.valueOf(i));
    }

    public Pixmap createPixmap(Drawable drawable) {
        if (this.pixmaps.containsKey(Integer.valueOf(drawable.getId()))) {
            return null;
        }
        PixmapImpl pixmapImpl = new PixmapImpl(drawable);
        this.pixmaps.put(Integer.valueOf(drawable.getId()), pixmapImpl);
        this.pixmapLifecycleListenersList.sendPixmapCreated(pixmapImpl);
        return pixmapImpl;
    }

    public void freePixmap(Pixmap pixmap) {
        this.pixmaps.remove(Integer.valueOf(pixmap.getBackingStore().getId()));
        this.drawablesManager.removeDrawable(pixmap.getBackingStore());
        this.pixmapLifecycleListenersList.sendPixmapFreed(pixmap);
    }

    public void addPixmapLifecycleListener(PixmapLifecycleListener pixmapLifecycleListener) {
        this.pixmapLifecycleListenersList.addListener(pixmapLifecycleListener);
    }

    public void removePixmapLifecycleListener(PixmapLifecycleListener pixmapLifecycleListener) {
        this.pixmapLifecycleListenersList.removeListener(pixmapLifecycleListener);
    }
}
