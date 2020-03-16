package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GuestAppActionAdapters.MousePointAndClickAdapter;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import java.util.List;

public class GestureState1FingerToLongClick extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent GESTURE_COMPLETED = new FSMEvent() {
    };
    private final MousePointAndClickAdapter clicker;

    public void notifyMoved(Finger finger, List<Finger> list) {
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
    }

    public GestureState1FingerToLongClick(GestureContext gestureContext, MousePointAndClickAdapter mousePointAndClickAdapter) {
        super(gestureContext);
        this.clicker = mousePointAndClickAdapter;
    }

    public void notifyBecomeActive() {
        getContext().getFingerEventsSource().addListener(this);
        boolean z = true;
        if (getContext().getFingers().size() != 1) {
            z = false;
        }
        Assert.isTrue(z);
        Finger finger = (Finger) getContext().getFingers().get(0);
        this.clicker.click(finger.getX(), finger.getY());
    }

    public void notifyBecomeInactive() {
        getContext().getFingerEventsSource().removeListener(this);
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        if (list.isEmpty()) {
            this.clicker.finalizeClick(finger.getX(), finger.getY());
            sendEvent(GESTURE_COMPLETED);
        }
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        notifyReleased(finger, list);
    }
}
