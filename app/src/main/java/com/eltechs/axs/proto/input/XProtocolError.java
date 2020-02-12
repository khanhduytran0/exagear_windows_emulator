package com.eltechs.axs.proto.input;

import java.io.*;

public abstract class XProtocolError extends Exception {
    private final byte code;
    private final int data;

    protected XProtocolError(byte b, int i) {
        this.code = b;
        this.data = i;
    }

    protected XProtocolError(byte b, int i, String str) {
        super(str);
        this.code = b;
        this.data = i;
    }

    public byte getErrorCode() {
        return this.code;
    }

    public int getData() {
        return this.data;
    }
}
