package com.eltechs.axs;

import com.eltechs.axs.graphicsScene.SceneOfRectangles;

public interface TouchScreenControl {
    void attachedToGLContext(SceneOfRectangles sceneOfRectangles);

    void detachedFromGLContext();

    void handleFingerDown(Finger finger);

    void handleFingerMove(Finger finger);

    void handleFingerUp(Finger finger);
}
