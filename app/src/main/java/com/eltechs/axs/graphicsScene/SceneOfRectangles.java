package com.eltechs.axs.graphicsScene;

import android.graphics.Bitmap;
import android.opengl.Matrix;
import com.eltechs.axs.annotations.UsedByNativeCode;
import com.eltechs.axs.geom.RectangleF;
import com.eltechs.axs.graphicsScene.impl.TextureManagersFactory;
import com.eltechs.axs.graphicsScene.impl.TexturesManager;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.GLHelpers;
import com.eltechs.axs.helpers.MathHelpers;
import com.eltechs.axs.helpers.ShaderHelpers;
import com.eltechs.axs.helpers.ShaderHelpers.ShaderType;
import com.eltechs.axs.xserver.impl.drawables.gl.PersistentGLDrawable;

public class SceneOfRectangles {
    private static final float DEPTH_OF_SCENE = 1024.0f;
    private final boolean have_GL_OES_texture_npot;
    private final float[] imageTransformationMatrix = new float[16];
    private final int nRectangles;
    private final int nTextures;
    @UsedByNativeCode
    private long sceneData;
    @UsedByNativeCode
    private int texturerDynamicAlpha;
    @UsedByNativeCode
    private int texturerStaticAlpha;
    private final TexturesManager texturesManager;
    private final float[] viewportAdjustmentMatrix = new float[16];

    private native void allocateNativeSceneData(int i);

    private native void freeNativeSceneData();

    private static native boolean initialiseNativeParts();

    private native void moveRectangleImpl(int i, float f, float f2, float f3, float f4, float f5);

    private native void placeRectangleImpl(int i, float f, float f2, float f3, float f4, float f5, int i2, float f6, float f7, float f8, boolean z);

    private native void setMVPMatrix(float[] fArr);

    public native synchronized void draw();

    static {
        System.loadLibrary("axs-helpers");
        Assert.state(initialiseNativeParts(), "Managed and native parts of SceneOfRectangles do not match one another.");
    }

    public SceneOfRectangles(int i, int i2) {
        this.nRectangles = i;
        this.nTextures = i2;
        this.have_GL_OES_texture_npot = GLHelpers.have_GL_OES_texture_npot();
        this.texturerStaticAlpha = createTexturer(true);
        this.texturerDynamicAlpha = createTexturer(false);
        this.texturesManager = TextureManagersFactory.createTexturesManager();
        Matrix.setIdentityM(this.imageTransformationMatrix, 0);
        Matrix.setIdentityM(this.viewportAdjustmentMatrix, 0);
        this.texturesManager.allocateTextures(i2);
        allocateNativeSceneData(i);
    }

    public void destroy() {
        this.texturesManager.freeTextures();
        freeNativeSceneData();
    }

