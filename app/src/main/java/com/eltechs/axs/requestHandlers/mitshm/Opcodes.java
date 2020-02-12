package com.eltechs.axs.requestHandlers.mitshm;

public abstract class Opcodes {
    public static final int Attach = 1;
    public static final int AttachFd = 6;
    public static final int CreatePixmap = 5;
    public static final int CreateSegment = 7;
    public static final int Detach = 2;
    public static final int GetImage = 4;
    public static final int PutImage = 3;
    public static final int QueryVersion = 0;

    private Opcodes() {
    }
}
