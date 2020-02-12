package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.graphicsContext.ArcMode;
import com.eltechs.axs.xserver.graphicsContext.FillRule;
import com.eltechs.axs.xserver.graphicsContext.FillStyle;
import com.eltechs.axs.xserver.graphicsContext.GraphicsContextParts;
import com.eltechs.axs.xserver.graphicsContext.JoinStyle;
import com.eltechs.axs.xserver.graphicsContext.LineStyle;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import com.eltechs.axs.xserver.graphicsContext.SubwindowMode;
import com.eltechs.axs.xserver.impl.masks.Mask;

public interface GraphicsContextsManager {
    void addGraphicsContextsLifecycleListener(GraphicsContextLifecycleListener graphicsContextLifecycleListener);

    GraphicsContext createGraphicsContext(int i, Drawable drawable);

    GraphicsContext getGraphicsContext(int i);

    void removeGraphicsContext(GraphicsContext graphicsContext);

    void removeGraphicsContextLifecycleListener(GraphicsContextLifecycleListener graphicsContextLifecycleListener);

    void updateGraphicsContext(GraphicsContext graphicsContext, Mask<GraphicsContextParts> mask, PixelCompositionRule pixelCompositionRule, Integer num, Integer num2, Integer num3, Integer num4, LineStyle lineStyle, Integer num5, JoinStyle joinStyle, FillStyle fillStyle, FillRule fillRule, Pixmap pixmap, Pixmap pixmap2, Integer num6, Integer num7, Integer num8, SubwindowMode subwindowMode, Boolean bool, Integer num9, Integer num10, Pixmap pixmap3, Integer num11, Integer num12, ArcMode arcMode);
}
