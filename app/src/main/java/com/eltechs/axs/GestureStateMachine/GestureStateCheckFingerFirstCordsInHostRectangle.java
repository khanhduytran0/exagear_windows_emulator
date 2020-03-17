package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.geom.Rectangle;

public class GestureStateCheckFingerFirstCordsInHostRectangle extends AbstractGestureFSMState {
    public static FSMEvent INSIDE = new FSMEvent() {
    };
    public static FSMEvent OUTSIDE = new FSMEvent() {
    };
    private final Rectangle rect;

    public void notifyBecomeInactive() {
    }

    public GestureStateCheckFingerFirstCordsInHostRectangle(GestureContext gestureContext, Rectangle rectangle) {
        super(gestureContext);
        this.rect = rectangle;
    }

    public void notifyBecomeActive() {
        Finger finger = getContext().getTouchArea().getLastFingerAction().getFinger();
        float xWhenFirstTouched = finger.getXWhenFirstTouched();
        float yWhenFirstTouched = finger.getYWhenFirstTouched();
        float[] fArr = {(float) this.rect.x, (float) this.rect.y};
        float[] fArr2 = {(float) (this.rect.x + this.rect.width), (float) (this.rect.y + this.rect.height)};
        if (fArr[0] > xWhenFirstTouched || xWhenFirstTouched >= fArr2[0] || fArr[1] > yWhenFirstTouched || yWhenFirstTouched >= fArr2[1]) {
            sendEvent(OUTSIDE);
        } else {
            sendEvent(INSIDE);
        }
    }
}
