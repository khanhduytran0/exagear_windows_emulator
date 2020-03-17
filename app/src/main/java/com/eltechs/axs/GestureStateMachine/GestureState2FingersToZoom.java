package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GeometryHelpers;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.InfiniteTimer;
import com.eltechs.axs.widgets.viewOfXServer.XZoomController;
import java.util.List;

public class GestureState2FingersToZoom extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent FINGER_MOVED_IN = new FSMEvent() {
    };
    public static FSMEvent FINGER_MOVED_OUT = new FSMEvent() {
    };
    public static FSMEvent FINGER_RELEASED = new FSMEvent() {
    };
    public static FSMEvent FINGER_TOUCHED = new FSMEvent() {
    };
    private static final int timerPeriodMs = 40;
    private float distance;
    private Finger mainFinger;
    private InfiniteTimer timer;

    public void notifyMoved(Finger finger, List<Finger> list) {
    }

    public GestureState2FingersToZoom(GestureContext gestureContext) {
        super(gestureContext);
    }

    public void notifyBecomeActive() {
        getContext().getFingerEventsSource().addListener(this);
        this.timer = new InfiniteTimer(40) {
            public void onTick(long j) {
                if (GestureState2FingersToZoom.this.getContext().getMachine().isActiveState(GestureState2FingersToZoom.this)) {
                    GestureState2FingersToZoom.this.notifyTimer();
                }
            }
        };
        this.timer.start();
        List fingers = getContext().getFingers();
        Assert.state(fingers.size() == 2);
        this.distance = getDistanceBetweenFingers(fingers);
        this.mainFinger = (Finger) fingers.get(0);
        getContext().getZoomController().setAnchorBoth(this.mainFinger.getXWhenFingerCountLastChanged(), this.mainFinger.getYWhenFingerCountLastChanged());
    }

    public void notifyBecomeInactive() {
        this.distance = 0.0f;
        this.mainFinger = null;
        getContext().getFingerEventsSource().removeListener(this);
        this.timer.cancel();
    }

    /* access modifiers changed from: private */
    public void notifyTimer() {
        List fingers = getContext().getFingers();
        Assert.state(fingers.size() == 2);
        XZoomController zoomController = getContext().getZoomController();
        float distanceBetweenFingers = getDistanceBetweenFingers(fingers);
        double d = (double) (distanceBetweenFingers / this.distance);
        this.distance = distanceBetweenFingers;
        boolean isZoomed = zoomController.isZoomed();
        zoomController.insertZoomFactorChange(d);
        zoomController.refreshZoom();
        if (isZoomed != zoomController.isZoomed()) {
            zoomController.setAnchorBoth(this.mainFinger.getX(), this.mainFinger.getY());
        } else {
            zoomController.setAnchorHost(this.mainFinger.getX(), this.mainFinger.getY());
        }
        zoomController.refreshZoom();
    }

    private float getDistanceBetweenFingers(List<Finger> list) {
        Assert.state(list.size() == 2);
        Finger finger = (Finger) list.get(0);
        Finger finger2 = (Finger) list.get(1);
        return GeometryHelpers.distance(finger.getX(), finger.getY(), finger2.getX(), finger2.getY());
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
