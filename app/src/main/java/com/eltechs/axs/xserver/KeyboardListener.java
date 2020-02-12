package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.Mask;

public interface KeyboardListener {
    void keyPressed(byte b, int i, Mask<KeyButNames> mask);

    void keyReleased(byte b, int i, Mask<KeyButNames> mask);
}
