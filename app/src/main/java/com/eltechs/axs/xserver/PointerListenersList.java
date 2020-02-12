package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class PointerListenersList {
    private final Collection<PointerListener> listeners = new ArrayList();

    public void addListener(PointerListener pointerListener) {
        this.listeners.add(pointerListener);
    }

    public void removeListener(PointerListener pointerListener) {
        this.listeners.remove(pointerListener);
    }

    public void sendPointerMoved(int i, int i2) {
        for (PointerListener pointerMoved : this.listeners) {
            pointerMoved.pointerMoved(i, i2);
        }
    }

    public void sendPointerButtonPressed(int i) {
        for (PointerListener pointerButtonPressed : this.listeners) {
            pointerButtonPressed.pointerButtonPressed(i);
        }
    }

    public void sendPointerButtonReleased(int i) {
        for (PointerListener pointerButtonReleased : this.listeners) {
            pointerButtonReleased.pointerButtonReleased(i);
        }
    }

    public void sendPointerWarped(int i, int i2) {
        for (PointerListener pointerWarped : this.listeners) {
            pointerWarped.pointerWarped(i, i2);
        }
    }
}
