package com.eltechs.axs.dsoundServer.impl;

import com.eltechs.axs.xserver.impl.masks.FlagsEnum;

public enum PlayFlags implements FlagsEnum {
    LOOPING(1),
    LOCHARDWARE(2),
    LOCSOFTWARE(4),
    TERMINATEBY_TIME(8),
    TERMINATEBY_DISTANCE(16),
    TERMINATEBY_PRIORITY(32);
    
    private final int value;

    private PlayFlags(int i) {
        this.value = i;
    }

    public int flagValue() {
        return this.value;
    }
}
