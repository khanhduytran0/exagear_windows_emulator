package com.eltechs.axs.helpers.imageDetector;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.xserver.Drawable;
import java.util.Arrays;

public class ImageDetector {
    private final Rectangle area;
    private final byte[] sample;

    public ImageDetector(byte[] bArr, Rectangle rectangle) {
        this.area = rectangle;
        this.sample = bArr;
    }

    public boolean isSamplePresentInDrawable(Drawable drawable) {
        return Arrays.equals(drawable.getPainter().getZPixmap(this.area.x, this.area.y, this.area.width, this.area.height), this.sample);
    }
}
