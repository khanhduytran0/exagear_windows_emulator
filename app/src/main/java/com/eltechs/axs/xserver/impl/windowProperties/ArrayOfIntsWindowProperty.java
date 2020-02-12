package com.eltechs.axs.xserver.impl.windowProperties;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.WindowProperty;
import com.eltechs.axs.xserver.WindowProperty.Format;

public final class ArrayOfIntsWindowProperty extends MutableWindowProperty<int[]> {
    private int[] data;

    public ArrayOfIntsWindowProperty(Atom atom, int[] iArr) {
        super(atom);
        this.data = iArr;
    }

    public ArrayOfIntsWindowProperty(Atom atom) {
        this(atom, new int[0]);
    }

    public Format<int[]> getFormat() {
        return WindowProperty.ARRAY_OF_INTS;
    }

    public int[] getValues() {
        return this.data;
    }

    public void replaceValues(int[] iArr) {
        this.data = iArr;
    }

    public int getSizeInBytes() {
        return this.data.length * 4;
    }

    public void appendValues(int[] iArr) {
        int[] iArr2 = new int[(this.data.length + iArr.length)];
        System.arraycopy(this.data, 0, iArr2, 0, this.data.length);
        System.arraycopy(iArr, 0, iArr2, this.data.length, iArr.length);
        this.data = iArr2;
    }

    public void prependValues(int[] iArr) {
        int[] iArr2 = new int[(this.data.length + iArr.length)];
        System.arraycopy(this.data, 0, iArr2, iArr.length, this.data.length);
        System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
        this.data = iArr2;
    }
}