    public synchronized void setViewport(float f, float f2, float f3, float f4) {
        synchronized (this) {
            try {
                Matrix.setIdentityM(this.viewportAdjustmentMatrix, 0);
                Matrix.translateM(this.viewportAdjustmentMatrix, 0, this.viewportAdjustmentMatrix, 0, (2.0f * f) - 1.0f, (-2.0f * f2) + 1.0f, 0.0f);
                Matrix.scaleM(this.viewportAdjustmentMatrix, 0, this.viewportAdjustmentMatrix, 0, f3, f4, 1.0f);
                Matrix.translateM(this.viewportAdjustmentMatrix, 0, this.viewportAdjustmentMatrix, 0, 1.0f, -1.0f, 0.0f);
                updateMVPMatrix();
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
    }

    public synchronized void setSceneViewport(float f, float f2, float f3, float f4) {
        Matrix.orthoM(this.imageTransformationMatrix, 0, f, f + f3, f2 - f4, f2, DEPTH_OF_SCENE, -1024.0f);
        updateMVPMatrix();
    }

    public synchronized void setSceneViewport(RectangleF rectangleF) {
        setSceneViewport(rectangleF.x, rectangleF.y, rectangleF.width, rectangleF.height);
    }

    private void updateMVPMatrix() {
        float[] fArr = new float[16];
        System.arraycopy(this.imageTransformationMatrix, 0, fArr, 0, 16);
        Matrix.multiplyMM(fArr, 0, this.viewportAdjustmentMatrix, 0, fArr, 0);
        setMVPMatrix(fArr);
    }

    public synchronized void setTextureSize(int i, int i2, int i3) {
        Assert.isTrue(i < this.nTextures, "Invalid texture number.");
        this.texturesManager.setTextureSize(i, i2, i3);
    }

    /* JADX INFO: finally extract failed */
    public synchronized void placeRectangle(int i, float f, float f2, float f3, float f4, float f5, int i2, float f6, boolean z) {
        float f7;
        float f8;
        int i3 = i2;
        synchronized (this) {
            try {
                int i4 = i;
                Assert.isTrue(i4 < this.nRectangles, "Invalid rectangle number");
                Assert.isTrue(i3 < this.nTextures, "Invalid texture number");
                if (this.have_GL_OES_texture_npot) {
                    f8 = 1.0f;
                    f7 = 1.0f;
                } else {
                    int[] iArr = new int[2];
                    this.texturesManager.getTextureSize(i3, iArr);
                    f7 = ((float) iArr[1]) / ((float) MathHelpers.upperPOT(iArr[1]));
                    f8 = ((float) iArr[0]) / ((float) MathHelpers.upperPOT(iArr[0]));
                }
                placeRectangleImpl(i4, f, f2, f3, f4, f5, this.texturesManager.getTextureId(i3), f8, f7, f6, z);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
    }

    public synchronized void moveRectangle(int i, float f, float f2, float f3, float f4, float f5) {
        Assert.isTrue(i < this.nRectangles, "Invalid rectangle number");
        moveRectangleImpl(i, f, f2, f3, f4, f5);
    }

    public synchronized void updateTextureFromDrawable(int i, PersistentGLDrawable persistentGLDrawable) {
        Assert.isTrue(i < this.nTextures, "Invalid texture number");
        this.texturesManager.updateTextureFromDrawable(i, persistentGLDrawable);
    }

    public synchronized void updateTextureFromBitmap(int i, Bitmap bitmap) {
        Assert.isTrue(i < this.nTextures, "Invalid texture number");
        this.texturesManager.updateTextureFromBitmap(i, bitmap);
    }

    public synchronized void setTextureFromBitmap(int i, Bitmap bitmap) {
        Assert.isTrue(i < this.nTextures, "Invalid texture number");
        this.texturesManager.setTextureSize(i, bitmap.getWidth(), bitmap.getHeight());
        this.texturesManager.updateTextureFromBitmap(i, bitmap);
    }

    private static int createTexturer(boolean z) {
        return ShaderHelpers.createAndLinkProgram(ShaderHelpers.compileShader(ShaderType.VERTEX, "uniform mat4 u_MVP;                                        \nattribute vec4 a_Position;                                 \nattribute vec2 a_TexCoordinate;                            \nvarying vec2 v_TexCoordinate;                              \nvoid main()                                                \n{                                                          \n   v_TexCoordinate = a_TexCoordinate;                      \n   gl_Position = u_MVP * a_Position;                       \n}                                                          \n"), ShaderHelpers.compileShader(ShaderType.FRAGMENT, z ? "precision mediump float;                                   \nuniform sampler2D u_Texture;                               \nuniform float u_Alpha;varying vec2 v_TexCoordinate;                              \nvoid main()                                                \n{                                                          \n   vec4 tc = texture2D(u_Texture, v_TexCoordinate);        \n   gl_FragColor = vec4(tc.rgb, u_Alpha);}                                                          \n" : "precision mediump float;                                   \nuniform sampler2D u_Texture;                               \nuniform float u_Alpha;varying vec2 v_TexCoordinate;                              \nvoid main()                                                \n{                                                          \n   vec4 tc = texture2D(u_Texture, v_TexCoordinate);        \n   gl_FragColor = vec4(tc.rgb, u_Alpha * tc.a);            \n}                                                          \n"), new String[]{"a_Position", "a_TexCoordinate"});
    }
}
