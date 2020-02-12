package com.eltechs.axs.xserver;

public interface ColormapLifecycleListener {
    void colormapCreated(Colormap colormap);

    void colormapFreed(Colormap colormap);
}
