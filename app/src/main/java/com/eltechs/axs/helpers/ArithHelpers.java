package com.eltechs.axs.helpers;

public abstract class ArithHelpers {
    public static int extendAsUnsigned(byte b) {
        return b & 255;
    }

    public static int extendAsUnsigned(short s) {
        return s & 65535;
    }

    public static long extendAsUnsigned(int i) {
        return ((long) i) & 4294967295L;
    }

    public static int max(int i, int i2) {
        return i > i2 ? i : i2;
    }

    public static int min(int i, int i2) {
        return i < i2 ? i : i2;
    }

    public static float saturateInRange(float f, float f2, float f3) {
        return f < f2 ? f2 : f > f3 ? f3 : f;
    }

    public static int saturateInRange(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }

    private ArithHelpers() {
    }

    public static int unsignedSaturate(int i, int i2) {
        Assert.isTrue(i2 >= 0);
        if (i < 0) {
            return 0;
        }
        return i > i2 ? i2 : i;
    }

    public static float unsignedSaturate(float f, float f2) {
        Assert.isTrue(f2 >= 0.0f);
        if (f < 0.0f) {
            return 0.0f;
        }
        return f > f2 ? f2 : f;
    }
}
