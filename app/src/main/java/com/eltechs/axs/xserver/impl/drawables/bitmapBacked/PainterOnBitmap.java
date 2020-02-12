package com.eltechs.axs.xserver.impl.drawables.bitmapBacked;

import android.graphics.Bitmap;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.ReluctantlyGarbageCollectedArrays;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Drawable.ModificationListener;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import java.nio.ByteBuffer;

public class PainterOnBitmap implements Painter {
    public static final int ALPHA_PIXEL = 0;
    public static final int BLACK_PIXEL = -16777216;
    public static final int WHITE_PIXEL = -1;
    private final ReluctantlyGarbageCollectedArrays arrays = new ReluctantlyGarbageCollectedArrays();
    private final Bitmap bitmap;
    private final BitmapBackedDrawable drawable;
    private final int height;
    private ModificationListener modificationListener;
    private final int width;

    private int makeVisibleRGBPixel(int i, int i2, int i3) {
        return (i << 16) | -16777216 | (i2 << 8) | i3;
    }

    private native void readBitmap(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int[] iArr);

    private native void readZPixmap24(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int[] iArr);

    public void drawFilledRectangles(ByteBuffer byteBuffer, int i) {
    }

    public void drawLines(ByteBuffer byteBuffer, int i, int i2) {
    }

    public void fillWithColor(int i) {
    }

    static {
        System.loadLibrary("axs-helpers");
    }

    public PainterOnBitmap(Bitmap bitmap2, BitmapBackedDrawable bitmapBackedDrawable, int i, int i2) {
        this.drawable = bitmapBackedDrawable;
        this.bitmap = bitmap2;
        this.width = i;
        this.height = i2;
    }

    public void drawBitmap(int i, int i2, int i3, int i4, ByteBuffer byteBuffer) {
        drawBitmapImpl(i, i2, i3, i4, byteBuffer);
    }

    public void drawZPixmap(PixelCompositionRule pixelCompositionRule, byte b, int i, int i2, int i3, int i4, int i5, int i6, ByteBuffer byteBuffer, int i7, int i8) {
        PainterOnBitmap painterOnBitmap;
        byte b2 = b;
        if (b2 == 1) {
            painterOnBitmap = this;
            painterOnBitmap.drawBitmapImpl(i, i2, i5, i6, byteBuffer);
        } else if (b2 != 24) {
            Assert.state(false, "Sorting out unsupported pixmap depths must be done in protocol handlers.");
            painterOnBitmap = this;
        } else {
            painterOnBitmap = this;
            painterOnBitmap.drawZPixmap24(i, i2, i5, i6, byteBuffer);
        }
        painterOnBitmap.modificationListener.changed(i, i2, i5, i6);
    }

    public byte[] getZPixmap(int i, int i2, int i3, int i4) {
        if (this.drawable.getVisual().getDepth() == 24) {
            return getZPixmap24(i, i2, i3, i4);
        }
        Assert.state(false, "Sorting out unsupported pixmap depths must be done in protocol handlers.");
        return null;
    }

    public void copyArea(GraphicsContext graphicsContext, Drawable drawable2, int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i3;
        int i8 = i4;
        int i9 = i5;
        int i10 = i6;
        PixelCompositionRule function = graphicsContext.getFunction();
        if (function != PixelCompositionRule.NO_OP) {
            Bitmap content = ((BitmapBackedDrawable) drawable2).getContent();
            if (i < 0 || i2 < 0 || i + i9 > content.getWidth() || i2 + i10 > content.getHeight()) {
                Assert.notImplementedYet();
            }
            Rectangle intersection = Rectangle.getIntersection(new Rectangle(i7, i8, i9, i10), new Rectangle(0, 0, this.bitmap.getWidth(), this.bitmap.getHeight()));
            if (intersection != null) {
                int i11 = i + (intersection.x - i7);
                int i12 = i2 + (intersection.y - i8);
                int i13 = intersection.width;
                int i14 = intersection.height;
                int i15 = i13 * i14;
                int[] intArray = this.arrays.getIntArray(i15);
                if (function == PixelCompositionRule.COPY) {
                    content.getPixels(intArray, 0, i9, i11, i12, i13, i14);
                } else if (function == PixelCompositionRule.CLEAR || function == PixelCompositionRule.SET) {
                    int i16 = function == PixelCompositionRule.CLEAR ? -16777216 : -1;
                    for (int i17 = 0; i17 < i15; i17++) {
                        intArray[i17] = i16;
                    }
                }
                this.bitmap.setPixels(intArray, 0, i9, intersection.x, intersection.y, i13, i14);
                this.modificationListener.changed(intersection.x, intersection.y, i13, i14);
            }
        }
    }

