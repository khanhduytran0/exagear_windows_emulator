package com.eltechs.axs.requestHandlers.glx;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.errors.BadValue;
import com.eltechs.axs.proto.output.PODWriter;
import com.eltechs.axs.proto.output.replies.VisualConfig;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.Collection;

public class GLXRequests extends HandlerObjectBase {
    /* access modifiers changed from: private */
    public static final Charset latin1 = Charset.forName("latin1");

    @RequestHandler(opcode = 35)
    public void SetClientInfo2ARB(@RequestParam int i, @RequestParam int i2, @RequestParam int i3, @RequestParam int i4, @RequestParam int i5, @RequestParam ByteBuffer byteBuffer) throws XProtocolError {
    }

    public GLXRequests(XServer xServer) {
        super(xServer);
    }

    private static void CheckGLXScreenValid(int i) throws XProtocolError {
        if (i != 0) {
            throw new BadValue(i);
        }
    }

    @RequestHandler(opcode = 7)
    public void QueryVersion(XResponse xResponse, @RequestParam int i, @RequestParam int i2) throws IOException {
        xResponse.sendSimpleSuccessReply((byte) 0, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(1);
                byteBuffer.putInt(4);
            }
        });
    }

    @RequestHandler(opcode = 19)
    @Locks({"RENDERING_ENGINE"})
    public void QueryServerString(XResponse xResponse, @RequestParam int i, @RequestParam int i2) throws IOException, XProtocolError {
        final String str;
        CheckGLXScreenValid(i);
        switch (i2) {
            case 1:
                str = this.xServer.getRenderingEngine().getVendor();
                break;
            case 2:
                str = "1.4";
                break;
            case 3:
                str = this.xServer.getRenderingEngine().getGLXExtensionsList();
                break;
            default:
                throw new BadValue(i2);
        }
        final int length = str.length() + 1;
        xResponse.sendSuccessReplyWithPayload((byte) 0, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(0);
                byteBuffer.putInt(length);
            }
        }, length, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.put(str.getBytes(GLXRequests.latin1));
            }
        });
    }

    @RequestHandler(opcode = 14)
    @Locks({"DRAWABLES_MANAGER"})
    public void GetVisualConfigs(XResponse xResponse, @RequestParam int i) throws IOException, XProtocolError {
        CheckGLXScreenValid(i);
        final Collection supportedVisuals = this.xServer.getDrawablesManager().getSupportedVisuals();
        final int onWireLength = PODWriter.getOnWireLength(new VisualConfig((Visual) supportedVisuals.iterator().next()));
        xResponse.sendSuccessReplyWithPayload((byte) 0, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(supportedVisuals.size());
                byteBuffer.putInt(onWireLength / 4);
            }
        }, onWireLength * supportedVisuals.size(), new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                for (Visual visualConfig : supportedVisuals) {
                    PODWriter.write(byteBuffer, (Object) new VisualConfig(visualConfig));
                }
            }
        });
    }

    @RequestHandler(opcode = 21)
    @Locks({"DRAWABLES_MANAGER"})
    public void GetFBConfigs(XResponse xResponse, @RequestParam int i) throws IOException, XProtocolError {
        CheckGLXScreenValid(i);
        final int size = this.xServer.getDrawablesManager().getSupportedVisuals().size();
        xResponse.sendSuccessReplyWithPayload((byte) 0, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(size);
                byteBuffer.putInt(44);
            }
        }, size * 44 * 2 * 4, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                IntBuffer asIntBuffer = byteBuffer.asIntBuffer();
                for (Visual visual : GLXRequests.this.xServer.getDrawablesManager().getSupportedVisuals()) {
                    asIntBuffer.put(new int[]{32779, visual.getId(), 32787, -1, 32786, -1, 4, 1, 32785, 1, 5, 1, 6, 0, 2, visual.getBitsPerRgbValue(), 3, 0, 7, 0, 8, Integer.bitCount(visual.getRedMask()), 9, Integer.bitCount(visual.getGreenMask()), 10, Integer.bitCount(visual.getBlueMask()), 11, visual.getDepth() > visual.getBitsPerRgbValue() ? visual.getDepth() - visual.getBitsPerRgbValue() : 0, 14, 0, 15, 0, 16, 0, 17, 0, 12, visual.getDepth(), 13, 0, 34, 32770, 32, 32768, 35, 32768, 37, -1, 38, -1, 39, -1, 40, -1, 36, -1, 32864, 0, 100001, 0, 100000, 0, 32808, 0, 32784, 7, 8400, -1, 8401, -1, 8402, -1, 8403, -1, 8404, -1, 32790, 0, 32791, 0, 32792, 0, 32793, 0, 32794, 0, 0, 0});
                }
                byteBuffer.position(byteBuffer.position() + (asIntBuffer.position() * 4));
            }
        });
    }

    @RequestHandler(opcode = 3)
    @Locks({"DRAWABLES_MANAGER"})
    public void CreateContext(@RequestParam int i, @RequestParam Visual visual, @RequestParam int i2, @RequestParam int i3, @RequestParam boolean z, @RequestParam byte b, @RequestParam short s) throws XProtocolError {
        CheckGLXScreenValid(i2);
    }
}
