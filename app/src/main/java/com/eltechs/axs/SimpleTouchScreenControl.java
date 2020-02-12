package com.eltechs.axs;

import com.eltechs.axs.graphicsScene.SceneOfRectangles;

public class SimpleTouchScreenControl implements TouchScreenControl {
    private final TouchArea[] touchAreas;
    private final TouchScreenControlVisualizer[] visualizers;

    public SimpleTouchScreenControl(TouchArea[] touchAreaArr, TouchScreenControlVisualizer[] touchScreenControlVisualizerArr) {
        this.touchAreas = touchAreaArr;
        if (touchScreenControlVisualizerArr == null) {
            touchScreenControlVisualizerArr = new PointerVisualizer[0];
        }
        this.visualizers = touchScreenControlVisualizerArr;
    }

    public void attachedToGLContext(SceneOfRectangles sceneOfRectangles) {
        for (TouchScreenControlVisualizer attachedToGLContext : this.visualizers) {
            attachedToGLContext.attachedToGLContext(sceneOfRectangles);
        }
    }

    public void detachedFromGLContext() {
        for (TouchScreenControlVisualizer detachedFromGLContext : this.visualizers) {
            detachedFromGLContext.detachedFromGLContext();
        }
    }

    public void handleFingerDown(Finger finger) {
        for (TouchArea handleFingerDown : this.touchAreas) {
            handleFingerDown.handleFingerDown(finger);
        }
    }

    public void handleFingerUp(Finger finger) {
        for (TouchArea handleFingerUp : this.touchAreas) {
            handleFingerUp.handleFingerUp(finger);
        }
    }

    public void handleFingerMove(Finger finger) {
        for (TouchArea handleFingerMove : this.touchAreas) {
            handleFingerMove.handleFingerMove(finger);
        }
    }
}
