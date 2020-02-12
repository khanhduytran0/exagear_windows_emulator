package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.Signed;
import com.eltechs.axs.proto.input.annotations.Unsigned;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.proto.input.errors.BadValue;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.requestHandlers.IncomingImageFormat;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DrawingRequests extends HandlerObjectBase {

    public enum CoordinateMode {
        ORIGIN,
        PREVIOUS
    }

    @RequestHandler(opcode = 67)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolyRectangle(@RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
    }

    @RequestHandler(opcode = 66)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolySegment(@RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
    }

    public DrawingRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 72)
    @Locks({"DRAWABLES_MANAGER", "WINDOWS_MANAGER", "PIXMAPS_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PutImage(@OOBParam @RequestParam IncomingImageFormat incomingImageFormat, @RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam @Width(2) @Unsigned int i, @RequestParam @Width(2) @Unsigned int i2, @RequestParam @Width(2) @Signed int i3, @RequestParam @Width(2) @Signed int i4, @RequestParam byte b, @RequestParam byte b2, @RequestParam short s, @RequestParam ByteBuffer byteBuffer) throws XProtocolError {
        IncomingImageFormat incomingImageFormat2 = incomingImageFormat;
        byte b3 = b2;
        Painter painter = drawable.getPainter();
        if (!(graphicsContext.getFunction() == PixelCompositionRule.COPY || incomingImageFormat2 == IncomingImageFormat.Z_PIXMAP)) {
            Assert.notImplementedYet("Drawing with GC::Function values other than COPY is not supported yet.");
        }
        switch (incomingImageFormat2) {
            case BITMAP:
                if (b != 0) {
                    Assert.notImplementedYet("PutImage.leftPad != 0 not implemented yet");
                }
                if (b3 != 1) {
                    throw new BadMatch();
                }
                painter.drawBitmap(i3, i4, i, i2, byteBuffer);
                return;
            case XY_PIXMAP:
                if (drawable.getVisual().getDepth() != b3) {
                    throw new BadMatch();
                }
                return;
            case Z_PIXMAP:
                if (drawable.getVisual().getDepth() == b3 && b == 0) {
                    painter.drawZPixmap(graphicsContext.getFunction(), b3, i3, i4, 0, 0, i, i2, byteBuffer, i, i2);
                    return;
                }
                throw new BadMatch();
            default:
                Assert.state(false, String.format("Unknown IncomingImageFormat %s.", new Object[]{incomingImageFormat2}));
                return;
        }
    }

    @RequestHandler(opcode = 73)
    @Locks({"DRAWABLES_MANAGER", "PIXMAPS_MANAGER"})
    public void GetImage(XResponse xResponse, @OOBParam @RequestParam IncomingImageFormat incomingImageFormat, @RequestParam Drawable drawable, @RequestParam @Width(2) @Signed int i, @RequestParam @Width(2) @Signed int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam @Width(2) @Unsigned int i4, @RequestParam int i5) throws XProtocolError, IOException {
        final int i6;
        if (incomingImageFormat == IncomingImageFormat.BITMAP) {
            throw new BadValue(incomingImageFormat.ordinal());
        }
        Rectangle rectangle = new Rectangle(i, i2, i3, i4);
        if (!(this.xServer.getPixmapsManager().getPixmap(drawable.getId()) != null)) {
            i6 = drawable.getVisual().getId();
        } else if (!new Rectangle(0, 0, drawable.getWidth(), drawable.getHeight()).containsInnerRectangle(rectangle)) {
            throw new BadMatch();
        } else {
            i6 = 0;
        }
        Painter painter = drawable.getPainter();
        byte[] bArr = null;
        switch (incomingImageFormat) {
            case XY_PIXMAP:
                Assert.notImplementedYet("Reading data as XY Pixmap is unimplemented yet.");
                break;
            case Z_PIXMAP:
                bArr = painter.getZPixmap(i, i2, i3, i4);
                break;
            default:
                Assert.state(false, String.format("Unknown IncomingImageFormat %s.", new Object[]{incomingImageFormat}));
                break;
        }
		final byte[] bArrPut = bArr;
        xResponse.sendSuccessReplyWithPayload((byte) drawable.getVisual().getDepth(), new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(i6);
            }
        }, bArr.length, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.put(bArrPut);
            }
        });
    }

    @RequestHandler(opcode = 62)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void CopyArea(@RequestParam Drawable drawable, @RequestParam Drawable drawable2, @RequestParam GraphicsContext graphicsContext, @RequestParam @Width(2) @Signed int i, @RequestParam @Width(2) @Signed int i2, @RequestParam @Width(2) @Signed int i3, @RequestParam @Width(2) @Signed int i4, @RequestParam @Width(2) @Unsigned int i5, @RequestParam @Width(2) @Unsigned int i6) {
        drawable2.getPainter().copyArea(graphicsContext, drawable, i, i2, i3, i4, i5, i6);
    }

    @RequestHandler(opcode = 61)
    @Locks({"WINDOWS_MANAGER"})
    public void ClearArea(@OOBParam @RequestParam Boolean bool, @RequestParam Window window, @RequestParam @Width(2) @Signed int i, @RequestParam @Width(2) @Signed int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam @Width(2) @Unsigned int i4) {
        if (i3 != 0 || i4 != 0) {
            Assert.notImplementedYet("ClearArea is not implemented");
        }
    }

    @RequestHandler(opcode = 70)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolyFillRectangle(@RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
        if (graphicsContext.getFunction() == PixelCompositionRule.COPY) {
            drawable.getPainter().drawFilledRectangles(byteBuffer, graphicsContext.getBackground());
        }
    }

    @RequestHandler(opcode = 65)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolyLine(@OOBParam @RequestParam CoordinateMode coordinateMode, @RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
        if (coordinateMode == CoordinateMode.ORIGIN && graphicsContext.getLineWidth() == 1 && graphicsContext.getFunction() == PixelCompositionRule.COPY) {
            drawable.getPainter().drawLines(byteBuffer, graphicsContext.getForeground(), graphicsContext.getLineWidth());
        }
    }
}
