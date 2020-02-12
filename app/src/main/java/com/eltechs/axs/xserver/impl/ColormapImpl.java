package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.Colormap;

public class ColormapImpl implements Colormap {
    private final int id;

    public ColormapImpl(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }
}
