package com.eltechs.axs;

import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.graphicsScene.SceneOfRectangles;
import java.util.ArrayList;
import java.util.Collection;

public class TouchScreenControls {
    private SceneOfRectangles graphicsScene;
    private GraphicsSceneConfigurer graphicsSceneConfigurer;
    private final Collection<TouchScreenControl> touchScreenControls = new ArrayList();

    public TouchScreenControls(GraphicsSceneConfigurer graphicsSceneConfigurer2) {
        this.graphicsSceneConfigurer = graphicsSceneConfigurer2;
    }

    public void add(TouchScreenControl touchScreenControl) {
        this.touchScreenControls.add(touchScreenControl);
    }

    public void attachedToGLContext() {
        this.graphicsScene = this.graphicsSceneConfigurer.createScene();
        for (TouchScreenControl attachedToGLContext : this.touchScreenControls) {
            attachedToGLContext.attachedToGLContext(this.graphicsScene);
        }
    }

    public void detachedFromGLContext() {
        if (this.graphicsScene != null) {
            for (TouchScreenControl detachedFromGLContext : this.touchScreenControls) {
                detachedFromGLContext.detachedFromGLContext();
            }
            this.graphicsScene.destroy();
            this.graphicsScene = null;
        }
    }

    public void draw() {
        if (this.graphicsScene != null) {
            this.graphicsScene.draw();
        }
    }

    public void handleFingerDown(Finger finger) {
        for (TouchScreenControl handleFingerDown : this.touchScreenControls) {
            handleFingerDown.handleFingerDown(finger);
        }
    }

    public void handleFingerUp(Finger finger) {
        for (TouchScreenControl handleFingerUp : this.touchScreenControls) {
            handleFingerUp.handleFingerUp(finger);
        }
    }

    public void handleFingerMove(Finger finger) {
        for (TouchScreenControl handleFingerMove : this.touchScreenControls) {
            handleFingerMove.handleFingerMove(finger);
        }
    }
}
