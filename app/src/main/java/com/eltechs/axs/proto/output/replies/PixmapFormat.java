package com.eltechs.axs.proto.output.replies;

import com.eltechs.axs.proto.output.POD;

@POD({"depth", "bitsPerPixel", "scanlinePad", "unused"})
public class PixmapFormat {
    public final byte bitsPerPixel;
    public final byte depth;
    public final byte scanlinePad;
    public final byte[] unused = new byte[5];

    public PixmapFormat(byte b, byte b2, byte b3) {
        this.depth = b;
        this.bitsPerPixel = b2;
        this.scanlinePad = b3;
    }
}
