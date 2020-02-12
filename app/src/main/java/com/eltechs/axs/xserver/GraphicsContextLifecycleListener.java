package com.eltechs.axs.xserver;

public interface GraphicsContextLifecycleListener {
    void graphicsContextCreated(GraphicsContext graphicsContext);

    void graphicsContextFreed(GraphicsContext graphicsContext);
}
