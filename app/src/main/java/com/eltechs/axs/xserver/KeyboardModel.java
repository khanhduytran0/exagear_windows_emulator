package com.eltechs.axs.xserver;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;

public class KeyboardModel {
    public static final int KEYSYMS_PER_KEYCODE_IN_LAYOUT = 2;
    public static final int KEYS_COUNT = 248;
    public static final int MAX_KEYCODE = 255;
    public static final int MIN_KEYCODE = 8;
    private final int[][] keymap;
    private final KeyboardModifiersLayout modifiersLayout;

    public KeyboardModel(KeyboardModifiersLayout keyboardModifiersLayout, KeyboardLayout... keyboardLayoutArr) {
        this.modifiersLayout = keyboardModifiersLayout;
        Assert.isTrue(keyboardLayoutArr.length <= 127, "Can have at most 127 keyboard layouts");
        Assert.state(true, "Keyboard layout must define 2 keysyms per keycode: for minuscule and majuscule letters.");
        this.keymap = new int[(keyboardLayoutArr.length * 2)][];
        for (int i = 0; i < keyboardLayoutArr.length; i++) {
            int i2 = 2 * i;
            int i3 = i2 + 0;
            this.keymap[i3] = new int[KEYS_COUNT];
            int i4 = i2 + 1;
            this.keymap[i4] = new int[KEYS_COUNT];
            System.arraycopy(keyboardLayoutArr[i].getMinusculeKeysyms(), 0, this.keymap[i3], 0, KEYS_COUNT);
            System.arraycopy(keyboardLayoutArr[i].getMajusculeKeysyms(), 0, this.keymap[i4], 0, KEYS_COUNT);
        }
    }

    public int getLayoutsCount() {
        return this.keymap.length / 2;
    }

    public void getKeysymsForKeycode(int i, int[] iArr) {
        for (int i2 = 0; i2 < this.keymap.length; i2++) {
            iArr[i2] = this.keymap[i2][i - 8];
        }
    }

    public void getKeysymsForKeycodeGroup1(int i, int[] iArr) {
        int i2 = i - 8;
        iArr[0] = this.keymap[0][i2];
        iArr[1] = this.keymap[1][i2];
    }

    public void setKeysymsForKeycodeGroup1(int i, int i2, int i3) {
        int i4 = i - 8;
        this.keymap[0][i4] = i2;
        this.keymap[1][i4] = i3;
    }

    public KeyboardModifiersLayout getModifiersLayout() {
        return this.modifiersLayout;
    }

    public boolean isKeycodeValid(byte b) {
        int extendAsUnsigned = ArithHelpers.extendAsUnsigned(b);
        return extendAsUnsigned >= 8 && extendAsUnsigned <= 255;
    }
}
