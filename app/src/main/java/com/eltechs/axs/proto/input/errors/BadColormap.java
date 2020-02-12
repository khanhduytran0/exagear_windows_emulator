package com.eltechs.axs.proto.input.errors;

import com.eltechs.axs.proto.input.XProtocolError;

public class BadColormap extends XProtocolError {
    public BadColormap(int i) {
        super(CoreErrorCodes.COLORMAP, i, String.format("Bad colormap id %d.", new Object[]{Integer.valueOf(i)}));
    }

    public int getId() {
        return getData();
    }
}
