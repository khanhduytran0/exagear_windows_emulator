package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.Mask;

public interface KeyboardModifiersListener {
    void modifiersChanged(Mask<KeyButNames> mask);
}
