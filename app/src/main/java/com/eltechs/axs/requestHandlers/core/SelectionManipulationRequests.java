package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SelectionManipulationRequests extends HandlerObjectBase {
    public SelectionManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 22)
    @Locks({"WINDOWS_MANAGER", "ATOMS_MANAGER", "SELECTIONS_MANAGER"})
    public void SetSelectionOwner(XClient xClient, XResponse xResponse, @RequestParam @SpecialNullValue(0) Window window, @RequestParam Atom atom, @RequestParam int i) throws XProtocolError, IOException {
        this.xServer.getSelectionsManager().setSelectionOwner(atom, window, xClient, i);
    }

    @RequestHandler(opcode = 23)
    @Locks({"WINDOWS_MANAGER", "ATOMS_MANAGER", "SELECTIONS_MANAGER"})
    public void GetSelectionOwner(XClient xClient, XResponse xResponse, @RequestParam Atom atom) throws XProtocolError, IOException {
        final Window selectionOwner = this.xServer.getSelectionsManager().getSelectionOwner(atom);
        xResponse.sendSimpleSuccessReply((byte) 0, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(selectionOwner != null ? selectionOwner.getId() : 0);
            }
        });
    }

    @RequestHandler(opcode = 24)
    @Locks({"WINDOWS_MANAGER", "ATOMS_MANAGER", "SELECTIONS_MANAGER"})
    public void ConvertSelection(XClient xClient, XResponse xResponse, @RequestParam Window window, @RequestParam Atom atom, @RequestParam Atom atom2, @RequestParam @SpecialNullValue(0) Atom atom3, @RequestParam int i) throws XProtocolError, IOException {
        this.xServer.getSelectionsManager().convertSelection(window, xClient, atom, atom2, atom3, i);
    }
}
