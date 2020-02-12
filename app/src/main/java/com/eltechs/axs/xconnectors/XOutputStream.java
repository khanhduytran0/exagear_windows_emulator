package com.eltechs.axs.xconnectors;

import java.io.IOException;
import java.nio.ByteOrder;

public interface XOutputStream {
    void flush() throws IOException;

    XStreamLock lock();

    void setByteOrder(ByteOrder byteOrder);

    void write(int i, BufferFiller bufferFiller) throws IOException;

    void write(byte[] bArr) throws IOException;

    void write(byte[] bArr, int i, int i2) throws IOException;

    void writeByte(byte b) throws IOException;

    void writeInt(int i) throws IOException;

    void writeShort(short s) throws IOException;

    void writeString8(String str) throws IOException;
}
