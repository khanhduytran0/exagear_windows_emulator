package com.eltechs.axs.xserver.impl.drawables;

public class ImageFormat {
    private final int bitsPerPixel;
    private final int depth;
    private final int scanlinePad;

    public ImageFormat(int i, int i2, int i3) {
        this.depth = i;
        this.bitsPerPixel = i2;
        this.scanlinePad = i3;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }

    public int getScanlinePad() {
        return this.scanlinePad;
    }
}
