package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import com.eltechs.axs.xserver.graphicsContext.SubwindowMode;

public class GraphicsContextImpl implements GraphicsContext {
    private int background = 1;
    private int foreground = 0;
    private PixelCompositionRule function = PixelCompositionRule.COPY;
    private final int id;
    private int lineWidth = 1;
    private int planeMask = -1;
    private final Drawable referenceDrawable;
    private SubwindowMode subwindowMode = SubwindowMode.CLIP_BY_CHILDREN;

    public GraphicsContextImpl(int i, Drawable drawable) {
        this.id = i;
        this.referenceDrawable = drawable;
    }

    public int getId() {
        return this.id;
    }

    public Drawable getReferenceDrawable() {
        return this.referenceDrawable;
    }

    public PixelCompositionRule getFunction() {
        return this.function;
    }

    public void setFunction(PixelCompositionRule pixelCompositionRule) {
        this.function = pixelCompositionRule;
    }

    public int getPlaneMask() {
        return this.planeMask;
    }

    public void setPlaneMask(int i) {
        if (i != -1) {
            Assert.notImplementedYet("GC::PlaneMask must be all ones. Other values are not supported yet.");
        }
        this.planeMask = i;
    }

    public int getForeground() {
        return this.foreground;
    }

    public void setForeground(int i) {
        this.foreground = i;
    }

    public int getBackground() {
        return this.background;
    }

    public void setBackground(int i) {
        this.background = i;
    }

    public SubwindowMode getSubwindowMode() {
        return this.subwindowMode;
    }

    public void setSubwindowMode(SubwindowMode subwindowMode2) {
        this.subwindowMode = subwindowMode2;
    }

    public int getLineWidth() {
        return this.lineWidth;
    }

    public void setLineWidth(int i) {
        this.lineWidth = i;
    }
}
