package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import java.util.List;

public class GestureStateNeutral extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent FINGER_MOVED_IN = new FSMEvent() {
    };
    public static FSMEvent FINGER_TOUCHED = new FSMEvent() {
    };

    public GestureStateNeutral(GestureContext gestureContext) {
        super(gestureContext);
    }

    public void notifyBecomeActive() {
        getContext().getFingerEventsSource().addListener(this);
        Assert.state(getContext().getFingers().isEmpty());
    }

    public void notifyBecomeInactive() {
        getContext().getFingerEventsSource().removeListener(this);
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
        sendEvent(FINGER_TOUCHED);
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        Assert.unreachable();
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        Assert.unreachable();
    }

    public void notifyMoved(Finger finger, List<Finger> list) {
        Assert.unreachable();
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
        sendEvent(FINGER_MOVED_IN);
    }
}
