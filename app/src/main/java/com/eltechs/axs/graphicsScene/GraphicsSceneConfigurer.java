package com.eltechs.axs.graphicsScene;

import com.eltechs.axs.geom.RectangleF;

public class GraphicsSceneConfigurer {
    private int nRectangles;
    private int nTextures;
    private RectangleF sceneViewport = new RectangleF(0.0f, 0.0f, 0.0f, 0.0f);

    public void setSceneViewport(RectangleF rectangleF) {
        this.sceneViewport = rectangleF;
    }

    public void setSceneViewport(float f, float f2, float f3, float f4) {
        this.sceneViewport = new RectangleF(f, f2, f3, f4);
    }

    public int addRectangle() {
        int i = this.nRectangles;
        this.nRectangles = i + 1;
        return i;
    }

    public int addTexture() {
        int i = this.nTextures;
        this.nTextures = i + 1;
        return i;
    }

    public SceneOfRectangles createScene() {
        SceneOfRectangles sceneOfRectangles = new SceneOfRectangles(this.nRectangles, this.nTextures);
        sceneOfRectangles.setSceneViewport(this.sceneViewport.x, this.sceneViewport.y, this.sceneViewport.width, this.sceneViewport.height);
        return sceneOfRectangles;
    }
}
