package com.eltechs.axs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ButtonTapAdapter implements TouchEventAdapter {
    private Finger activeFinger = null;
    private final Collection<ButtonTapEventListener> listeners = new ArrayList();

    public void notifyMoved(Finger finger, List<Finger> list) {
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
    }

    public void addListener(ButtonTapEventListener buttonTapEventListener) {
        this.listeners.add(buttonTapEventListener);
    }

    public void removeListener(ButtonTapEventListener buttonTapEventListener) {
        this.listeners.remove(buttonTapEventListener);
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
        if (this.activeFinger == null) {
            this.activeFinger = finger;
        }
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        if (finger == this.activeFinger) {
            for (ButtonTapEventListener tapped : this.listeners) {
                tapped.tapped();
            }
        }
        this.activeFinger = null;
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        if (finger == this.activeFinger) {
            this.activeFinger = null;
        }
    }
}
