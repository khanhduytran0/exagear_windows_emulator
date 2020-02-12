package com.eltechs.axs.xserver;

public interface EventsInjector {
    void injectKeyPress(byte b, int i);

    void injectKeyRelease(byte b, int i);

    void injectPointerButtonPress(int i);

    void injectPointerButtonRelease(int i);

    void injectPointerMove(int i, int i2);
}
