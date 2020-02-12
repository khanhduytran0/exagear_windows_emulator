package com.eltechs.axs.xconnectors;

import com.eltechs.axs.helpers.Assert;
import java.nio.ByteBuffer;

public class XRequest {
    private final XInputStream inputStream;
    private byte majorOpcode;
    private short minorOpcode;
    private int nBytesRemaining;
    private final int sequenceNumber;

    public XRequest(int i, XInputStream xInputStream, int i2) {
        this.sequenceNumber = i;
        this.inputStream = xInputStream;
        this.nBytesRemaining = i2;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public byte readByte() {
        updateRemainingBytesCount(1);
        return this.inputStream.getByte();
    }

    public short readShort() {
        updateRemainingBytesCount(2);
        return this.inputStream.getShort();
    }

    public int readInt() {
        updateRemainingBytesCount(4);
        return this.inputStream.getInt();
    }

    public void read(byte[] bArr) {
        updateRemainingBytesCount(bArr.length);
        this.inputStream.get(bArr);
    }

    public ByteBuffer readAsByteBuffer(int i) {
        updateRemainingBytesCount(i);
        return this.inputStream.getAsByteBuffer(i);
    }

    public void skip(int i) {
        updateRemainingBytesCount(i);
        this.inputStream.skip(i);
    }

    public void skipRequest() {
        this.inputStream.skip(this.nBytesRemaining);
        updateRemainingBytesCount(this.nBytesRemaining);
    }

    public byte getMajorOpcode() {
        return this.majorOpcode;
    }

    public void setMajorOpcode(byte b) {
        this.majorOpcode = b;
    }

    public short getMinorOpcode() {
        return this.minorOpcode;
    }

    public void setMinorOpcode(short s) {
        this.minorOpcode = s;
    }

    public int getRemainingBytesCount() {
        return this.nBytesRemaining;
    }

    private void updateRemainingBytesCount(int i) {
        Assert.state(this.nBytesRemaining >= i);
        this.nBytesRemaining -= i;
    }
}
