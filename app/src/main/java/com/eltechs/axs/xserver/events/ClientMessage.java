package com.eltechs.axs.xserver.events;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;

public class ClientMessage extends Event {
    public static final int MAX_PAYLOAD_LENGTH = 20;
    private final Atom atom;
    private final byte[] data;
    private final byte format;
    private final short sequenceNumber;
    private final Window window;

    public ClientMessage(byte b, short s, Window window2, Atom atom2, byte[] bArr) {
        super(33);
        Assert.isTrue(bArr.length <= 20, "The length of additional data in a client message can be at most 20 bytes.");
        this.format = b;
        this.sequenceNumber = s;
        this.window = window2;
        this.atom = atom2;
        if (bArr.length == 20) {
            this.data = bArr;
            return;
        }
        this.data = new byte[20];
        System.arraycopy(bArr, 0, this.data, 0, bArr.length);
    }

    public byte getFormat() {
        return this.format;
    }

    public short getSequenceNumber() {
        return this.sequenceNumber;
    }

    public Window getWindow() {
        return this.window;
    }

    public Atom getAtom() {
        return this.atom;
    }

    public byte[] getData() {
        return this.data;
    }
}
