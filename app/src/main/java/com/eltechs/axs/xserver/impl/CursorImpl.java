package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.Drawable;

public class CursorImpl implements Cursor {
    private final Drawable cursorImage;
    private final int hotSpotX;
    private final int hotSpotY;
    private final int id;
    private final Drawable maskImage;
    private final Drawable sourceImage;

    public CursorImpl(int i, int i2, int i3, Drawable drawable, Drawable drawable2, Drawable drawable3) {
        this.id = i;
        this.hotSpotX = i2;
        this.hotSpotY = i3;
        this.cursorImage = drawable;
        this.sourceImage = drawable2;
        this.maskImage = drawable3;
        if (drawable3 != null) {
            boolean z = false;
            Assert.isTrue(drawable3.getVisual().getDepth() == 1);
            if (drawable3.getWidth() == drawable2.getWidth() && drawable3.getHeight() == drawable2.getHeight()) {
                z = true;
            }
            Assert.isTrue(z);
        }
    }

    public int getId() {
        return this.id;
    }

    public int getHotSpotX() {
        return this.hotSpotX;
    }

    public int getHotSpotY() {
        return this.hotSpotY;
    }

    public Drawable getCursorImage() {
        return this.cursorImage;
    }

    public void recolorCursor(int i, int i2, int i3, int i4, int i5, int i6) {
        this.cursorImage.getPainter().drawAlphaMaskedBitmap(this.sourceImage, this.maskImage, i, i2, i3, i4, i5, i6);
    }
}
