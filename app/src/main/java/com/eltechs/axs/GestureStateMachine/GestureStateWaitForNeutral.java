package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import java.util.List;

public class GestureStateWaitForNeutral extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent GESTURE_COMPLETED = new FSMEvent() {
    };

    public void notifyMoved(Finger finger, List<Finger> list) {
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
    }

    public GestureStateWaitForNeutral(GestureContext gestureContext) {
        super(gestureContext);
    }

    public void notifyBecomeActive() {
        getContext().getFingerEventsSource().addListener(this);
        if (getContext().getFingers().isEmpty()) {
            sendEvent(GESTURE_COMPLETED);
        }
    }

    public void notifyBecomeInactive() {
        getContext().getFingerEventsSource().removeListener(this);
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        if (list.isEmpty()) {
            sendEvent(GESTURE_COMPLETED);
        }
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        notifyReleased(finger, list);
    }
}
