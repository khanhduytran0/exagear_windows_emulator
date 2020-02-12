package com.eltechs.axs.xserver;

public interface PixmapLifecycleListener {
    void pixmapCreated(Pixmap pixmap);

    void pixmapFreed(Pixmap pixmap);
}
