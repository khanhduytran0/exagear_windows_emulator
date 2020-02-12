package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.drawables.ImageFormat;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.util.Collection;

public interface DrawablesManager {
    Drawable createDrawable(int i, Window window, int i2, int i3, byte b);

    Drawable createDrawable(int i, Window window, int i2, int i3, Visual visual);

    Drawable getDrawable(int i);

    Visual getPreferredVisual();

    Collection<ImageFormat> getSupportedImageFormats();

    Collection<Visual> getSupportedVisuals();

    Visual getVisual(int i);

    void removeDrawable(Drawable drawable);
}
