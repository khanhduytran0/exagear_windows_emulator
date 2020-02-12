package com.eltechs.axs.dsoundServer;

public abstract class Opcodes {
    public static final int Attach = 0;
    public static final int InitGlobalNotifier = 255;
    public static final int Play = 1;
    public static final int RecalcVolpan = 5;
    public static final int SetCurrentPosition = 3;
    public static final int SetNotifications = 4;
    public static final int Stop = 2;

    private Opcodes() {
    }
}
