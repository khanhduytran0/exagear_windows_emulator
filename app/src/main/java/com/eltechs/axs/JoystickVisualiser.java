package com.eltechs.axs;

import android.graphics.Bitmap;
import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.graphicsScene.SceneOfRectangles;
import com.eltechs.axs.helpers.Assert;

public class JoystickVisualiser implements TouchScreenControlVisualizer, PointerEventListener {
    private final float alpha;
    private final boolean alwaysVisible;
    private final Bitmap bitmap;
    private float centerX;
    private float centerY;
    private float defaultX;
    private float defaultY;
    private transient SceneOfRectangles graphicsScene;
    private final float radius;
    private final int rectangleId;
    private final int textureId;
    private boolean visible = true;

    public JoystickVisualiser(float f, Bitmap bitmap2, float f2, boolean z, float f3, float f4, GraphicsSceneConfigurer graphicsSceneConfigurer) {
        Assert.notNull(bitmap2, "No button image specified");
        this.radius = f;
        this.bitmap = bitmap2;
        this.alpha = f2;
        this.alwaysVisible = z;
        this.defaultX = f3;
        this.defaultY = f4;
        this.rectangleId = graphicsSceneConfigurer.addRectangle();
        this.textureId = graphicsSceneConfigurer.addTexture();
        pointerExited(0.0f, 0.0f);
    }

    public void attachedToGLContext(SceneOfRectangles sceneOfRectangles) {
        this.graphicsScene = sceneOfRectangles;
        sceneOfRectangles.setTextureFromBitmap(this.textureId, this.bitmap);
        sceneOfRectangles.placeRectangle(this.rectangleId, this.centerX - this.radius, -(this.centerY - this.radius), 2.0f * this.radius, 2.0f * this.radius, -1.0f, this.textureId, this.alpha, false);
    }

    public void detachedFromGLContext() {
        this.graphicsScene = null;
    }

    public void pointerEntered(float f, float f2) {
        this.centerX = f;
        this.centerY = f2;
        this.visible = true;
        moveHolder();
    }

    public void pointerExited(float f, float f2) {
        if (!this.alwaysVisible) {
            this.visible = false;
        }
        this.centerX = this.defaultX;
        this.centerY = this.defaultY;
        moveHolder();
    }

    public void pointerMove(float f, float f2) {
        this.centerX = f;
        this.centerY = f2;
        moveHolder();
    }

    private void moveHolder() {
        if (this.graphicsScene != null) {
            this.graphicsScene.placeRectangle(this.rectangleId, this.centerX - this.radius, -(this.centerY - this.radius), this.radius * 2.0f, this.radius * 2.0f, -1.0f, this.textureId, this.alpha, false);
        }
    }
}
