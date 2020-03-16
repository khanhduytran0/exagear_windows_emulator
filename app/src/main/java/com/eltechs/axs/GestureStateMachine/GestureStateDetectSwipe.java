package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;

public class GestureStateDetectSwipe extends AbstractGestureFSMState {
    public static FSMEvent NOT_SWIPE = new FSMEvent() {
    };
    public static FSMEvent SWIPE_DOWN = new FSMEvent() {
    };
    public static FSMEvent SWIPE_LEFT = new FSMEvent() {
    };
    public static FSMEvent SWIPE_RIGHT = new FSMEvent() {
    };
    public static FSMEvent SWIPE_UP = new FSMEvent() {
    };
    private final double coordsRatioThreshold;

    public void notifyBecomeInactive() {
    }

    public GestureStateDetectSwipe(GestureContext gestureContext, double d) {
        super(gestureContext);
        this.coordsRatioThreshold = d;
    }

    public void notifyBecomeActive() {
        boolean z = true;
        if (getContext().getFingers().size() != 1) {
            z = false;
        }
        Assert.state(z);
        Finger finger = (Finger) getContext().getFingers().get(0);
        float x = finger.getX() - finger.getXWhenFirstTouched();
        float y = finger.getY() - finger.getYWhenFirstTouched();
        if (((double) Math.abs(x)) > ((double) Math.abs(y)) * this.coordsRatioThreshold) {
            sendEvent(x > 0.0f ? SWIPE_RIGHT : SWIPE_LEFT);
        } else if (((double) Math.abs(y)) > ((double) Math.abs(x)) * this.coordsRatioThreshold) {
            sendEvent(y > 0.0f ? SWIPE_DOWN : SWIPE_UP);
        } else {
            sendEvent(NOT_SWIPE);
        }
    }
}
