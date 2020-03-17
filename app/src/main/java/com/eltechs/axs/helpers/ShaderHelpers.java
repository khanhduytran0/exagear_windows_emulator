package com.eltechs.axs.helpers;

import android.opengl.GLES20;
import android.util.Log;

public abstract class ShaderHelpers {

    public enum ShaderType {
        VERTEX(35633),
        FRAGMENT(35632);
        
        /* access modifiers changed from: private */
        public final int glTypeName;

        private ShaderType(int i) {
            this.glTypeName = i;
        }
    }

    private ShaderHelpers() {
    }

    public static int compileShader(ShaderType shaderType, String str) {
        int glCreateShader = GLES20.glCreateShader(shaderType.glTypeName);
        if (glCreateShader == 0) {
            return 0;
        }
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        Log.e("ShaderHelpers", GLES20.glGetProgramInfoLog(glCreateShader));
        GLES20.glDeleteShader(glCreateShader);
        return 0;
    }

    public static int createAndLinkProgram(int i, int i2, String[] strArr) {
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram == 0) {
            return 0;
        }
        GLES20.glAttachShader(glCreateProgram, i);
        GLES20.glAttachShader(glCreateProgram, i2);
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateProgram;
        }
        GLES20.glDeleteProgram(glCreateProgram);
        return 0;
    }
}
