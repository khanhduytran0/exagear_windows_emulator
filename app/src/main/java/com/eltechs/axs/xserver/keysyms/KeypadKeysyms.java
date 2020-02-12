package com.eltechs.axs.xserver.keysyms;

public enum KeypadKeysyms implements KeysymsEnum {
    KEYPAD_DEL(65439);
    
    private final int keysym;

    private KeypadKeysyms(int i) {
        this.keysym = i;
    }

    public int getKeysym() {
        return this.keysym;
    }
}
