package com.eltechs.axs.widgets.touchScreenControlsOverlay;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import com.eltechs.axs.TouchScreenControls;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TouchScreenControlsRenderer implements Renderer {
    private TouchScreenControls controls;
    private TouchScreenControls replacementControls;

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(2884);
        GLES20.glEnable(2929);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
    }

    public synchronized void onDrawFrame(GL10 gl10) {
        if (this.replacementControls != null) {
            switchControls();
        }
        if (this.controls != null) {
            GLES20.glClear(16640);
            synchronized (this.controls) {
                this.controls.draw();
            }
        }
    }

    public synchronized void setTouchScreenControls(TouchScreenControls touchScreenControls) {
        this.replacementControls = touchScreenControls;
    }

    public synchronized void onPause() {
        if (this.controls != null) {
            this.controls.detachedFromGLContext();
            this.controls = null;
        }
    }

    private void switchControls() {
        if (this.controls != null) {
            this.controls.detachedFromGLContext();
        }
        this.controls = this.replacementControls;
        this.controls.attachedToGLContext();
        this.replacementControls = null;
    }
}
