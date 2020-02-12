package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.Mask;

public interface WindowChangeListener {
    void attributesChanged(Window window, Mask<WindowAttributeNames> mask);

    void geometryChanged(Window window);
}
