package com.eltechs.axs;

import com.eltechs.axs.graphicsScene.SceneOfRectangles;

public interface TouchScreenControlVisualizer {
    void attachedToGLContext(SceneOfRectangles sceneOfRectangles);

    void detachedFromGLContext();
}
