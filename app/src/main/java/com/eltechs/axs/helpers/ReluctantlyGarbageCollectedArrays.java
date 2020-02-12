package com.eltechs.axs.helpers;

import java.lang.ref.SoftReference;

public class ReluctantlyGarbageCollectedArrays {
    private SoftReference<byte[]> byteArray = null;
    private SoftReference<int[]> intArray = null;
    private SoftReference<short[]> shortArray = null;

    public byte[] getByteArray(int i) {
        byte[] bArr = this.byteArray != null ? (byte[]) this.byteArray.get() : null;
        if (bArr != null && bArr.length >= i) {
            return bArr;
        }
        byte[] bArr2 = new byte[i];
        this.byteArray = new SoftReference<>(bArr2);
        return bArr2;
    }

    public short[] getShortArray(int i) {
        short[] sArr = this.shortArray != null ? (short[]) this.shortArray.get() : null;
        if (sArr != null && sArr.length >= i) {
            return sArr;
        }
        short[] sArr2 = new short[i];
        this.shortArray = new SoftReference<>(sArr2);
        return sArr2;
    }

    public int[] getIntArray(int i) {
        int[] iArr = this.intArray != null ? (int[]) this.intArray.get() : null;
        if (iArr != null && iArr.length >= i) {
            return iArr;
        }
        int[] iArr2 = new int[i];
        this.intArray = new SoftReference<>(iArr2);
        return iArr2;
    }
}
