package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadShmSeg extends XProtocolError {
    public BadShmSeg(int i) {
        super(Byte.MIN_VALUE, i, String.format("Bad shm segment id %d.", new Object[]{Integer.valueOf(i)}));
    }

    public int getId() {
        return getData();
    }
}
