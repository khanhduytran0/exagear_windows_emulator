package com.eltechs.axs.xserver.impl.drawables.gl;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Drawable.ModificationListener;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import java.nio.ByteBuffer;

public class PainterOnPersistentGLDrawable implements Painter {
    public static final int ALPHA_PIXEL = 0;
    public static final int BLACK_PIXEL = -16777216;
    public static final int WHITE_PIXEL = -1;
    private final PersistentGLDrawable drawable;
    private ModificationListener modificationListener;

    private native void copyPixmapArea(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void copyPixmapAreaAnd(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void copyPixmapAreaAndReverse(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void copyPixmapAreaOr(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void copyPixmapAreaOrReverse(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void drawAlphaMaskedBitmapImpl(long j, long j2, long j3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11);

    private native void drawBitmapImpl(ByteBuffer byteBuffer, long j, int i, int i2, int i3, int i4, int i5);

    private native void drawFilledRectangles(ByteBuffer byteBuffer, int i, int i2, long j, int i3, int i4, int i5);

    private native void drawLines(ByteBuffer byteBuffer, int i, int i2, long j, int i3, int i4, int i5);

    private native void drawZPixmapS16D16(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void drawZPixmapS16D16AND(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void drawZPixmapS16D16XOR(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void drawZPixmapS32D32(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void drawZPixmapS32D32AND(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void drawZPixmapS32D32XOR(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7, int i8, int i9, int i10);

    private native void getZPixmap16(byte[] bArr, int i, int i2, int i3, int i4, long j, int i5, int i6);

    private native void getZPixmap32(byte[] bArr, int i, int i2, int i3, int i4, long j, int i5, int i6);

    private native void setPixmapArea(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    public void fillWithColor(int i) {
    }

    static {
        System.loadLibrary("axs-helpers");
    }

    public PainterOnPersistentGLDrawable(PersistentGLDrawable persistentGLDrawable) {
        this.drawable = persistentGLDrawable;
    }

    public void copyArea(GraphicsContext graphicsContext, Drawable drawable2, int i, int i2, int i3, int i4, int i5, int i6) {
        Rectangle rectangle;
        int i7;
        int i8;
        int i9 = i3;
        int i10 = i4;
        int i11 = i5;
        int i12 = i6;
        PixelCompositionRule function = graphicsContext.getFunction();
        if (function != PixelCompositionRule.NO_OP) {
            if (i < 0 || i2 < 0 || i + i11 > drawable2.getWidth() || i2 + i12 > drawable2.getHeight()) {
                Assert.notImplementedYet();
            }
            Rectangle intersection = Rectangle.getIntersection(new Rectangle(i9, i10, i11, i12), new Rectangle(0, 0, this.drawable.getWidth(), this.drawable.getHeight()));
            if (intersection != null) {
                int i13 = i + (intersection.x - i9);
                int i14 = i2 + (intersection.y - i10);
                int i15 = intersection.width;
                int i16 = intersection.height;
                PersistentGLDrawable persistentGLDrawable = (PersistentGLDrawable) drawable2;
                switch (function) {
                    case COPY:
                        i8 = i16;
                        i7 = i15;
                        Rectangle rectangle2 = intersection;
                        rectangle = intersection;
                        copyPixmapArea(persistentGLDrawable.getContent(), this.drawable.getContent(), persistentGLDrawable.getWidth(), persistentGLDrawable.getHeight(), this.drawable.getWidth(), this.drawable.getHeight(), i7, i8, i13, i14, intersection.x, intersection.y);
                        break;
                    case AND_REVERSE:
                        i8 = i16;
                        i7 = i15;
                        Rectangle rectangle3 = intersection;
                        Rectangle rectangle4 = intersection;
                        copyPixmapAreaAndReverse(persistentGLDrawable.getContent(), this.drawable.getContent(), persistentGLDrawable.getWidth(), persistentGLDrawable.getHeight(), this.drawable.getWidth(), this.drawable.getHeight(), i7, i8, i13, i14, intersection.x, intersection.y);
                        rectangle = rectangle4;
                        break;
                    case OR_REVERSE:
                        i8 = i16;
                        i7 = i15;
                        Rectangle rectangle5 = intersection;
                        Rectangle rectangle6 = intersection;
                        copyPixmapAreaOrReverse(persistentGLDrawable.getContent(), this.drawable.getContent(), persistentGLDrawable.getWidth(), persistentGLDrawable.getHeight(), this.drawable.getWidth(), this.drawable.getHeight(), i7, i8, i13, i14, intersection.x, intersection.y);
                        rectangle = rectangle6;
                        break;
                    case AND:
                        i8 = i16;
                        i7 = i15;
                        Rectangle rectangle7 = intersection;
                        Rectangle rectangle8 = intersection;
                        copyPixmapAreaAnd(persistentGLDrawable.getContent(), this.drawable.getContent(), persistentGLDrawable.getWidth(), persistentGLDrawable.getHeight(), this.drawable.getWidth(), this.drawable.getHeight(), i7, i8, i13, i14, intersection.x, intersection.y);
                        rectangle = rectangle8;
                        break;
                    case OR:
                        long content = persistentGLDrawable.getContent();
                        long content2 = this.drawable.getContent();
                        int width = persistentGLDrawable.getWidth();
                        int height = persistentGLDrawable.getHeight();
                        int width2 = this.drawable.getWidth();
                        int height2 = this.drawable.getHeight();
                        int i17 = intersection.x;
                        int i18 = i17;
                        i8 = i16;
                        i7 = i15;
                        Rectangle rectangle9 = intersection;
                        copyPixmapAreaOr(content, content2, width, height, width2, height2, i15, i16, i13, i14, i18, intersection.y);
                        rectangle = rectangle9;
                        break;
                    case CLEAR:
                        setPixmapArea(this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i15, i16, intersection.x, intersection.y, -16777216);
                        break;
                    case SET:
                        setPixmapArea(this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i15, i16, intersection.x, intersection.y, -1);
                        break;
                }
                i8 = i16;
                i7 = i15;
                rectangle = intersection;
                Rectangle rectangle10 = rectangle;
                this.modificationListener.changed(rectangle10.x, rectangle10.y, i7, i8);
            }
        }
    }

    public void drawBitmap(int i, int i2, int i3, int i4, ByteBuffer byteBuffer) {
        if (!(i3 == this.drawable.getWidth() && i4 == this.drawable.getHeight() && i == 0 && i2 == 0)) {
            Assert.notImplementedYet();
        }
        drawBitmapImpl(byteBuffer, this.drawable.getContent(), i3, i4, -1, -16777216, 4);
    }

    public void drawZPixmap(PixelCompositionRule pixelCompositionRule, byte b, int i, int i2, int i3, int i4, int i5, int i6, ByteBuffer byteBuffer, int i7, int i8) {
        PixelCompositionRule pixelCompositionRule2 = pixelCompositionRule;
        byte b2 = b;
        int i9 = i;
        int i10 = i2;
        Rectangle intersection = Rectangle.getIntersection(new Rectangle(i9, i10, i5, i6), new Rectangle(0, 0, this.drawable.getWidth(), this.drawable.getHeight()));
        if (intersection != null) {
            int i11 = i3 + (intersection.x - i9);
            int i12 = i4 + (intersection.y - i10);
            if (b2 == 1) {
                if (pixelCompositionRule2 != PixelCompositionRule.COPY) {
                    Assert.notImplementedYet("drawZPixmap bitmap::Function values other than COPY is not supported yet.");
                }
                drawBitmapImpl(byteBuffer, this.drawable.getContent(), i7, i8, -1, -16777216, 4);
            } else if (b2 != 32) {
                switch (b2) {
                    case 15:
                    case 16:
                        if (pixelCompositionRule2 != PixelCompositionRule.COPY) {
                            if (pixelCompositionRule2 != PixelCompositionRule.AND) {
                                if (pixelCompositionRule2 != PixelCompositionRule.XOR) {
                                    Assert.notImplementedYet("drawZPixmap 15/16 bit depth::Function values other than COPY/AND/XOR is not supported yet.");
                                    break;
                                } else {
                                    drawZPixmap16XOR(byteBuffer, i7, i8, i11, i12, intersection.x, intersection.y, intersection.width, intersection.height);
                                    break;
                                }
                            } else {
                                drawZPixmap16AND(byteBuffer, i7, i8, i11, i12, intersection.x, intersection.y, intersection.width, intersection.height);
                                break;
                            }
                        } else {
                            drawZPixmap16(byteBuffer, i7, i8, i11, i12, intersection.x, intersection.y, intersection.width, intersection.height);
                            break;
                        }
                    default:
                        Assert.state(false, "Sorting out unsupported pixmap depths must be done in protocol handlers.");
                        break;
                }
            } else if (pixelCompositionRule2 == PixelCompositionRule.COPY) {
                drawZPixmap32(byteBuffer, i7, i8, i11, i12, intersection.x, intersection.y, intersection.width, intersection.height);
            } else if (pixelCompositionRule2 == PixelCompositionRule.AND) {
                drawZPixmap32AND(byteBuffer, i7, i8, i11, i12, intersection.x, intersection.y, intersection.width, intersection.height);
            } else if (pixelCompositionRule2 == PixelCompositionRule.XOR) {
                drawZPixmap32XOR(byteBuffer, i7, i8, i11, i12, intersection.x, intersection.y, intersection.width, intersection.height);
            } else {
                Assert.notImplementedYet("drawZPixmap 32 bit depth::Function values other than COPY/AND/XOR is not supported yet.");
            }
            this.modificationListener.changed(intersection.x, intersection.y, intersection.width, intersection.height);
        }
    }

    public void drawAlphaMaskedBitmap(Drawable drawable2, Drawable drawable3, int i, int i2, int i3, int i4, int i5, int i6) {
        drawAlphaMaskedBitmapImpl(this.drawable.getContent(), ((PersistentGLDrawable) drawable2).getContent(), drawable3 != null ? ((PersistentGLDrawable) drawable3).getContent() : 0, this.drawable.getWidth(), this.drawable.getHeight(), i, i2, i3, i4, i5, i6, -1, -16777216, 0);
    }

    public byte[] getZPixmap(int i, int i2, int i3, int i4) {
        int depth = this.drawable.getVisual().getDepth();
        if (depth == 16) {
            byte[] bArr = new byte[(i3 * i4 * 2)];
            getZPixmap16(bArr, i3, i4, i, i2, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight());
            return bArr;
        } else if (depth != 32) {
            Assert.notImplementedYet();
            return null;
        } else {
            byte[] bArr2 = new byte[(i3 * i4 * 4)];
            getZPixmap32(bArr2, i3, i4, i, i2, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight());
            return bArr2;
        }
    }

    public void drawLines(ByteBuffer byteBuffer, int i, int i2) {
        if (i2 == 1) {
            int depth = this.drawable.getVisual().getDepth();
            if (depth != 32) {
                switch (depth) {
                    case 15:
                    case 16:
                        break;
                }
            }
            drawLines(byteBuffer, (byteBuffer.limit() / 4) - 1, i, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), depth);
        }
    }

    public void drawFilledRectangles(ByteBuffer byteBuffer, int i) {
        int depth = this.drawable.getVisual().getDepth();
        if (depth != 32) {
            switch (depth) {
                case 15:
                case 16:
                    break;
                default:
                    return;
            }
        }
        drawFilledRectangles(byteBuffer, byteBuffer.limit() / 8, i, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), depth);
    }

    public void setModificationListener(ModificationListener modificationListener2) {
        this.modificationListener = modificationListener2;
    }

    private void drawZPixmap16(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        switch (this.drawable.getVisual().getDepth()) {
            case 15:
            case 16:
                drawZPixmapS16D16(byteBuffer, i, i2, i3, i4, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i5, i6, i7, i8);
                return;
            default:
                Assert.notImplementedYet();
                return;
        }
    }

    private void drawZPixmap16XOR(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        switch (this.drawable.getVisual().getDepth()) {
            case 15:
            case 16:
                drawZPixmapS16D16XOR(byteBuffer, i, i2, i3, i4, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i5, i6, i7, i8);
                return;
            default:
                Assert.notImplementedYet();
                return;
        }
    }

    private void drawZPixmap16AND(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        switch (this.drawable.getVisual().getDepth()) {
            case 15:
            case 16:
                drawZPixmapS16D16AND(byteBuffer, i, i2, i3, i4, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i5, i6, i7, i8);
                return;
            default:
                Assert.notImplementedYet();
                return;
        }
    }

    private void drawZPixmap32(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.drawable.getVisual().getDepth() != 32) {
            Assert.notImplementedYet();
            return;
        }
        drawZPixmapS32D32(byteBuffer, i, i2, i3, i4, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i5, i6, i7, i8);
    }

    private void drawZPixmap32XOR(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.drawable.getVisual().getDepth() != 32) {
            Assert.notImplementedYet();
            return;
        }
        drawZPixmapS32D32XOR(byteBuffer, i, i2, i3, i4, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i5, i6, i7, i8);
    }

    private void drawZPixmap32AND(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.drawable.getVisual().getDepth() != 32) {
            Assert.notImplementedYet();
            return;
        }
        drawZPixmapS32D32AND(byteBuffer, i, i2, i3, i4, this.drawable.getContent(), this.drawable.getWidth(), this.drawable.getHeight(), i5, i6, i7, i8);
    }
}
