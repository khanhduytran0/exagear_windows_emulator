package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.FlagsEnum;

public enum WindowAttributeNames implements FlagsEnum {
    BACKGROUND_PIXMAP(1),
    BACKGROUND_PIXEL(2),
    BORDER_PIXMAP(4),
    BORDER_PIXEL(8),
    BIT_GRAVITY(16),
    WIN_GRAVITY(32),
    BACKING_STORE(64),
    BACKING_PLANES(128),
    BACKING_PIXEL(256),
    OVERRIDE_REDIRECT(512),
    SAVE_UNDER(1024),
    EVENT_MASK(2048),
    DO_NOT_PROPAGATE_MASK(4096),
    COLORMAP(8192),
    CURSOR(16384);
    
    private final int flag;

    private WindowAttributeNames(int i) {
        this.flag = i;
    }

    public int flagValue() {
        return this.flag;
    }
}
