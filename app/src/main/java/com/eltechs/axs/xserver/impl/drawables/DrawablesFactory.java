package com.eltechs.axs.xserver.impl.drawables;

import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Window;
import java.util.Collection;

public interface DrawablesFactory {
    Drawable create(int i, Window window, int i2, int i3, Visual visual);

    Visual getPreferredVisual();

    Visual getPreferredVisualForDepth(int i);

    Collection<ImageFormat> getSupportedImageFormats();

    Collection<Visual> getSupportedVisuals();

    Visual getVisual(int i);
}
