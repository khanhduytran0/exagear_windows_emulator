package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.proto.input.annotations.EventId;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.events.ClientMessage;
import com.eltechs.axs.xserver.events.SelectionNotify;
import com.eltechs.axs.xserver.events.UnmapNotify;
import java.nio.ByteBuffer;

public abstract class EventBuilders {
    private EventBuilders() {
    }

    @EventId(id = 33)
    public static ClientMessage parseClientMessage(byte b, short s, Window window, Atom atom, ByteBuffer byteBuffer) {
        byte[] bArr = new byte[byteBuffer.limit()];
        byteBuffer.get(bArr, 0, byteBuffer.limit());
        ClientMessage clientMessage = new ClientMessage(b, s, window, atom, bArr);
        return clientMessage;
    }

    @EventId(id = 18)
    public static UnmapNotify parseUnmapNotify(byte b, short s, Window window, Window window2, boolean z, ByteBuffer byteBuffer) {
        return new UnmapNotify(window, window2, z);
    }

    @EventId(id = 31)
    public static SelectionNotify parseSelectionNotify(byte b, short s, int i, Window window, Atom atom, Atom atom2, @SpecialNullValue(0) Atom atom3, ByteBuffer byteBuffer) {
        SelectionNotify selectionNotify = new SelectionNotify(i, window, atom, atom2, atom3);
        return selectionNotify;
    }
}
