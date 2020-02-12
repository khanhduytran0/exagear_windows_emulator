package com.eltechs.axs.requestHandlers.glx;

public abstract class Opcodes {
    public static final int CreateContext = 3;
    public static final int DestroyContext = 4;
    public static final int GetFBConfigs = 21;
    public static final int GetVisualConfigs = 14;
    public static final int MakeCurrent = 5;
    public static final int QueryServerString = 19;
    public static final int QueryVersion = 7;
    public static final int Render = 0;
    public static final int RenderLarge = 1;
    public static final int SetClientInfo2ARB = 35;

    private Opcodes() {
    }
}
