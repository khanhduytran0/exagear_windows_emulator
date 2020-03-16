package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.OneShotTimer;
import java.util.List;

public class GestureStateWaitFingersNumberChangeWithTimeout extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent FINGER_MOVED_IN = new FSMEvent() {
    };
    public static FSMEvent FINGER_MOVED_OUT = new FSMEvent() {
    };
    public static FSMEvent FINGER_RELEASED = new FSMEvent() {
    };
    public static FSMEvent FINGER_TOUCHED = new FSMEvent() {
    };
    public static FSMEvent TIMED_OUT = new FSMEvent() {
    };
    private final int timeoutMs;
    private OneShotTimer timer;

    public void notifyMoved(Finger finger, List<Finger> list) {
    }

    public GestureStateWaitFingersNumberChangeWithTimeout(GestureContext gestureContext, int i) {
        super(gestureContext);
        this.timeoutMs = i;
    }

    public void notifyBecomeActive() {
        getContext().getFingerEventsSource().addListener(this);
        this.timer = new OneShotTimer((long) this.timeoutMs) {
            public void onFinish() {
                if (GestureStateWaitFingersNumberChangeWithTimeout.this.getContext().getMachine().isActiveState(GestureStateWaitFingersNumberChangeWithTimeout.this)) {
                    GestureStateWaitFingersNumberChangeWithTimeout.this.notifyTimeout();
                }
            }
        };
        this.timer.start();
    }

    public void notifyBecomeInactive() {
        getContext().getFingerEventsSource().removeListener(this);
        this.timer.cancel();
    }

    /* access modifiers changed from: private */
    public void notifyTimeout() {
        sendEvent(TIMED_OUT);
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
        sendEvent(FINGER_TOUCHED);
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        sendEvent(FINGER_RELEASED);
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
        sendEvent(FINGER_MOVED_IN);
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        sendEvent(FINGER_MOVED_OUT);
    }
}
