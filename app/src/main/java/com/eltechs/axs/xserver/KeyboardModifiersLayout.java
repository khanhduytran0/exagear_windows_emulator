package com.eltechs.axs.xserver;

import com.eltechs.axs.helpers.Assert;
import java.util.Iterator;

public class KeyboardModifiersLayout {
    public static final int KEYCODES_PER_MODIFIER_NUM = 4;
    private static final int MODIFIER_MAX = KeyButNames.MOD5.ordinal();
    public static final int MODIFIERS_NUM = (MODIFIER_MAX + 1);
    private boolean[] modifierStickness;
    /* access modifiers changed from: private */
    public byte[][] modifierToKeycodes = new byte[MODIFIERS_NUM][];

    public KeyboardModifiersLayout() {
        for (int i = 0; i < MODIFIERS_NUM; i++) {
            this.modifierToKeycodes[i] = new byte[4];
        }
        this.modifierStickness = new boolean[MODIFIERS_NUM];
    }

    public Iterable<Byte> getModifierKeycodes(final KeyButNames keyButNames) {
        return new Iterable<Byte>() {
            public Iterator<Byte> iterator() {
                final byte[] bArr = KeyboardModifiersLayout.this.modifierToKeycodes[keyButNames.ordinal()];
                return new Iterator<Byte>() {
                    int currentNum = 0;

                    public boolean hasNext() {
                        return this.currentNum < 4 && bArr[this.currentNum] != 0;
                    }

                    public Byte next() {
                        if (!hasNext()) {
                            return Byte.valueOf((byte) 0);
                        }
                        // byte[] bArr = bArr;
                        this.currentNum = currentNum++;
                        return Byte.valueOf(bArr[this.currentNum]);
                    }

                    public void remove() {
                        Assert.state(false, "Read-only iterator.");
                    }
                };
            }
        };
    }

    public void setKeycodeToModifier(byte b, KeyButNames keyButNames) {
        Assert.isTrue(keyButNames.ordinal() <= MODIFIER_MAX);
        byte[] bArr = this.modifierToKeycodes[keyButNames.ordinal()];
        for (int i = 0; i < 4; i++) {
            if (bArr[i] == 0) {
                bArr[i] = b;
                return;
            }
        }
        Assert.state(false, String.format("Unable to assign keycode %d to modifier %d: too many keycodes already assigned", new Object[]{Byte.valueOf(b), Integer.valueOf(keyButNames.ordinal())}));
    }

    public KeyButNames getModifierByKeycode(byte b) {
        int i = 0;
        while (i < MODIFIERS_NUM) {
            int i2 = 0;
            while (i2 < 4 && this.modifierToKeycodes[i][i2] != 0) {
                if (this.modifierToKeycodes[i][i2] == b) {
                    return KeyButNames.values()[i];
                }
                i2++;
            }
            i++;
        }
        return null;
    }

    public boolean isModifierSticky(KeyButNames keyButNames) {
        return this.modifierStickness[keyButNames.ordinal()];
    }

    public void setModifierSticky(KeyButNames keyButNames, boolean z) {
        this.modifierStickness[keyButNames.ordinal()] = z;
    }
}
