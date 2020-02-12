package com.eltechs.axs;

import com.eltechs.axs.xserver.ViewFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WheelJoystickEventAdapter implements TouchEventAdapter {
    private final float coordX;
    private final float coordY;
    private Finger finger;
    private float prevX;
    private float prevY;
    private final float radiusDelta;
    private final ViewFacade viewFacade;
    private final Collection<WheelEventListener> wheelEventListeners = new ArrayList();
    private final float wheelExternalRadius;
    private final float wheelInternalRadius;

    public WheelJoystickEventAdapter(ViewFacade viewFacade2, float f, float f2, float f3, float f4, float f5) {
        this.viewFacade = viewFacade2;
        this.coordX = f;
        this.coordY = f2;
        this.wheelInternalRadius = f3;
        this.wheelExternalRadius = f4;
        this.radiusDelta = f5;
    }

    public void addListener(WheelEventListener... wheelEventListenerArr) {
        Collections.addAll(this.wheelEventListeners, wheelEventListenerArr);
    }

    public void removeLister(WheelEventListener... wheelEventListenerArr) {
        for (WheelEventListener remove : wheelEventListenerArr) {
            this.wheelEventListeners.remove(remove);
        }
    }

    private boolean isFingerOnTheWheel(Finger finger2) {
        float x = finger2.getX() - this.coordX;
        float y = finger2.getY() - this.coordY;
        double sqrt = Math.sqrt((double) ((x * x) + (y * y)));
        return sqrt > ((double) (this.wheelInternalRadius - this.radiusDelta)) && sqrt < ((double) (this.wheelExternalRadius + this.radiusDelta));
    }

    private void rotateWheel(Finger finger2) {
        if (!isFingerOnTheWheel(finger2)) {
            if (finger2 == this.finger) {
                this.finger = null;
            }
        } else if (this.finger == null) {
            this.finger = finger2;
            this.prevX = finger2.getX();
            this.prevY = finger2.getY();
        } else if (this.finger == finger2) {
            float x = finger2.getX() - this.coordX;
            float y = finger2.getY() - this.coordY;
            float f = this.prevX - this.coordX;
            float f2 = this.prevY - this.coordY;
            double degrees = Math.toDegrees(-Math.atan2((double) ((y * f) - (x * f2)), (double) ((x * f) + (y * f2))));
            this.prevX = finger2.getX();
            this.prevY = finger2.getY();
            for (WheelEventListener turnedAntiClockwise : this.wheelEventListeners) {
                turnedAntiClockwise.turnedAntiClockwise(degrees);
            }
        }
    }

    public void notifyTouched(Finger finger2, List<Finger> list) {
        rotateWheel(finger2);
    }

    public void notifyMovedIn(Finger finger2, List<Finger> list) {
        rotateWheel(finger2);
    }

    public void notifyReleased(Finger finger2, List<Finger> list) {
        if (this.finger == finger2 && this.finger != null) {
            this.finger = null;
        }
    }

    public void notifyMovedOut(Finger finger2, List<Finger> list) {
        if (this.finger == finger2 && this.finger != null) {
            this.finger = null;
        }
    }

    public void notifyMoved(Finger finger2, List<Finger> list) {
        rotateWheel(finger2);
    }
}
