package com.eltechs.axs.xserver.keysyms;

public enum NavigationKeysyms implements KeysymsEnum {
    RIGHT(65363),
    UP(65362),
    LEFT(65361),
    DOWN(65364),
    PRIOR(65365),
    NEXT(65366),
    HOME(65360),
    END(65367);
    
    private final int keysym;

    private NavigationKeysyms(int i) {
        this.keysym = i;
    }

    public int getKeysym() {
        return this.keysym;
    }
}