    public void drawAlphaMaskedBitmap(Drawable drawable2, Drawable drawable3, int i, int i2, int i3, int i4, int i5, int i6) {
        Bitmap content = ((BitmapBackedDrawable) drawable2).getContent();
        Bitmap content2 = drawable3 != null ? ((BitmapBackedDrawable) drawable3).getContent() : null;
        int makeVisibleRGBPixel = makeVisibleRGBPixel(i, i2, i3);
        int makeVisibleRGBPixel2 = makeVisibleRGBPixel(i4, i5, i6);
        int width2 = this.bitmap.getWidth();
        int height2 = this.bitmap.getHeight();
        int[] iArr = new int[(width2 * height2)];
        int i7 = 0;
        int i8 = 0;
        while (i7 < height2) {
            int i9 = i8;
            int i10 = 0;
            while (i10 < width2) {
                int i11 = content.getPixel(i10, i7) == -16777216 ? makeVisibleRGBPixel : makeVisibleRGBPixel2;
                if (drawable3 != null && content2.getPixel(i10, i7) == -1) {
                    i11 = 0;
                }
                int i12 = i9 + 1;
                iArr[i9] = i11;
                i10++;
                i9 = i12;
            }
            i7++;
            i8 = i9;
        }
        this.bitmap.setPixels(iArr, 0, width2, 0, 0, width2, height2);
    }

    private void drawZPixmap24(int i, int i2, int i3, int i4, ByteBuffer byteBuffer) {
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        Rectangle intersection = Rectangle.getIntersection(new Rectangle(i5, i6, i7, i8), new Rectangle(0, 0, this.bitmap.getWidth(), this.bitmap.getHeight()));
        if (intersection != null) {
            int i9 = intersection.width;
            int i10 = intersection.height;
            int i11 = intersection.x - i5;
            int i12 = ((i5 + i7) - 1) - ((intersection.x + intersection.width) - 1);
            int i13 = intersection.y - i6;
            int[] intArray = this.arrays.getIntArray(i9 * i10);
            readZPixmap24(byteBuffer, i7, i8, i11, i12, i13, intArray);
            this.bitmap.setPixels(intArray, 0, i7, intersection.x, intersection.y, i9, i10);
        }
    }

    private void drawBitmapImpl(int i, int i2, int i3, int i4, ByteBuffer byteBuffer) {
        int[] intArray = this.arrays.getIntArray(i3 * i4);
        readBitmap(byteBuffer, i3, i4, 4, -16777216, -1, intArray);
        this.bitmap.setPixels(intArray, 0, i3, i, i2, i3, i4);
    }

    private byte[] getZPixmap24(int i, int i2, int i3, int i4) {
        int i5 = i3 * i4;
        int i6 = i5 * 4;
        byte[] bArr = new byte[i6];
        int[] iArr = new int[i5];
        this.bitmap.getPixels(iArr, 0, i3, i, i2, i3, i4);
        int i7 = 0;
        int i8 = 0;
        while (i7 < i6) {
            bArr[i7 + 0] = (byte) iArr[i8];
            bArr[i7 + 1] = (byte) (iArr[i8] >> 8);
            bArr[i7 + 2] = (byte) (iArr[i8] >> 16);
            bArr[i7 + 3] = 0;
            i7 += 4;
            i8++;
        }
        return bArr;
    }

    public void setModificationListener(ModificationListener modificationListener2) {
        this.modificationListener = modificationListener2;
    }
}
