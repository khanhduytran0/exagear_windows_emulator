package com.eltechs.axs.xserver;

public interface ColormapsManager {
    void addColormapLifecycleListener(ColormapLifecycleListener colormapLifecycleListener);

    Colormap createColormap(int i);

    void freeColormap(Colormap colormap);

    Colormap getColormap(int i);

    void removeColormapLifecycleListener(ColormapLifecycleListener colormapLifecycleListener);
}
