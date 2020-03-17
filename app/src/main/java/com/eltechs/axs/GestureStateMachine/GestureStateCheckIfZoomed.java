package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.finiteStateMachine.FSMEvent;

public class GestureStateCheckIfZoomed extends AbstractGestureFSMState {
    public static FSMEvent ZOOM_OFF = new FSMEvent() {
    };
    public static FSMEvent ZOOM_ON = new FSMEvent() {
    };

    public void notifyBecomeInactive() {
    }

    public GestureStateCheckIfZoomed(GestureContext gestureContext) {
        super(gestureContext);
    }

    public void notifyBecomeActive() {
        sendEvent(getContext().getZoomController().isZoomed() ? ZOOM_ON : ZOOM_OFF);
    }
}
