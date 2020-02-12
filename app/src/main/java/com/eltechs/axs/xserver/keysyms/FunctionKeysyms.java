package com.eltechs.axs.xserver.keysyms;

import android.support.v4.internal.view.SupportMenu;

public enum FunctionKeysyms implements KeysymsEnum {
    BACKSPACE(65288),
    TAB(65289),
    RETURN(65293),
    ESC(65307),
    F1(65470),
    F2(65471),
    F3(65472),
    F4(65473),
    F5(65474),
    F6(65475),
    F7(65476),
    F8(65477),
    F9(65478),
    F10(65479),
    F11(65480),
    F12(65481),
    INSERT(65379),
    DELETE(SupportMenu.USER_MASK);
    
    private int keysym;

    private FunctionKeysyms(int i) {
        this.keysym = i;
    }

    public int getKeysym() {
        return this.keysym;
    }
}
