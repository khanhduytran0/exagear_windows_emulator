package com.eltechs.axs.proto.output.replies;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.output.POD;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import com.eltechs.axs.xserver.impl.drawables.VisualClass;

@POD({"visualId", "visualClass", "bitsPerRgbValue", "colormapEntries", "redMask", "greenMask", "blueMask", "unused"})
public class VisualType {
    public final byte bitsPerRgbValue;
    public final int blueMask;
    public final short colormapEntries;
    public final int greenMask;
    public final int redMask;
    public final int unused = 0;
    public final byte visualClass;
    public final int visualId;

    public VisualType(Visual visual) {
        Assert.isTrue(visual.isDisplayable(), "Only displayable visuals must be reported to a client as X Visuals.");
        this.visualId = visual.getId();
        this.visualClass = (byte) VisualClass.TRUE_COLOR.ordinal();
        this.bitsPerRgbValue = (byte) visual.getBitsPerRgbValue();
        this.colormapEntries = 256;
        this.redMask = visual.getRedMask();
        this.greenMask = visual.getGreenMask();
        this.blueMask = visual.getBlueMask();
    }
}
