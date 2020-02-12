package com.eltechs.axs.requestHandlers.mitshm;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.Signed;
import com.eltechs.axs.proto.input.annotations.Unsigned;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadAccess;
import com.eltechs.axs.proto.input.errors.BadImplementation;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.proto.input.errors.BadValue;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.requestHandlers.IncomingImageFormat;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.ShmSegment;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MITShmRequests extends HandlerObjectBase {
    public static final byte SHARED_PIXMAPS_AVAILABLE = 0;

    public MITShmRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 0)
    public void QueryVersion(XResponse xResponse) throws IOException {
        xResponse.sendSimpleSuccessReply((byte)0, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putShort((short)1);
                byteBuffer.putShort((short)1);
                byteBuffer.putShort((short)0);
                byteBuffer.putShort((short)0);
                byteBuffer.put((byte)0);
            }
        });
    }

    @RequestHandler(opcode = 1)
    @Locks({"SHM_SEGMENTS_MANAGER"})
    public void Attach(@NewXId @RequestParam int i, @RequestParam int i2, @RequestParam boolean z, @RequestParam byte b, @RequestParam short s) throws XProtocolError {
        this.xServer.getShmSegmentsManager().attachSegment(i, i2, !z);
    }

    @RequestHandler(opcode = 2)
    @Locks({"SHM_SEGMENTS_MANAGER"})
    public void Detach(@RequestParam ShmSegment shmSegment) throws XProtocolError {
        this.xServer.getShmSegmentsManager().detachSegment(shmSegment);
    }

    @RequestHandler(opcode = 3)
    @Locks({"SHM_SEGMENTS_MANAGER", "DRAWABLES_MANAGER", "WINDOWS_MANAGER", "PIXMAPS_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PutImage(@RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam @Width(2) @Unsigned int i, @RequestParam @Width(2) @Unsigned int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam @Width(2) @Unsigned int i4, @RequestParam @Width(2) @Unsigned int i5, @RequestParam @Width(2) @Unsigned int i6, @RequestParam @Width(2) @Signed int i7, @RequestParam @Width(2) @Signed int i8, @RequestParam byte b, @RequestParam IncomingImageFormat incomingImageFormat, @RequestParam boolean z, @RequestParam byte b2, @RequestParam ShmSegment shmSegment, @RequestParam int i9) throws XProtocolError {
        byte b3 = b;
        IncomingImageFormat incomingImageFormat2 = incomingImageFormat;
        if (z) {
            Assert.notImplementedYet();
        }
        if (!(graphicsContext.getFunction() == PixelCompositionRule.COPY || incomingImageFormat2 == IncomingImageFormat.Z_PIXMAP)) {
            Assert.notImplementedYet("Drawing with GC::Function values other than COPY is not supported yet.");
        }
        Painter painter = drawable.getPainter();
        switch (incomingImageFormat) {
            case BITMAP:
                if (b3 != 1) {
                    throw new BadMatch();
                }
                Assert.notImplementedYet();
                return;
            case XY_PIXMAP:
                if (drawable.getVisual().getDepth() != b3) {
                    throw new BadMatch();
                }
                Assert.notImplementedYet();
                return;
            case Z_PIXMAP:
                if (drawable.getVisual().getDepth() != b3) {
                    throw new BadMatch();
                }
                painter.drawZPixmap(graphicsContext.getFunction(), b3, i7, i8, i3, i4, i5, i6, shmSegment.getContent(), i, i2);
                return;
            default:
                Assert.state(false, String.format("Unknown IncomingImageFormat %s.", new Object[]{incomingImageFormat2}));
                return;
        }
    }

    @RequestHandler(opcode = 4)
    @Locks({"DRAWABLES_MANAGER", "PIXMAPS_MANAGER"})
    public void GetImage(XResponse xResponse, @RequestParam Drawable drawable, @RequestParam @Width(2) @Signed int i, @RequestParam @Width(2) @Signed int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam @Width(2) @Unsigned int i4, @RequestParam int i5, @RequestParam IncomingImageFormat incomingImageFormat, @RequestParam byte b, @RequestParam byte b2, @RequestParam byte b3, @RequestParam ShmSegment shmSegment, @RequestParam int i6) throws XProtocolError, IOException {
        if (incomingImageFormat == IncomingImageFormat.BITMAP) {
            throw new BadValue(incomingImageFormat.ordinal());
        }
        Rectangle rectangle = new Rectangle(i, i2, i3, i4);
        int i7 = 0;
        byte depth = (byte) drawable.getVisual().getDepth();
        if (!(this.xServer.getPixmapsManager().getPixmap(drawable.getId()) != null)) {
            i7 = drawable.getVisual().getId();
        } else if (!new Rectangle(0, 0, drawable.getWidth(), drawable.getHeight()).containsInnerRectangle(rectangle)) {
            throw new BadMatch();
        }
        if (!shmSegment.isWritable()) {
            throw new BadAccess();
        }
        Assert.notImplementedYet();
		final int i71 = i7;
        xResponse.sendSimpleSuccessReply(depth, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(i71);
                byteBuffer.putInt(0);
            }
        });
    }

    @RequestHandler(opcode = 5)
    @Locks({"PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
    public void CreatePixmap(@NewXId @RequestParam int i, @RequestParam Drawable drawable, @RequestParam @Width(2) @Unsigned int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam byte b, @RequestParam byte b2, @RequestParam byte b3, @RequestParam byte b4, @RequestParam ShmSegment shmSegment, @RequestParam int i4) throws XProtocolError {
        if (!shmSegment.isWritable()) {
            throw new BadAccess();
        }
        Assert.state(true);
        throw new BadImplementation();
    }
}
