package com.eltechs.axs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ButtonTouchEventAdapter implements TouchEventAdapter {
    private final Collection<ButtonEventListener> buttonEventListeners = new ArrayList();
    private int pointersInside = 0;

    public void notifyMoved(Finger finger, List<Finger> list) {
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
        if (this.pointersInside == 0) {
            for (ButtonEventListener pressed : this.buttonEventListeners) {
                pressed.pressed();
            }
        }
        this.pointersInside++;
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        this.pointersInside--;
        if (this.pointersInside == 0) {
            for (ButtonEventListener released : this.buttonEventListeners) {
                released.released();
            }
        }
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
        notifyTouched(finger, list);
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        notifyReleased(finger, list);
    }
}
