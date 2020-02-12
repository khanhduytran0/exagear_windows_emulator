package com.eltechs.axs.xserver;

public interface WindowLifecycleListener {
    void windowCreated(Window window);

    void windowDestroyed(Window window);

    void windowMapped(Window window);

    void windowReparented(Window window, Window window2);

    void windowUnmapped(Window window);

    void windowZOrderChange(Window window);
}
