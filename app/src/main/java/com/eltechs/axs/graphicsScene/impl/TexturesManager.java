package com.eltechs.axs.graphicsScene.impl;

import android.graphics.Bitmap;
import com.eltechs.axs.xserver.impl.drawables.gl.PersistentGLDrawable;

public interface TexturesManager {
    boolean allocateTextures(int i);

    void freeTextures();

    int getTextureId(int i);

    void getTextureSize(int i, int[] iArr);

    boolean setTextureSize(int i, int i2, int i3);

    void updateTextureFromBitmap(int i, Bitmap bitmap);

    void updateTextureFromDrawable(int i, PersistentGLDrawable persistentGLDrawable);
}
