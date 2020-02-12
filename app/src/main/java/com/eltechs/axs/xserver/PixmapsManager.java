package com.eltechs.axs.xserver;

public interface PixmapsManager {
    void addPixmapLifecycleListener(PixmapLifecycleListener pixmapLifecycleListener);

    Pixmap createPixmap(Drawable drawable);

    void freePixmap(Pixmap pixmap);

    Pixmap getPixmap(int i);

    void removePixmapLifecycleListener(PixmapLifecycleListener pixmapLifecycleListener);
}
