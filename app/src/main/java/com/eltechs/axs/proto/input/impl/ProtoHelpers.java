package com.eltechs.axs.proto.input.impl;

public abstract class ProtoHelpers {
    public static final int SIZE_OF_INT = 4;

    private ProtoHelpers() {
    }

    public static int roundUpLength4(int i) {
        return ((i + 3) / 4) * 4;
    }

    public static int calculatePad(int i) {
        return roundUpLength4(i) - i;
    }

    public static int calculateLengthInWords(int i) {
        return i / 4;
    }
}
