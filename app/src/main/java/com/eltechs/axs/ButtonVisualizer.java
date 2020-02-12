package com.eltechs.axs;

import android.graphics.Bitmap;
import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.graphicsScene.SceneOfRectangles;
import com.eltechs.axs.helpers.Assert;

public class ButtonVisualizer implements ButtonEventListener, TouchScreenControlVisualizer {
    private final float alpha;
    private final Bitmap bitmap;
    private final float bottomX;
    private final float bottomY;
    private boolean isActive = false;
    private final int rectangleId;
    private final int textureId;
    private final float topX;
    private final float topY;

    public void detachedFromGLContext() {
    }

    public ButtonVisualizer(float f, float f2, float f3, float f4, Bitmap bitmap2, float f5, GraphicsSceneConfigurer graphicsSceneConfigurer) {
        Assert.notNull(bitmap2, "No button image specified");
        this.topX = f;
        this.topY = f2;
        this.bottomX = f3;
        this.bottomY = f4;
        this.bitmap = bitmap2;
        this.alpha = f5;
        this.rectangleId = graphicsSceneConfigurer.addRectangle();
        this.textureId = graphicsSceneConfigurer.addTexture();
    }

    public void pressed() {
        this.isActive = true;
    }

    public void released() {
        this.isActive = false;
    }

    public void attachedToGLContext(SceneOfRectangles sceneOfRectangles) {
        sceneOfRectangles.setTextureFromBitmap(this.textureId, this.bitmap);
        sceneOfRectangles.placeRectangle(this.rectangleId, this.topX, -this.topY, this.bottomX - this.topX, this.bottomY - this.topY, -1.0f, this.textureId, this.alpha, false);
    }
}
