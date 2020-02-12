package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.GraphicsContextLifecycleListener;
import com.eltechs.axs.xserver.GraphicsContextLifecycleListenersList;
import com.eltechs.axs.xserver.GraphicsContextsManager;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.graphicsContext.ArcMode;
import com.eltechs.axs.xserver.graphicsContext.FillRule;
import com.eltechs.axs.xserver.graphicsContext.FillStyle;
import com.eltechs.axs.xserver.graphicsContext.GraphicsContextParts;
import com.eltechs.axs.xserver.graphicsContext.JoinStyle;
import com.eltechs.axs.xserver.graphicsContext.LineStyle;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import com.eltechs.axs.xserver.graphicsContext.SubwindowMode;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.HashMap;
import java.util.Map;

public class GraphicsContextsManagerImpl implements GraphicsContextsManager {
    private final Map<Integer, GraphicsContext> gcs = new HashMap();
    private final GraphicsContextLifecycleListenersList graphicsContextLifecycleListenersList = new GraphicsContextLifecycleListenersList();

    public GraphicsContext getGraphicsContext(int i) {
        return (GraphicsContext) this.gcs.get(Integer.valueOf(i));
    }

    public GraphicsContext createGraphicsContext(int i, Drawable drawable) {
        if (this.gcs.containsKey(Integer.valueOf(i))) {
            return null;
        }
        GraphicsContextImpl graphicsContextImpl = new GraphicsContextImpl(i, drawable);
        this.gcs.put(Integer.valueOf(i), graphicsContextImpl);
        this.graphicsContextLifecycleListenersList.sendGraphicsContextCreated(graphicsContextImpl);
        return graphicsContextImpl;
    }

    public void removeGraphicsContext(GraphicsContext graphicsContext) {
        this.gcs.remove(Integer.valueOf(graphicsContext.getId()));
        this.graphicsContextLifecycleListenersList.sendGraphicsContextFreed(graphicsContext);
    }

    public void updateGraphicsContext(GraphicsContext graphicsContext, Mask<GraphicsContextParts> mask, PixelCompositionRule pixelCompositionRule, Integer num, Integer num2, Integer num3, Integer num4, LineStyle lineStyle, Integer num5, JoinStyle joinStyle, FillStyle fillStyle, FillRule fillRule, Pixmap pixmap, Pixmap pixmap2, Integer num6, Integer num7, Integer num8, SubwindowMode subwindowMode, Boolean bool, Integer num9, Integer num10, Pixmap pixmap3, Integer num11, Integer num12, ArcMode arcMode) {
        Mask<GraphicsContextParts> mask2 = mask;
        GraphicsContextImpl graphicsContextImpl = (GraphicsContextImpl) graphicsContext;
        if (mask2.isSet(GraphicsContextParts.FUNCTION)) {
            graphicsContextImpl.setFunction(pixelCompositionRule);
        }
        if (mask2.isSet(GraphicsContextParts.PLANE_MASK)) {
            graphicsContextImpl.setPlaneMask(num.intValue());
        }
        if (mask2.isSet(GraphicsContextParts.FOREGROUND)) {
            graphicsContextImpl.setForeground(num2.intValue());
        }
        if (mask2.isSet(GraphicsContextParts.BACKGROUND)) {
            graphicsContextImpl.setBackground(num3.intValue());
        }
        if (mask2.isSet(GraphicsContextParts.SUBWINDOW_MODE)) {
            graphicsContextImpl.setSubwindowMode(subwindowMode);
        }
        if (mask2.isSet(GraphicsContextParts.LINE_WIDTH)) {
            graphicsContextImpl.setLineWidth(num4.intValue());
        }
    }

    public void addGraphicsContextsLifecycleListener(GraphicsContextLifecycleListener graphicsContextLifecycleListener) {
        this.graphicsContextLifecycleListenersList.addListener(graphicsContextLifecycleListener);
    }

    public void removeGraphicsContextLifecycleListener(GraphicsContextLifecycleListener graphicsContextLifecycleListener) {
        this.graphicsContextLifecycleListenersList.removeListener(graphicsContextLifecycleListener);
    }
}
