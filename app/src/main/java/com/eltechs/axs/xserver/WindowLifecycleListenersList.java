package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class WindowLifecycleListenersList {
    private final Collection<WindowLifecycleListener> listeners = new ArrayList();

    public void addListener(WindowLifecycleListener windowLifecycleListener) {
        this.listeners.add(windowLifecycleListener);
    }

    public void removeListener(WindowLifecycleListener windowLifecycleListener) {
        this.listeners.remove(windowLifecycleListener);
    }

    public void sendWindowCreated(Window window) {
        for (WindowLifecycleListener windowCreated : this.listeners) {
            windowCreated.windowCreated(window);
        }
    }

    public void sendWindowMapped(Window window) {
        for (WindowLifecycleListener windowMapped : this.listeners) {
            windowMapped.windowMapped(window);
        }
    }

    public void sendWindowUnmapped(Window window) {
        for (WindowLifecycleListener windowUnmapped : this.listeners) {
            windowUnmapped.windowUnmapped(window);
        }
    }

    public void sendWindowReparented(Window window, Window window2) {
        for (WindowLifecycleListener windowReparented : this.listeners) {
            windowReparented.windowReparented(window, window2);
        }
    }

    public void sendWindowZOrderChange(Window window) {
        for (WindowLifecycleListener windowZOrderChange : this.listeners) {
            windowZOrderChange.windowZOrderChange(window);
        }
    }

    public void sendWindowDestroyed(Window window) {
        for (WindowLifecycleListener windowDestroyed : this.listeners) {
            windowDestroyed.windowDestroyed(window);
        }
    }
}
