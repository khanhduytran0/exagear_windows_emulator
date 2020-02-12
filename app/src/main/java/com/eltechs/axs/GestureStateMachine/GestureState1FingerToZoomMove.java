package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.InfiniteTimer;
import java.util.List;

public class GestureState1FingerToZoomMove extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent FINGER_MOVED_IN = new FSMEvent() {
    };
    public static FSMEvent FINGER_MOVED_OUT = new FSMEvent() {
    };
    public static FSMEvent FINGER_RELEASED = new FSMEvent() {
    };
    public static FSMEvent FINGER_TOUCHED = new FSMEvent() {
    };
    private static final int timerPeriodMs = 40;
    private InfiniteTimer timer;

    public void notifyMoved(Finger finger, List<Finger> list) {
    }

    public GestureState1FingerToZoomMove(GestureContext gestureContext) {
        super(gestureContext);
    }

    public void notifyBecomeActive() {
        getContext().getFingerEventsSource().addListener(this);
        this.timer = new InfiniteTimer(40) {
            public void onTick(long j) {
                if (GestureState1FingerToZoomMove.this.getContext().getMachine().isActiveState(GestureState1FingerToZoomMove.this)) {
                    GestureState1FingerToZoomMove.this.notifyTimer();
                }
            }
        };
        this.timer.start();
        boolean z = true;
        if (getContext().getFingers().size() != 1) {
            z = false;
        }
        Assert.state(z);
        Finger finger = (Finger) getContext().getFingers().get(0);
        getContext().getZoomController().setAnchorBoth(finger.getXWhenFingerCountLastChanged(), finger.getYWhenFingerCountLastChanged());
    }

    public void notifyBecomeInactive() {
        getContext().getFingerEventsSource().removeListener(this);
        this.timer.cancel();
    }

    /* access modifiers changed from: private */
    public void notifyTimer() {
        List fingers = getContext().getFingers();
        boolean z = true;
        if (fingers.size() != 1) {
            z = false;
        }
        Assert.state(z);
        Finger finger = (Finger) fingers.get(0);
        getContext().getZoomController().setAnchorHost(finger.getX(), finger.getY());
        getContext().getZoomController().refreshZoom();
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
        sendEvent(FINGER_TOUCHED);
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
        sendEvent(FINGER_MOVED_IN);
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        sendEvent(FINGER_RELEASED);
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        sendEvent(FINGER_MOVED_OUT);
    }
}
