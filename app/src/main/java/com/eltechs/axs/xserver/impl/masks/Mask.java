package com.eltechs.axs.xserver.impl.masks;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.impl.masks.FlagsEnum;

public class Mask<F extends FlagsEnum> {
    private final Class<F> flagsClass;
    private int value;

    private Mask(int i, Class<F> cls) {
        this.value = i;
        this.flagsClass = cls;
    }

    public static <F extends FlagsEnum> Mask<F> create(Class<F> cls, int i) {
        if (!isValueValid(cls, i)) {
            return null;
        }
        return new Mask<>(i, cls);
    }

    public static <F extends FlagsEnum> Mask<F> fullMask(Class<F> cls) {
        int i = 0;
        for (FlagsEnum flagValue : (FlagsEnum[]) cls.getEnumConstants()) {
            i |= flagValue.flagValue();
        }
        return new Mask<>(i, cls);
    }

    public static <F extends FlagsEnum> Mask<F> emptyMask(Class<F> cls) {
        return new Mask<>(0, cls);
    }

    public static <F extends FlagsEnum> Mask<F> of(F... fArr) {
        Class componentType = fArr.getClass().getComponentType();
        int i = 0;
        for (F flagValue : fArr) {
            i |= flagValue.flagValue();
        }
        return new Mask<>(i, componentType);
    }

    private static <F extends FlagsEnum> boolean isValueValid(Class<F> cls, int i) {
        int i2 = i;
        for (FlagsEnum flagValue : (FlagsEnum[]) cls.getEnumConstants()) {
            i2 &= ~flagValue.flagValue();
        }
        if (i2 == 0) {
            return true;
        }
        return false;
    }

    public int getRawMask() {
        return this.value;
    }

    public boolean isSet(F f) {
        return (f.flagValue() & this.value) != 0;
    }

    public boolean isEmpty() {
        return this.value == 0;
    }

    public boolean intersects(Mask<F> mask) {
        return (mask.value & this.value) != 0;
    }

    public boolean isSubsetOf(Mask<F> mask) {
        return ((~mask.value) & this.value) == 0;
    }

    public void joinWith(Mask<F> mask) {
        this.value = mask.value | this.value;
    }

    public void substract(Mask<F> mask) {
        this.value = (~mask.value) & this.value;
    }

    public void set(F f) {
        this.value = f.flagValue() | this.value;
    }

    public void clear(F f) {
        this.value = (~f.flagValue()) & this.value;
    }

    public void toggle(F f) {
        this.value = f.flagValue() ^ this.value;
    }

    public void setValue(F f, boolean z) {
        if (z) {
            set(f);
        } else {
            clear(f);
        }
    }

    public static <F extends FlagsEnum> Mask<F> mergeMasksOR(Mask<F> mask, Mask<F> mask2) {
        Assert.isTrue(mask.flagsClass == mask2.flagsClass);
        return create(mask.flagsClass, mask.getRawMask() | mask2.getRawMask());
    }

    public String toString() {
        FlagsEnum[] flagsEnumArr;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (FlagsEnum flagsEnum : (FlagsEnum[]) this.flagsClass.getEnumConstants()) {
            if ((this.value & flagsEnum.flagValue()) != 0) {
                if (i > 0) {
                    sb.append('|');
                }
                sb.append(((Enum) flagsEnum).name());
                i++;
            }
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Mask)) {
            return false;
        }
        Mask mask = (Mask) obj;
        if (mask.flagsClass == this.flagsClass && mask.value == this.value) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.value;
    }
}
