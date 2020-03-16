package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GestureStateMachine.PointerContext.MoveMethod;
import com.eltechs.axs.GuestAppActionAdapters.MouseMoveAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;

public class GestureStateMouseWarpToFingerLastCoords extends AbstractGestureFSMState {
    public static FSMEvent GESTURE_COMPLETED = new FSMEvent() {
    };
    private final MouseMoveAdapter mover;
    private final PointerContext pointerContext;

    public void notifyBecomeInactive() {
    }

    public GestureStateMouseWarpToFingerLastCoords(GestureContext gestureContext, MouseMoveAdapter mouseMoveAdapter, PointerContext pointerContext2) {
        super(gestureContext);
        this.mover = mouseMoveAdapter;
        this.pointerContext = pointerContext2;
    }

    public void notifyBecomeActive() {
        Finger finger = getContext().getTouchArea().getLastFingerAction().getFinger();
        this.mover.prepareMoving(finger.getX(), finger.getY());
        this.mover.moveTo(finger.getX(), finger.getY());
        this.pointerContext.setLastMoveMethod(MoveMethod.AIM);
        sendEvent(GESTURE_COMPLETED);
    }
}
