package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DrawablesManipulationRequests extends HandlerObjectBase {

    public enum QuerySizeObject {
        CURSOR,
        TILE,
        STIPPLE
    }

    public DrawablesManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 14)
    @Locks({"DRAWABLES_MANAGER", "WINDOWS_MANAGER"})
    public void GetGeometry(XResponse xResponse, @RequestParam Drawable drawable) throws XProtocolError, IOException {
        final short s;
        final short s2;
        final short s3;
        byte depth = (byte) drawable.getVisual().getDepth();
        final short width = (short) drawable.getWidth();
        final short height = (short) drawable.getHeight();
        Window window = this.xServer.getWindowsManager().getWindow(drawable.getId());
        final Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
        if (window != null) {
            Rectangle boundingRectangle = window.getBoundingRectangle();
            short s4 = (short) boundingRectangle.x;
            s = (short) window.getWindowAttributes().getBorderWidth();
            s2 = (short) boundingRectangle.y;
            s3 = s4;
        } else {
            s3 = 0;
            s2 = 0;
            s = 0;
        }
        ResponseDataWriter r2 = new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(rootWindow.getId());
                byteBuffer.putShort((short)s3);
                byteBuffer.putShort((short)s2);
                byteBuffer.putShort((short)width);
                byteBuffer.putShort((short)height);
                byteBuffer.putShort((short)s);
            }
        };
        xResponse.sendSimpleSuccessReply(depth, (ResponseDataWriter) r2);
    }

    @RequestHandler(opcode = 97)
    @Locks({"DRAWABLES_MANAGER"})
    public void QueryBestSize(XResponse xResponse, @OOBParam @RequestParam QuerySizeObject querySizeObject, @RequestParam Drawable drawable, @RequestParam final short s, @RequestParam final short s2) throws IOException {
        xResponse.sendSimpleSuccessReply((byte) 0, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putShort((short)s);
                byteBuffer.putShort((short)s2);
            }
        });
    }
}
