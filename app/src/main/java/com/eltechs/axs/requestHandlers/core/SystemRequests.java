package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.XServer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SystemRequests extends HandlerObjectBase {
    private final short DEFAULT_SCREEN_SAVER_INTERVAL_SEC = 600;
    private final short DEFAULT_SCREEN_SAVER_TIME_SEC = 600;

    public enum NoYesDefault {
        NO,
        YES,
        DEFAULT
    }

    @RequestHandler(opcode = 104)
    public void Bell(@OOBParam @RequestParam byte b) {
    }

    @RequestHandler(opcode = 127)
    public void NoOperation(@RequestParam ByteBuffer byteBuffer) {
    }

    @RequestHandler(opcode = 107)
    public void SetScreenSaver(@RequestParam short s, @RequestParam short s2, @RequestParam NoYesDefault noYesDefault, @RequestParam NoYesDefault noYesDefault2, @RequestParam short s3) throws IOException {
    }

    public SystemRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 108)
    public void GetScreenSaver(XResponse xResponse) throws IOException {
        xResponse.sendSimpleSuccessReply((byte) 0, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putShort((short)600);
                byteBuffer.putShort((short)600);
                byteBuffer.put((byte)(byte) NoYesDefault.YES.ordinal());
                byteBuffer.put((byte)(byte) NoYesDefault.YES.ordinal());
            }
        });
    }
}
