package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Pixmap;

public class PixmapImpl implements Pixmap {
    private final Drawable backingStore;

    public PixmapImpl(Drawable drawable) {
        this.backingStore = drawable;
    }

    public Drawable getBackingStore() {
        return this.backingStore;
    }
}
