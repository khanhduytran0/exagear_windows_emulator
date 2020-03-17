package com.eltechs.axs.helpers;

import com.eltechs.axs.proto.input.errors.CoreErrorCodes;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final boolean DECODE = false;
    public static final boolean ENCODE = true;
    private static final byte EQUALS_SIGN = 61;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte NEW_LINE = 10;
	private static final byte WHITE_SPACE_ENC = -5;
    private static final byte[] WEBSAFE_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, TarConstants.LF_GNUTYPE_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME, 77, 78, 79, 80, 81, 82, TarConstants.LF_GNUTYPE_SPARSE, 84, 85, 86, 87, TarConstants.LF_PAX_EXTENDED_HEADER_UC, 89, 90, 97, 98, 99, 100, 101, 102, TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, TarConstants.LF_PAX_EXTENDED_HEADER_LC, 121, 122, TarConstants.LF_NORMAL, TarConstants.LF_LINK, TarConstants.LF_SYMLINK, TarConstants.LF_CHR, TarConstants.LF_BLK, TarConstants.LF_DIR, TarConstants.LF_FIFO, TarConstants.LF_CONTIG, 56, 57, 45, 95};
    private static final byte[] WEBSAFE_DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, TarConstants.LF_BLK, TarConstants.LF_DIR, TarConstants.LF_FIFO, TarConstants.LF_CONTIG, 56, 57, 58, 59, 60, EQUALS_SIGN, -9, -9, -9, EQUALS_SIGN_ENC, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, CoreErrorCodes.ALLOC, CoreErrorCodes.COLORMAP, CoreErrorCodes.GCONTEXT, CoreErrorCodes.ID_CHOICE, CoreErrorCodes.NAME, CoreErrorCodes.LENGTH, CoreErrorCodes.IMPLEMENTATION, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, TarConstants.LF_NORMAL, TarConstants.LF_LINK, TarConstants.LF_SYMLINK, TarConstants.LF_CHR, -9, -9, -9, -9, -9};
    private static final byte[] ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, TarConstants.LF_GNUTYPE_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME, 77, 78, 79, 80, 81, 82, TarConstants.LF_GNUTYPE_SPARSE, 84, 85, 86, 87, TarConstants.LF_PAX_EXTENDED_HEADER_UC, 89, 90, 97, 98, 99, 100, 101, 102, TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, TarConstants.LF_PAX_EXTENDED_HEADER_LC, 121, 122, TarConstants.LF_NORMAL, TarConstants.LF_LINK, TarConstants.LF_SYMLINK, TarConstants.LF_CHR, TarConstants.LF_BLK, TarConstants.LF_DIR, TarConstants.LF_FIFO, TarConstants.LF_CONTIG, 56, 57, 43, 47};
    private static final byte[] DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, TarConstants.LF_BLK, TarConstants.LF_DIR, TarConstants.LF_FIFO, TarConstants.LF_CONTIG, 56, 57, 58, 59, 60, EQUALS_SIGN, -9, -9, -9, EQUALS_SIGN_ENC, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, CoreErrorCodes.ALLOC, CoreErrorCodes.COLORMAP, CoreErrorCodes.GCONTEXT, CoreErrorCodes.ID_CHOICE, CoreErrorCodes.NAME, CoreErrorCodes.LENGTH, CoreErrorCodes.IMPLEMENTATION, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, TarConstants.LF_NORMAL, TarConstants.LF_LINK, TarConstants.LF_SYMLINK, TarConstants.LF_CHR, -9, -9, -9, -9, -9};
    
    public static class DecoderException extends Exception {
        private static final long serialVersionUID = 1;

        public DecoderException() {
        }

        public DecoderException(String str) {
            super(str);
        }
    }

    private Base64() {
    }

    private static byte[] encode3to4(byte[] bArr, int i, int i2, byte[] bArr2, int i3, byte[] bArr3) {
        int i4 = 0;
        int i5 = (i2 > 0 ? (bArr[i] << 24) >>> 8 : 0) | (i2 > 1 ? (bArr[i + 1] << 24) >>> 16 : 0);
        if (i2 > 2) {
            i4 = (bArr[i + 2] << 24) >>> 24;
        }
        int i6 = i5 | i4;
        switch (i2) {
            case 1:
                bArr2[i3] = bArr3[i6 >>> 18];
                bArr2[i3 + 1] = bArr3[(i6 >>> 12) & 63];
                bArr2[i3 + 2] = EQUALS_SIGN;
                bArr2[i3 + 3] = EQUALS_SIGN;
                return bArr2;
            case 2:
                bArr2[i3] = bArr3[i6 >>> 18];
                bArr2[i3 + 1] = bArr3[(i6 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i6 >>> 6) & 63];
                bArr2[i3 + 3] = EQUALS_SIGN;
                return bArr2;
            case 3:
                bArr2[i3] = bArr3[i6 >>> 18];
                bArr2[i3 + 1] = bArr3[(i6 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i6 >>> 6) & 63];
                bArr2[i3 + 3] = bArr3[i6 & 63];
                return bArr2;
            default:
                return bArr2;
        }
    }

    public static String encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length, ALPHABET, true);
    }

    public static String encodeWebSafe(byte[] bArr, boolean z) {
        return encode(bArr, 0, bArr.length, WEBSAFE_ALPHABET, z);
    }

    public static String encode(byte[] bArr, int i, int i2, byte[] bArr2, boolean z) {
        byte[] encode = encode(bArr, i, i2, bArr2, Integer.MAX_VALUE);
        int length = encode.length;
        while (!z && length > 0 && encode[length - 1] == 61) {
            length--;
        }
        return new String(encode, 0, length);
    }

    public static byte[] encode(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int i4 = ((i2 + 2) / 3) * 4;
        byte[] bArr3 = new byte[(i4 + (i4 / i3))];
        int i5 = i2 - 2;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i6 < i5) {
            int i9 = ((bArr[i6 + i] << 24) >>> 8) | ((bArr[(i6 + 1) + i] << 24) >>> 16) | ((bArr[(i6 + 2) + i] << 24) >>> 24);
            bArr3[i7] = bArr2[i9 >>> 18];
            int i10 = i7 + 1;
            bArr3[i10] = bArr2[(i9 >>> 12) & 63];
            bArr3[i7 + 2] = bArr2[(i9 >>> 6) & 63];
            bArr3[i7 + 3] = bArr2[i9 & 63];
            i8 += 4;
            if (i8 == i3) {
                bArr3[i7 + 4] = 10;
                i8 = 0;
            } else {
                i10 = i7;
            }
            i6 += 3;
            i7 = i10 + 4;
        }
        if (i6 < i2) {
            encode3to4(bArr, i6 + i, i2 - i6, bArr3, i7, bArr2);
            if (i8 + 4 == i3) {
                bArr3[i7 + 4] = 10;
            }
        }
        return bArr3;
    }

    private static int decode4to3(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3) {
        int i3 = i + 2;
        if (bArr[i3] == 61) {
            bArr2[i2] = (byte) ((((bArr3[bArr[i + 1]] << 24) >>> 12) | ((bArr3[bArr[i]] << 24) >>> 6)) >>> 16);
            return 1;
        }
        int i4 = i + 3;
        if (bArr[i4] == 61) {
            int i5 = ((bArr3[bArr[i3]] << 24) >>> 18) | ((bArr3[bArr[i + 1]] << 24) >>> 12) | ((bArr3[bArr[i]] << 24) >>> 6);
            bArr2[i2] = (byte) (i5 >>> 16);
            bArr2[i2 + 1] = (byte) (i5 >>> 8);
            return 2;
        }
        int i6 = ((bArr3[bArr[i4]] << 24) >>> 24) | ((bArr3[bArr[i + 1]] << 24) >>> 12) | ((bArr3[bArr[i]] << 24) >>> 6) | ((bArr3[bArr[i3]] << 24) >>> 18);
        bArr2[i2] = (byte) (i6 >> 16);
        bArr2[i2 + 1] = (byte) (i6 >> 8);
        bArr2[i2 + 2] = (byte) i6;
        return 3;
    }

    public static byte[] decode(String str) throws DecoderException {
        byte[] bytes = str.getBytes();
        return decode(bytes, 0, bytes.length);
    }

    public static byte[] decodeWebSafe(String str) throws DecoderException {
        byte[] bytes = str.getBytes();
        return decodeWebSafe(bytes, 0, bytes.length);
    }

    public static byte[] decode(byte[] bArr) throws DecoderException {
        return decode(bArr, 0, bArr.length);
    }

    public static byte[] decodeWebSafe(byte[] bArr) throws DecoderException {
        return decodeWebSafe(bArr, 0, bArr.length);
    }

    public static byte[] decode(byte[] bArr, int i, int i2) throws DecoderException {
        return decode(bArr, i, i2, DECODABET);
    }

    public static byte[] decodeWebSafe(byte[] bArr, int i, int i2) throws DecoderException {
        return decode(bArr, i, i2, WEBSAFE_DECODABET);
    }

    public static byte[] decode(byte[] bArr, int i, int i2, byte[] bArr2) throws DecoderException {
        int i3;
        char c = 0;
        int i4 = i2;
        byte[] bArr3 = bArr2;
        byte[] bArr4 = new byte[(((i4 * 3) / 4) + 2)];
        byte[] bArr5 = new byte[4];
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            if (i5 >= i4) {
                break;
            }
            int i8 = i5 + i;
            byte b = (byte) (bArr[i8] & Byte.MAX_VALUE);
            byte b2 = bArr3[b];
            if (b2 >= -5) {
                if (b2 < -1) {
                    c = 4;
                } else if (b == 61) {
                    int i9 = i4 - i5;
                    byte b3 = (byte) (bArr[(i4 - 1) + i] & Byte.MAX_VALUE);
                    if (i6 == 0 || i6 == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("invalid padding byte '=' at byte offset ");
                        sb.append(i5);
                        throw new DecoderException(sb.toString());
                    } else if ((i6 == 3 && i9 > 2) || (i6 == 4 && i9 > 1)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("padding byte '=' falsely signals end of encoded value at offset ");
                        sb2.append(i5);
                        throw new DecoderException(sb2.toString());
                    } else if (b3 != 61 && b3 != 10) {
                        throw new DecoderException("encoded value has invalid trailing byte");
                    }
                } else {
                    int i10 = i6 + 1;
                    bArr5[i6] = b;
                    c = 4;
                    if (i10 == 4) {
                        i7 += decode4to3(bArr5, 0, bArr4, i7, bArr3);
                        i6 = 0;
                    } else {
                        i6 = i10;
                    }
                }
                i5++;
                char c2 = c;
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Bad Base64 input character at ");
                sb3.append(i5);
                sb3.append(": ");
                sb3.append(bArr[i8]);
                sb3.append("(decimal)");
                throw new DecoderException(sb3.toString());
            }
        }
        if (i6 == 0) {
            i3 = 0;
        } else if (i6 == 1) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("single trailing character at offset ");
            sb4.append(i4 - 1);
            throw new DecoderException(sb4.toString());
        } else {
            bArr5[i6] = EQUALS_SIGN;
            i3 = 0;
            i7 += decode4to3(bArr5, 0, bArr4, i7, bArr3);
        }
        byte[] bArr6 = new byte[i7];
        System.arraycopy(bArr4, i3, bArr6, i3, i7);
        return bArr6;
    }
}
