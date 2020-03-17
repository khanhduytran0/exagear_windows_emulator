package com.eltechs.axs;

import com.eltechs.axs.graphicsScene.SceneOfRectangles;

public class PointerVisualizer implements PointerEventListener, TouchScreenControlVisualizer {
    private final int color;
    private float x;
    private float y;

    public void attachedToGLContext(SceneOfRectangles sceneOfRectangles) {
    }

    public void detachedFromGLContext() {
    }

    public PointerVisualizer(int i) {
        this.color = i;
    }

    private void updatePointer(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void pointerEntered(float f, float f2) {
        updatePointer(f, f2);
    }

    public void pointerExited(float f, float f2) {
        updatePointer(f, f2);
    }

    public void pointerMove(float f, float f2) {
        updatePointer(f, f2);
    }
}
