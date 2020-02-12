package com.eltechs.axs.alsaServer;

public class ClientOpcodes {
    public static final int Close = 0;
    public static final int Drain = 6;
    public static final int Pointer = 5;
    public static final int Prepare = 1;
    public static final int Start = 2;
    public static final int Stop = 4;
    public static final int Write = 3;

    private ClientOpcodes() {
    }
}
