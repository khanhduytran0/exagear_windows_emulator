package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import com.eltechs.axs.xserver.graphicsContext.SubwindowMode;

public interface GraphicsContext {
    int getBackground();

    int getForeground();

    PixelCompositionRule getFunction();

    int getId();

    int getLineWidth();

    int getPlaneMask();

    Drawable getReferenceDrawable();

    SubwindowMode getSubwindowMode();
}
