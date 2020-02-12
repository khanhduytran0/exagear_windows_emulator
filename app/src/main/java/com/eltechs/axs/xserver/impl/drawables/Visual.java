package com.eltechs.axs.xserver.impl.drawables;

public class Visual {
    private final int bitsPerRgbValue;
    private final int blueMask;
    private final int depth;
    private boolean displayable;
    private final int greenMask;
    private final int id;
    private final int redMask;

    private Visual(int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        this.id = i;
        this.displayable = z;
        this.depth = i2;
        this.bitsPerRgbValue = i3;
        this.redMask = i4;
        this.greenMask = i5;
        this.blueMask = i6;
    }

    public static Visual makeDisplayableVisual(int i, int i2, int i3, int i4, int i5, int i6) {
        Visual visual = new Visual(i, true, i2, i3, i4, i5, i6);
        return visual;
    }

    public static Visual makeNonDisplayableVisual(int i, int i2) {
        Visual visual = new Visual(i, false, i2, i2, 0, 0, 0);
        return visual;
    }

    public int getId() {
        return this.id;
    }

    public boolean isDisplayable() {
        return this.displayable;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getBitsPerRgbValue() {
        return this.bitsPerRgbValue;
    }

    public VisualClass getVisualClass() {
        return VisualClass.TRUE_COLOR;
    }

    public int getRedMask() {
        return this.redMask;
    }

    public int getGreenMask() {
        return this.greenMask;
    }

    public int getBlueMask() {
        return this.blueMask;
    }
}
