package com.eltechs.axs.xserver.impl.drawables.gl;

public class PersistentGLDrawableDestroyer implements Runnable {
    private final long content;

    public native void destroyDrawable(long j);

    static {
        System.loadLibrary("axs-helpers");
    }

    public PersistentGLDrawableDestroyer(PersistentGLDrawable persistentGLDrawable) {
        this.content = persistentGLDrawable.getContent();
    }

    public void run() {
        destroyDrawable(this.content);
    }
}
