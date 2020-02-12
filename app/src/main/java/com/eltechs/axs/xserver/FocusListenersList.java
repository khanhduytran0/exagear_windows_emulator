package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class FocusListenersList {
    private final Collection<FocusListener> listeners = new ArrayList();

    public void addListener(FocusListener focusListener) {
        this.listeners.add(focusListener);
    }

    public void removeListener(FocusListener focusListener) {
        this.listeners.remove(focusListener);
    }

    public void sendFocusChanged(Window window, Window window2) {
        for (FocusListener focusChanged : this.listeners) {
            focusChanged.focusChanged(window, window2);
        }
    }
}
