package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import java.nio.ByteBuffer;

public interface Painter {
    void copyArea(GraphicsContext graphicsContext, Drawable drawable, int i, int i2, int i3, int i4, int i5, int i6);

    void drawAlphaMaskedBitmap(Drawable drawable, Drawable drawable2, int i, int i2, int i3, int i4, int i5, int i6);

    void drawBitmap(int i, int i2, int i3, int i4, ByteBuffer byteBuffer);

    void drawFilledRectangles(ByteBuffer byteBuffer, int i);

    void drawLines(ByteBuffer byteBuffer, int i, int i2);

    void drawZPixmap(PixelCompositionRule pixelCompositionRule, byte b, int i, int i2, int i3, int i4, int i5, int i6, ByteBuffer byteBuffer, int i7, int i8);

    void fillWithColor(int i);

    byte[] getZPixmap(int i, int i2, int i3, int i4);
}
