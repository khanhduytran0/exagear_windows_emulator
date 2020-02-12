package com.eltechs.axs.xserver.impl.windowProperties;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.WindowProperty;
import com.eltechs.axs.xserver.WindowProperty.Format;
import java.nio.charset.Charset;

public final class ArrayOfBytesWindowProperty extends MutableWindowProperty<byte[]> {
    private byte[] data;

    public ArrayOfBytesWindowProperty(Atom atom, byte[] bArr) {
        super(atom);
        this.data = bArr;
    }

    public ArrayOfBytesWindowProperty(Atom atom) {
        this(atom, new byte[0]);
    }

    public Format<byte[]> getFormat() {
        return WindowProperty.ARRAY_OF_BYTES;
    }

    public byte[] getValues() {
        return this.data;
    }

    public void replaceValues(byte[] bArr) {
        this.data = bArr;
    }

    public int getSizeInBytes() {
        return this.data.length;
    }

    public void appendValues(byte[] bArr) {
        byte[] bArr2 = new byte[(this.data.length + bArr.length)];
        System.arraycopy(this.data, 0, bArr2, 0, this.data.length);
        System.arraycopy(bArr, 0, bArr2, this.data.length, bArr.length);
        this.data = bArr2;
    }

    public void prependValues(byte[] bArr) {
        byte[] bArr2 = new byte[(this.data.length + bArr.length)];
        System.arraycopy(this.data, 0, bArr2, bArr.length, this.data.length);
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        this.data = bArr2;
    }

    public String toString() {
        if ("STRING".equals(getType().getName())) {
            return new String(this.data, Charset.forName("latin1"));
        }
        return super.toString();
    }
}
