package com.eltechs.axs.xserver;

public interface PointerListener {
    void pointerButtonPressed(int i);

    void pointerButtonReleased(int i);

    void pointerMoved(int i, int i2);

    void pointerWarped(int i, int i2);
}
