package com.eltechs.axs.proto.input.errors;

public abstract class CoreErrorCodes {
    public static final byte ACCESS = 10;
    public static final byte ALLOC = 11;
    public static final byte ATOM = 5;
    public static final byte COLORMAP = 12;
    public static final byte CURSOR = 6;
    public static final byte DRAWABLE = 9;
    public static final byte FONT = 7;
    public static final byte GCONTEXT = 13;
    public static final byte ID_CHOICE = 14;
    public static final byte IMPLEMENTATION = 17;
    public static final byte LENGTH = 16;
    public static final byte MATCH = 8;
    public static final byte NAME = 15;
    public static final byte PIXMAP = 4;
    public static final byte REQUEST = 1;
    public static final byte VALUE = 2;
    public static final byte WINDOW = 3;

    private CoreErrorCodes() {
    }
}
