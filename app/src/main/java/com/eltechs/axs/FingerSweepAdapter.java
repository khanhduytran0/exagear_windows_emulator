package com.eltechs.axs;

import com.eltechs.axs.helpers.Assert;

public class FingerSweepAdapter implements PointerEventListener {
    private final boolean acceleration;
    private final boolean ignoreX;
    private final boolean ignoreY;
    private final PointerEventReporter pointerEventReporter;
    private float pointerX;
    private float pointerY;
    private final int sensitivity;
    private long time;

    public FingerSweepAdapter(PointerEventReporter pointerEventReporter2, int i, boolean z, boolean z2, boolean z3) {
        Assert.isFalse(z2 && z3, "Can't ignore both coordinates");
        this.pointerEventReporter = pointerEventReporter2;
        this.sensitivity = i;
        this.acceleration = z;
        this.ignoreX = z2;
        this.ignoreY = z3;
    }

    private float getAcceleratedValue(float f) {
        long currentTimeMillis = System.currentTimeMillis();
        if (!this.acceleration || currentTimeMillis == this.time) {
            return f * ((float) this.sensitivity);
        }
        float f2 = f / ((float) (currentTimeMillis - this.time));
        this.time = currentTimeMillis;
        return ((float) (Math.abs((int) f2) + 1)) * f * ((float) this.sensitivity);
    }

    public void pointerEntered(float f, float f2) {
        this.pointerX = f;
        this.pointerY = f2;
        this.time = System.currentTimeMillis();
    }

    public void pointerExited(float f, float f2) {
        pointerMoveDelta(f - this.pointerX, f2 - this.pointerY);
    }

    public void pointerMove(float f, float f2) {
        pointerMoveDelta(f - this.pointerX, f2 - this.pointerY);
        this.pointerX = f;
        this.pointerY = f2;
    }

    private void pointerMoveDelta(float f, float f2) {
        PointerEventReporter pointerEventReporter2 = this.pointerEventReporter;
        float f3 = 0.0f;
        float acceleratedValue = this.ignoreX ? 0.0f : getAcceleratedValue(f);
        if (!this.ignoreY) {
            f3 = getAcceleratedValue(f2);
        }
        pointerEventReporter2.pointerMoveDelta(acceleratedValue, f3);
    }
}
