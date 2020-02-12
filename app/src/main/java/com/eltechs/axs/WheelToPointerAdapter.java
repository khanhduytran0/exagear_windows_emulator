package com.eltechs.axs;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.xserver.ViewFacade;
// pimport com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class WheelToPointerAdapter implements WheelEventListener {
    private final boolean absolute;
    private double accumulatedAngle = 0.0d; // FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
    private final boolean isHorizontal;
    private final PointerEventReporter pointerEvenReporter;
    private float pointerX;
    private float pointerY;
    private final ViewFacade viewFacade;
    private final double wheelStep;

    public WheelToPointerAdapter(ViewFacade viewFacade2, PointerEventReporter pointerEventReporter, boolean z, boolean z2) {
        this.viewFacade = viewFacade2;
        this.pointerEvenReporter = pointerEventReporter;
        this.isHorizontal = z;
        this.absolute = z2;
        if (z2) {
            this.wheelStep = 1.0d;
            this.pointerX = (float) (viewFacade2.getScreenInfo().widthInPixels / 2);
            this.pointerY = (float) (viewFacade2.getScreenInfo().heightInPixels / 2);
            pointerEventReporter.pointerEntered(this.pointerX, this.pointerY);
            return;
        }
        this.wheelStep = 0.06d;
    }

    public void fixCoordinates() {
        this.pointerX = ArithHelpers.unsignedSaturate(this.pointerX, (float) (this.viewFacade.getScreenInfo().widthInPixels - 1));
        this.pointerY = ArithHelpers.unsignedSaturate(this.pointerY, (float) (this.viewFacade.getScreenInfo().widthInPixels - 1));
    }

    public int convertAngleToSteps(double d) {
        double d2 = this.accumulatedAngle + d;
        int i = (int) (d2 / this.wheelStep);
        this.accumulatedAngle = d2 - (((double) i) * this.wheelStep);
        return i;
    }

    public void turnedClockwise(double d) {
        if (this.absolute) {
            if (this.isHorizontal) {
                this.pointerX += (float) convertAngleToSteps(d);
            } else {
                this.pointerY += (float) convertAngleToSteps(d);
            }
            fixCoordinates();
            this.pointerEvenReporter.pointerMove(this.pointerX, this.pointerY);
            return;
        }
        if (this.isHorizontal) {
            this.pointerX = (float) convertAngleToSteps(d);
        } else {
            this.pointerY = (float) convertAngleToSteps(d);
        }
        this.pointerEvenReporter.pointerMoveDelta(this.pointerX, this.pointerY);
    }

    public void turnedAntiClockwise(double d) {
        turnedClockwise(-d);
    }
}
