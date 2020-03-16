package com.eltechs.axs;

import android.graphics.Bitmap;
import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.graphicsScene.SceneOfRectangles;
import com.eltechs.axs.helpers.Assert;

public class WheelJoystickVisualizer implements TouchScreenControlVisualizer, WheelEventListener {
    private final float alpha;
    private final Bitmap bitmap;
    private final float coordX;
    private final float coordY;
    private final float radius;
    private final int rectangleId;
    private final int textureId;

    public void detachedFromGLContext() {
    }

    public void turnedAntiClockwise(double d) {
    }

    public void turnedClockwise(double d) {
    }

    public WheelJoystickVisualizer(float f, float f2, float f3, Bitmap bitmap2, float f4, GraphicsSceneConfigurer graphicsSceneConfigurer) {
        Assert.notNull(bitmap2, "No button image specified");
        this.coordX = f;
        this.coordY = f2;
        this.radius = f3;
        this.bitmap = bitmap2;
        this.alpha = f4;
        this.rectangleId = graphicsSceneConfigurer.addRectangle();
        this.textureId = graphicsSceneConfigurer.addTexture();
    }

    public void attachedToGLContext(SceneOfRectangles sceneOfRectangles) {
        sceneOfRectangles.setTextureFromBitmap(this.textureId, this.bitmap);
        sceneOfRectangles.placeRectangle(this.rectangleId, this.coordX - this.radius, this.coordY - this.radius, 2.0f * this.radius, 2.0f * this.radius, -1.0f, this.textureId, this.alpha, false);
    }
}
