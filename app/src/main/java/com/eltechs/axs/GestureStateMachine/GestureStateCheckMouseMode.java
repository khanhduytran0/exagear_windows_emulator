package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.GestureStateMachine.GestureMouseMode.MouseModeState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;

public class GestureStateCheckMouseMode extends AbstractGestureFSMState {
    public static FSMEvent MOUSE_MODE_LEFT = new FSMEvent() {
    };
    public static FSMEvent MOUSE_MODE_RIGHT = new FSMEvent() {
    };
    private final GestureMouseMode mouseMode;

    public void notifyBecomeInactive() {
    }

    public GestureStateCheckMouseMode(GestureContext gestureContext, GestureMouseMode gestureMouseMode) {
        super(gestureContext);
        this.mouseMode = gestureMouseMode;
    }

    public void notifyBecomeActive() {
        if (this.mouseMode.getState() == MouseModeState.MOUSE_MODE_RIGHT) {
            sendEvent(MOUSE_MODE_RIGHT);
        } else {
            sendEvent(MOUSE_MODE_LEFT);
        }
    }
}
