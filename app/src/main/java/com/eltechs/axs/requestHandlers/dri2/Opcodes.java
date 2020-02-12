package com.eltechs.axs.requestHandlers.dri2;

public abstract class Opcodes {
    public static final int Authenticate = 2;
    public static final int Connect = 1;
    public static final int CopyRegion = 6;
    public static final int CreateDrawable = 3;
    public static final int DestroyDrawable = 4;
    public static final int GetBuffers = 5;
    public static final int GetBuffersWithFormat = 7;
    public static final int GetMSC = 9;
    public static final int QueryVersion = 0;
    public static final int SwapBuffers = 8;
    public static final int SwapInterval = 12;
    public static final int WaitMSC = 10;
    public static final int WaitSBC = 11;

    private Opcodes() {
    }
}
