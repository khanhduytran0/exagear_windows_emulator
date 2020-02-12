package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadPixmap extends XProtocolError {
    public BadPixmap(int i) {
        super((byte) 4, i, String.format("Bad pixmap id %d.", new Object[]{Integer.valueOf(i)}));
    }

    public int getId() {
        return getData();
    }
}
