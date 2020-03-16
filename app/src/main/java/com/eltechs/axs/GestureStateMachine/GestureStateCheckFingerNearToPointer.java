package com.eltechs.axs.GestureStateMachine;

import android.graphics.PointF;
import com.eltechs.axs.Finger;
import com.eltechs.axs.GeometryHelpers;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.geom.Point;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.widgets.viewOfXServer.TransformationHelpers;

public class GestureStateCheckFingerNearToPointer extends AbstractGestureFSMState {
    public static FSMEvent FAR = new FSMEvent() {
    };
    public static FSMEvent NEAR = new FSMEvent() {
    };
    private final double distThreshold;
    private final boolean isTwoFingersAllowed;

    public void notifyBecomeInactive() {
    }

    public GestureStateCheckFingerNearToPointer(GestureContext gestureContext, double d, boolean z) {
        super(gestureContext);
        this.distThreshold = d;
        this.isTwoFingersAllowed = z;
    }

    public void notifyBecomeActive() {
        PointF pointF;
        Point pointerLocation = getContext().getViewFacade().getPointerLocation();
        float[] fArr = {(float) pointerLocation.x, (float) pointerLocation.y};
        TransformationHelpers.mapPoints(getContext().getHostView().getXServerToViewTransformationMatrix(), fArr);
        if (!this.isTwoFingersAllowed) {
            Assert.state(getContext().getFingers().size() < 2);
        } else {
            Assert.state(getContext().getFingers().size() > 0 && getContext().getFingers().size() <= 2);
        }
        if (getContext().getFingers().size() == 0) {
            Finger finger = getContext().getTouchArea().getLastFingerAction().getFinger();
            pointF = new PointF(finger.getXWhenFirstTouched(), finger.getYWhenFirstTouched());
        } else {
            pointF = getContext().getFingers().size() == 1 ? new PointF(((Finger) getContext().getFingers().get(0)).getXWhenFirstTouched(), ((Finger) getContext().getFingers().get(0)).getYWhenFirstTouched()) : getContext().getFingers().size() == 2 ? GeometryHelpers.center(new PointF(((Finger) getContext().getFingers().get(0)).getXWhenFirstTouched(), ((Finger) getContext().getFingers().get(0)).getYWhenFirstTouched()), new PointF(((Finger) getContext().getFingers().get(1)).getXWhenFirstTouched(), ((Finger) getContext().getFingers().get(1)).getYWhenFirstTouched())) : null;
        }
        if (((double) GeometryHelpers.distance(fArr[0], fArr[1], pointF.x, pointF.y)) < this.distThreshold) {
            sendEvent(NEAR);
        } else {
            sendEvent(FAR);
        }
    }
}
