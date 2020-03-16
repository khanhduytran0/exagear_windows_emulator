package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GeometryHelpers;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import java.util.List;

public class GestureStateCheckShortZoom extends AbstractGestureFSMState {
    public static FSMEvent ZOOM_LONG = new FSMEvent() {
    };
    public static FSMEvent ZOOM_SHORT = new FSMEvent() {
    };
    private static final int zoomActivateThresold = 400;

    public void notifyBecomeInactive() {
    }

    private static float getDistanceBetweenFingers(List<Finger> list) {
        Assert.state(list.size() == 2);
        Finger finger = (Finger) list.get(0);
        Finger finger2 = (Finger) list.get(1);
        return GeometryHelpers.distance(finger.getX(), finger.getY(), finger2.getX(), finger2.getY());
    }

    public GestureStateCheckShortZoom(GestureContext gestureContext) {
        super(gestureContext);
    }

    public void notifyBecomeActive() {
        List fingers = getContext().getFingers();
        if (getContext().getZoomController().isZoomed() || getDistanceBetweenFingers(fingers) < 400.0f) {
            sendEvent(ZOOM_SHORT);
        } else {
            sendEvent(ZOOM_LONG);
        }
    }
}
