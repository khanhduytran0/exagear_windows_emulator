package com.eltechs.axs.graphicsScene.impl;

public abstract class TextureManagersFactory {
    private TextureManagersFactory() {
    }

    public static TexturesManager createTexturesManager() {
        return new TrivialTexturesManager();
    }
}
