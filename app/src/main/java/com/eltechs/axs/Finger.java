package com.eltechs.axs;

public class Finger {
    private float x;
    private float xWhenFingerCountLastChanged;
    private final float xWhenFirstTouched;
    private float y;
    private float yWhenFingerCountLastChanged;
    private final float yWhenFirstTouched;

    public Finger(float f, float f2) {
        this.x = f;
        this.xWhenFirstTouched = f;
        this.y = f2;
        this.yWhenFirstTouched = f2;
    }

    public void update(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void release(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void notifyFingersCountChanged() {
        this.xWhenFingerCountLastChanged = this.x;
        this.yWhenFingerCountLastChanged = this.y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getXWhenFirstTouched() {
        return this.xWhenFirstTouched;
    }

    public float getYWhenFirstTouched() {
        return this.yWhenFirstTouched;
    }

    public float getXWhenFingerCountLastChanged() {
        return this.xWhenFingerCountLastChanged;
    }

    public float getYWhenFingerCountLastChanged() {
        return this.yWhenFingerCountLastChanged;
    }
}
