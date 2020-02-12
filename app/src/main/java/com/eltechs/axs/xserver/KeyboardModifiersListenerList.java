package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.ArrayList;
import java.util.Collection;

public class KeyboardModifiersListenerList {
    private final Collection<KeyboardModifiersListener> listeners = new ArrayList();

    public void addListener(KeyboardModifiersListener keyboardModifiersListener) {
        this.listeners.add(keyboardModifiersListener);
    }

    public void removeListener(KeyboardModifiersListener keyboardModifiersListener) {
        this.listeners.remove(keyboardModifiersListener);
    }

    public void sendModifiersChanged(Mask<KeyButNames> mask) {
        for (KeyboardModifiersListener modifiersChanged : this.listeners) {
            modifiersChanged.modifiersChanged(mask);
        }
    }
}
