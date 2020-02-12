package com.eltechs.axs.xserver.graphicsContext;

import com.eltechs.axs.xserver.impl.masks.FlagsEnum;

public enum GraphicsContextParts implements FlagsEnum {
    FUNCTION(1),
    PLANE_MASK(2),
    FOREGROUND(4),
    BACKGROUND(8),
    LINE_WIDTH(16),
    LINE_STYLE(32),
    CAP_STYLE(64),
    JOIN_STYLE(128),
    FILL_STYLE(256),
    FILL_RULE(512),
    TILE(1024),
    STIPPLE(2048),
    TILE_STIPPLE_X_ORIGIN(4096),
    TILE_STIPPLE_Y_ORIGIN(8192),
    FONT(16384),
    SUBWINDOW_MODE(32768),
    GRAPHICS_EXPOSURES(65536),
    CLIP_X_ORIGIN(131072),
    CLIP_Y_ORIGIN(262144),
    CLIP_MASK(524288),
    DASH_OFFSET(1048576),
    DASHES(2097152),
    ARC_MODE(4194304);
    
    private final int flag;

    private GraphicsContextParts(int i) {
        this.flag = i;
    }

    public int flagValue() {
        return this.flag;
    }
}
