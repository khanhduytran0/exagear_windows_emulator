package com.eltechs.axs.xserver.keysyms;

public enum ModifierKeysyms implements KeysymsEnum {
    SHIFT_L(65505),
    SHIFT_R(65506),
    CONTROL_L(65507),
    CONTROL_R(65508),
    ALT_L(65511),
    ALT_R(65512);
    
    private final int keysym;

    private ModifierKeysyms(int i) {
        this.keysym = i;
    }

    public int getKeysym() {
        return this.keysym;
    }
}
