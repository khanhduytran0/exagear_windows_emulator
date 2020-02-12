package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GeometryHelpers;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.OneShotTimer;
// import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.util.List;

public class GestureState1FingerMeasureSpeed extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent FINGER_FLASHED = new FSMEvent() {
    };
    public static FSMEvent FINGER_MOVED_IN = new FSMEvent() {
    };
    public static FSMEvent FINGER_MOVED_OUT = new FSMEvent() {
    };
    public static FSMEvent FINGER_STANDING = new FSMEvent() {
    };
    public static FSMEvent FINGER_TAPPED = new FSMEvent() {
    };
    public static FSMEvent FINGER_TOUCHED = new FSMEvent() {
    };
    public static FSMEvent FINGER_WALKED = new FSMEvent() {
    };
    public static FSMEvent FINGER_WALKED_AND_GONE = new FSMEvent() {
    };
    private final float aimingFingerMaxMove;
    private double distance = 0.0d;
    private final int measureTime;
    private final float standingFingerMaxMove;
    private final float tappingFingerMaxMove;
    private OneShotTimer timer;
    private long touchTime;

    public GestureState1FingerMeasureSpeed(GestureContext gestureContext, int i, float f, float f2, float f3, float f4) {
        super(gestureContext);
        this.measureTime = i;
        this.standingFingerMaxMove = f;
        this.tappingFingerMaxMove = f3;
        this.aimingFingerMaxMove = f2;
    }

    public void notifyBecomeActive() {
        boolean z = true;
        if (getContext().getFingers().size() != 1) {
            z = false;
        }
        Assert.state(z);
        getContext().getFingerEventsSource().addListener(this);
        this.timer = new OneShotTimer((long) this.measureTime) {
            public void onFinish() {
                if (GestureState1FingerMeasureSpeed.this.getContext().getMachine().isActiveState(GestureState1FingerMeasureSpeed.this)) {
                    GestureState1FingerMeasureSpeed.this.notifyTimeout();
                }
            }
        };
        this.timer.start();
        this.touchTime = System.currentTimeMillis();
        this.distance = 0d; // FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
    }

    public void notifyBecomeInactive() {
        getContext().getFingerEventsSource().removeListener(this);
        this.timer.cancel();
    }

    private void recalcDistance(Finger finger) {
        double distance2 = (double) GeometryHelpers.distance(finger.getX(), finger.getY(), finger.getXWhenFirstTouched(), finger.getYWhenFirstTouched());
        if (this.distance < distance2) {
            this.distance = distance2;
        }
    }

    /* access modifiers changed from: private */
    public void notifyTimeout() {
        boolean z = true;
        if (getContext().getFingers().size() != 1) {
            z = false;
        }
        Assert.isTrue(z);
        recalcDistance((Finger) getContext().getFingers().get(0));
        if (this.distance < ((double) this.standingFingerMaxMove)) {
            sendEvent(FINGER_STANDING);
        } else if (this.distance < ((double) this.aimingFingerMaxMove)) {
            sendEvent(FINGER_WALKED);
        } else {
            sendEvent(FINGER_FLASHED);
        }
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
        sendEvent(FINGER_TOUCHED);
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        Assert.state(getContext().getFingers().isEmpty());
        recalcDistance(finger);
        if (this.distance < ((double) this.tappingFingerMaxMove)) {
            sendEvent(FINGER_TAPPED);
        } else {
            sendEvent(FINGER_WALKED_AND_GONE);
        }
    }

    public void notifyMoved(Finger finger, List<Finger> list) {
        recalcDistance(finger);
        if (this.distance >= ((double) this.aimingFingerMaxMove)) {
            sendEvent(FINGER_FLASHED);
        }
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        sendEvent(FINGER_MOVED_OUT);
    }

    public void notifyMovedIn(Finger finger, List<Finger> list) {
        sendEvent(FINGER_MOVED_IN);
    }
}
