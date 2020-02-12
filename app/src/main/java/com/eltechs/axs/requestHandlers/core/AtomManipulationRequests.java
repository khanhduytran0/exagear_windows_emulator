package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.ParamLength;
import com.eltechs.axs.proto.input.annotations.ParamName;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.AtomsManager;
import com.eltechs.axs.xserver.XServer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class AtomManipulationRequests extends HandlerObjectBase {
    /* access modifiers changed from: private */
    public static final Charset latin1 = Charset.forName("latin1");

    public AtomManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 16)
    @Locks({"ATOMS_MANAGER"})
    public void InternAtom(XResponse xResponse, @OOBParam @RequestParam boolean z, @RequestParam @ParamName("nameLength") short s, @RequestParam short s2, @RequestParam @ParamLength("nameLength") String str) throws IOException {
        final int i;
        AtomsManager atomsManager = this.xServer.getAtomsManager();
        if (z) {
            i = atomsManager.getAtomId(str);
        } else {
            i = atomsManager.internAtom(str);
        }
        xResponse.sendSimpleSuccessReply((byte) 0, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(i);
            }
        });
    }

    @RequestHandler(opcode = 17)
    @Locks({"ATOMS_MANAGER"})
    public void GetAtomName(XResponse xResponse, @RequestParam final Atom atom) throws IOException {
        final short length = (short) atom.getName().length();
        xResponse.sendSuccessReplyWithPayload((byte) 0, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putShort((short)length);
            }
        }, length, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.put(atom.getName().getBytes(AtomManipulationRequests.latin1));
            }
        });
    }
}
