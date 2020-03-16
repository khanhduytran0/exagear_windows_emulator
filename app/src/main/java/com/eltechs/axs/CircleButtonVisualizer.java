package com.eltechs.axs;

import android.graphics.Bitmap;
import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.graphicsScene.SceneOfRectangles;
import com.eltechs.axs.helpers.Assert;

public class CircleButtonVisualizer implements ButtonEventListener, TouchScreenControlVisualizer {
    private final float alpha;
    private final Bitmap bitmap;
    private final float centerX;
    private final float centerY;
    private boolean isActive = false;
    private final float radius;
    private final int rectangleId;
    private final int textureId;

    public void detachedFromGLContext() {
    }

    public CircleButtonVisualizer(float f, float f2, float f3, Bitmap bitmap2, float f4, GraphicsSceneConfigurer graphicsSceneConfigurer) {
        Assert.notNull(bitmap2, "No button image specified");
        this.centerX = f;
        this.centerY = f2;
        this.radius = f3;
        this.bitmap = bitmap2;
        this.alpha = f4;
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
        sceneOfRectangles.placeRectangle(this.rectangleId, this.centerX - this.radius, -(this.centerY - this.radius), 2.0f * this.radius, 2.0f * this.radius, -2.0f, this.textureId, this.alpha, false);
    }
}
