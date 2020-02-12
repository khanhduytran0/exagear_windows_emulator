package com.eltechs.axs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ButtonPressOnlyEventAdapter implements TouchEventAdapter {
    private final Collection<Finger> activeFingers = new ArrayList();
    private final Collection<ButtonEventListener> buttonEventListeners = new ArrayList();

    public void notifyMoved(Finger finger, List<Finger> list) {
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
    }

    public void addListener(ButtonEventListener... buttonEventListenerArr) {
        for (ButtonEventListener add : buttonEventListenerArr) {
            this.buttonEventListeners.add(add);
        }
    }

    public void removeLister(ButtonEventListener... buttonEventListenerArr) {
        for (ButtonEventListener remove : buttonEventListenerArr) {
            this.buttonEventListeners.remove(remove);
        }
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
        if (this.activeFingers.isEmpty()) {
            for (ButtonEventListener pressed : this.buttonEventListeners) {
                pressed.pressed();
            }
        }
        this.activeFingers.add(finger);
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        this.activeFingers.remove(finger);
        if (this.activeFingers.isEmpty()) {
            for (ButtonEventListener released : this.buttonEventListeners) {
                released.released();
            }
        }
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        notifyReleased(finger, list);
    }
}
