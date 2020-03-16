package com.eltechs.axs.helpers;

public abstract class MathHelpers {
    static final double log2 = Math.log(2.0d);

    private MathHelpers() {
    }

    public static int lowerPOT(int i) {
        Assert.isTrue(i > 0, "Non positive number");
        return (int) Math.floor(Math.pow(2.0d, Math.floor(Math.log((double) i) / log2)));
    }

    public static int upperPOT(int i) {
        int lowerPOT = lowerPOT(i);
        return lowerPOT < i ? lowerPOT * 2 : lowerPOT;
    }
}
