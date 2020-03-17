package com.eltechs.axs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TouchEventMultiplexor implements TouchEventAdapter {
    private final Collection<TouchEventAdapter> listeners = new ArrayList();

    public void addListener(TouchEventAdapter touchEventAdapter) {
        this.listeners.add(touchEventAdapter);
    }

    public void removeListener(TouchEventAdapter touchEventAdapter) {
        this.listeners.remove(touchEventAdapter);
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
        for (TouchEventAdapter notifyTouched : this.listeners) {
            notifyTouched.notifyTouched(finger, list);
        }
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        for (TouchEventAdapter notifyReleased : this.listeners) {
            notifyReleased.notifyReleased(finger, list);
        }
    }

    public void notifyMoved(Finger finger, List<Finger> list) {
        for (TouchEventAdapter notifyMoved : this.listeners) {
            notifyMoved.notifyMoved(finger, list);
        }
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
        for (TouchEventAdapter notifyMovedIn : this.listeners) {
            notifyMovedIn.notifyMovedIn(finger, list);
        }
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        for (TouchEventAdapter notifyMovedOut : this.listeners) {
            notifyMovedOut.notifyMovedOut(finger, list);
        }
    }
}
