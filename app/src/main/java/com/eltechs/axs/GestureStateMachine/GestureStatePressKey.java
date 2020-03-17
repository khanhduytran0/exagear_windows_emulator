package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.finiteStateMachine.FSMEvent;

public class GestureStatePressKey extends AbstractGestureFSMState {
    public static FSMEvent GESTURE_COMPLETED = new FSMEvent() {
    };
    private final KeyCodesX keyCode;

    public void notifyBecomeInactive() {
    }

    public GestureStatePressKey(GestureContext gestureContext, KeyCodesX keyCodesX) {
        super(gestureContext);
        this.keyCode = keyCodesX;
    }

    public void notifyBecomeActive() {
        getContext().getKeyboardReporter().reportKeys(this.keyCode);
        sendEvent(GESTURE_COMPLETED);
    }
}
