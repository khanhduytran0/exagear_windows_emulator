package com.eltechs.axs.proto.output.replies;

import com.eltechs.axs.proto.output.POD;
import com.eltechs.axs.xserver.GLXConstants;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import com.eltechs.axs.xserver.impl.drawables.VisualClass;

@POD({"visualId", "visualClass", "isRGBA", "bitsRed", "bitsGreen", "bitsBlue", "bitsAlpha", "accBitsRed", "accBitsGreen", "accBitsBlue", "accBitsAlpha", "isDoubleBuffered", "isStereo", "bitsRGB", "bitsDepth", "bitsStencil", "auxBuffersNum", "level", "props"})
public class VisualConfig {
    public final int accBitsAlpha;
    public final int accBitsBlue;
    public final int accBitsGreen;
    public final int accBitsRed;
    public final int auxBuffersNum;
    public final int bitsAlpha;
    public final int bitsBlue;
    public final int bitsDepth;
    public final int bitsGreen;
    public final int bitsRGB;
    public final int bitsRed;
    public final int bitsStencil;
    public final int isDoubleBuffered;
    public final int isRGBA;
    public final int isStereo;
    public final int level;
    public final int[] props = {32, 32768, 35, 32768, 37, -1, 38, -1, 39, -1, 40, -1, 36, -1, GLXConstants.GLX_SAMPLES_SGIS, 0, 100000, 0, 0, 0, 0, 0};
    public final int visualClass;
    public final int visualId;

    public VisualConfig(Visual visual) {
        this.visualId = visual.getId();
        this.visualClass = VisualClass.TRUE_COLOR.ordinal();
        this.isRGBA = 1;
        this.bitsRed = Integer.bitCount(visual.getRedMask());
        this.bitsGreen = Integer.bitCount(visual.getGreenMask());
        this.bitsBlue = Integer.bitCount(visual.getBlueMask());
        this.bitsAlpha = visual.getDepth() > visual.getBitsPerRgbValue() ? visual.getDepth() - visual.getBitsPerRgbValue() : 0;
        this.accBitsRed = 0;
        this.accBitsGreen = 0;
        this.accBitsBlue = 0;
        this.accBitsAlpha = 0;
        this.isDoubleBuffered = 1;
        this.isStereo = 0;
        this.bitsRGB = visual.getBitsPerRgbValue();
        this.bitsDepth = visual.getDepth();
        this.bitsStencil = 0;
        this.auxBuffersNum = 0;
        this.level = 0;
    }
}
