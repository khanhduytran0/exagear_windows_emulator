package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.ArrayList;
import java.util.Collection;

public class WindowChangeListenersList {
    private final Collection<WindowChangeListener> listeners = new ArrayList();

    public void addListener(WindowChangeListener windowChangeListener) {
        this.listeners.add(windowChangeListener);
    }

    public void removeListener(WindowChangeListener windowChangeListener) {
        this.listeners.remove(windowChangeListener);
    }

    public void sendWindowGeometryChanged(Window window) {
        for (WindowChangeListener geometryChanged : this.listeners) {
            geometryChanged.geometryChanged(window);
        }
    }

    public void sendWindowAttributeChanged(Window window, Mask<WindowAttributeNames> mask) {
        for (WindowChangeListener attributesChanged : this.listeners) {
            attributesChanged.attributesChanged(window, mask);
        }
    }
}
