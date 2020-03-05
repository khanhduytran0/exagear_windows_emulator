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
    @EventId(id = 33)
    public static ClientMessage parseClientMessage(byte paramByte, short paramShort, Window paramWindow, Atom paramAtom, ByteBuffer paramByteBuffer) {
        byte[] arrayOfByte = new byte[paramByteBuffer.limit()];
        paramByteBuffer.get(arrayOfByte, 0, paramByteBuffer.limit());
        return new ClientMessage(paramByte, paramShort, paramWindow, paramAtom, arrayOfByte);
    }

    @EventId(id = 31)
    public static SelectionNotify parseSelectionNotify(byte paramByte, short paramShort, int paramInt, Window paramWindow, Atom paramAtom1, Atom paramAtom2, @SpecialNullValue(0) Atom paramAtom3, ByteBuffer paramByteBuffer) {
        return new SelectionNotify(paramInt, paramWindow, paramAtom1, paramAtom2, paramAtom3);
    }

    @EventId(id = 18)
    public static UnmapNotify parseUnmapNotify(byte paramByte, short paramShort, Window paramWindow1, Window paramWindow2, boolean paramBoolean, ByteBuffer paramByteBuffer) {
        return new UnmapNotify(paramWindow1, paramWindow2, paramBoolean);
    }
}
