package com.eltechs.axs.alsaServer;

public enum ClientFormats {
    U8,
    S16LE,
    S16BE;

    public static boolean checkFormat(int i) {
        return i >= U8.ordinal() && i <= S16BE.ordinal();
    }
}
