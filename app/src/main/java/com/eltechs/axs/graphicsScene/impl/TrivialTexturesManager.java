package com.eltechs.axs.graphicsScene.impl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.GLHelpers;
import com.eltechs.axs.helpers.MathHelpers;
import com.eltechs.axs.xserver.impl.drawables.gl.PersistentGLDrawable;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TrivialTexturesManager implements TexturesManager {
    private int[] textureSizes;
    private int[] textures;

    private native void setTextureFromDrawableImpl15(int i, int i2, int i3, long j);

    private native void setTextureFromDrawableImpl16(int i, int i2, int i3, long j);

    private native void setTextureFromDrawableImpl32(int i, int i2, int i3, long j);

    static {
        System.loadLibrary("axs-helpers");
    }

    public boolean allocateTextures(int i) {
        freeTextures();
        this.textures = new int[i];
        this.textureSizes = new int[(i * 2)];
        if (i != 0) {
            GLES20.glGenTextures(i, this.textures, 0);
        }
        return true;
    }

    public void freeTextures() {
        if (this.textures != null) {
            GLES20.glDeleteTextures(this.textures.length, this.textures, 0);
            this.textures = null;
        }
    }

    public boolean setTextureSize(int i, int i2, int i3) {
        int i4;
        int i5;
        if (GLHelpers.have_GL_OES_texture_npot()) {
            i5 = i2;
            i4 = i3;
        } else {
            i5 = MathHelpers.upperPOT(i2);
            i4 = MathHelpers.upperPOT(i3);
        }
        GLES20.glBindTexture(3553, this.textures[i]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, TarConstants.DEFAULT_BLKSIZE, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexImage2D(3553, 0, 6408, i5, i4, 0, 6408, 5121, null);
        int i6 = 2 * i;
        this.textureSizes[i6 + 0] = i2;
        this.textureSizes[i6 + 1] = i3;
        return true;
    }

    public void getTextureSize(int i, int[] iArr) {
        int i2 = 2 * i;
        iArr[0] = this.textureSizes[i2 + 0];
        iArr[1] = this.textureSizes[i2 + 1];
    }

    public int getTextureId(int i) {
        return this.textures[i];
    }

    public void updateTextureFromDrawable(int i, PersistentGLDrawable persistentGLDrawable) {
        int depth = persistentGLDrawable.getVisual().getDepth();
        if (depth == 1 || depth == 32) {
            setTextureFromDrawableImpl32(this.textures[i], persistentGLDrawable.getWidth(), persistentGLDrawable.getHeight(), persistentGLDrawable.getContent());
            return;
        }
        switch (depth) {
            case 15:
                setTextureFromDrawableImpl15(this.textures[i], persistentGLDrawable.getWidth(), persistentGLDrawable.getHeight(), persistentGLDrawable.getContent());
                return;
            case 16:
                setTextureFromDrawableImpl16(this.textures[i], persistentGLDrawable.getWidth(), persistentGLDrawable.getHeight(), persistentGLDrawable.getContent());
                return;
            default:
                Assert.notImplementedYet(String.format("Unsupported depth %s.", new Object[]{Integer.valueOf(persistentGLDrawable.getVisual().getDepth())}));
                return;
        }
    }

    public void updateTextureFromBitmap(int i, Bitmap bitmap) {
        GLES20.glBindTexture(3553, this.textures[i]);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
    }
}
