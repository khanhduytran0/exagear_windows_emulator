package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class WindowContentModificationListenersList {
    private final Collection<WindowContentModificationListener> listeners = new ArrayList();

    public void addListener(WindowContentModificationListener windowContentModificationListener) {
        this.listeners.add(windowContentModificationListener);
    }

    public void removeListener(WindowContentModificationListener windowContentModificationListener) {
        this.listeners.remove(windowContentModificationListener);
    }

    public void sendWindowContentChanged(Window window, int i, int i2, int i3, int i4) {
        for (WindowContentModificationListener contentChanged : new ArrayList(this.listeners)) {
            contentChanged.contentChanged(window, i, i2, i3, i4);
        }
    }

    public void sendFrontBufferReplaced(Window window) {
        for (WindowContentModificationListener frontBufferReplaced : new ArrayList(this.listeners)) {
            frontBufferReplaced.frontBufferReplaced(window);
        }
    }
}
