package com.eltechs.axs.xserver;

import java.io.Serializable;

public class ScreenInfo implements Serializable {
    private static final long serialVersionUID = 8683847249124621970L;
    public final int depth;
    public final int heightInMillimeters;
    public final int heightInPixels;
    public final int widthInMillimeters;
    public final int widthInPixels;

    public ScreenInfo(int i, int i2, int i3, int i4, int i5) {
        this.widthInPixels = i;
        this.heightInPixels = i2;
        this.widthInMillimeters = i3;
        this.heightInMillimeters = i4;
        this.depth = i5;
    }

    public ScreenInfo(int i, int i2, int i3) {
        this.widthInPixels = i;
        this.heightInPixels = i2;
        this.widthInMillimeters = i / 10;
        this.heightInMillimeters = i2 / 10;
        this.depth = i3;
    }

    public String toString() {
        return String.format("%dx%dx%d", new Object[]{Integer.valueOf(this.widthInPixels), Integer.valueOf(this.heightInPixels), Integer.valueOf(this.depth)});
    }
}
