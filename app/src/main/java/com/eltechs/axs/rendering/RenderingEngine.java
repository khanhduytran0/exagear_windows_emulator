package com.eltechs.axs.rendering;

public interface RenderingEngine {
    String getGLXExtensionsList();

    String getVendor();

    boolean isRenderingAvailable();
}
