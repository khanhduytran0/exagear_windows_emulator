package com.eltechs.axs.helpers;

import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;

public abstract class GLHelpers {
    private static Boolean have_GL_OES_texture_npot;

    private GLHelpers() {
    }

    public static synchronized boolean have_GL_OES_texture_npot() {
        boolean booleanValue;
        synchronized (GLHelpers.class) {
            if (have_GL_OES_texture_npot == null) {
                have_GL_OES_texture_npot = Boolean.valueOf(isGLExtensionAvailable("GL_OES_texture_npot"));
            }
            booleanValue = have_GL_OES_texture_npot.booleanValue();
        }
        return booleanValue;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0025, code lost:
        return false;
     */
    public static synchronized boolean isGLExtensionAvailable(String str) {
        synchronized (GLHelpers.class) {
            String glGetString = GLES20.glGetString(7939);
            if (glGetString != null) {
                for (String equals : glGetString.split(" ")) {
                    if (equals.equals(str)) {
                        return true;
                    }
                }
            }
        }
		return false;
	}

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        return false;
     */
    public static synchronized boolean isEGLExtensionAvailable(String str) {
        synchronized (GLHelpers.class) {
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            String eglQueryString = egl10.eglQueryString(egl10.eglGetCurrentDisplay(), 12373);
            if (eglQueryString != null) {
                for (String equals : eglQueryString.split(" ")) {
                    if (equals.equals(str)) {
                        return true;
                    }
                }
            }
        }
		return false;
	}
}
