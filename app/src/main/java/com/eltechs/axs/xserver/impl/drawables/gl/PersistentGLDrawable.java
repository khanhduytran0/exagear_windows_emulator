package com.eltechs.axs.xserver.impl.drawables.gl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Drawable.ModificationListener;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.impl.drawables.Visual;

public class PersistentGLDrawable implements Drawable {
    private final long content;
    private final int height;
    private final int id;
    private final PainterOnPersistentGLDrawable painter;
    private final Window rootWindow;
    private final Visual visual;
    private final int width;

    private static native long allocateNativeStorage(int i, int i2);

    static {
        System.loadLibrary("axs-helpers");
    }

    public PersistentGLDrawable(int i, Window window, int i2, int i3, Visual visual2) {
        Assert.isTrue(i2 >= 0 && i3 >= 0, "Dimensions of a Drawable must be non-negative.");
        this.id = i;
        this.rootWindow = window;
        this.visual = visual2;
        this.content = allocateNativeStorage(i2, i3);
        this.width = i2;
        this.height = i3;
        this.painter = new PainterOnPersistentGLDrawable(this);
        this.painter.setModificationListener(new ModificationListener() {
            public void changed(int i, int i2, int i3, int i4) {
            }
        });
    }

    public int getId() {
        return this.id;
    }

    public Window getRoot() {
        return this.rootWindow;
    }

    public Visual getVisual() {
        return this.visual;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Painter getPainter() {
        return this.painter;
    }

    public void installModificationListener(ModificationListener modificationListener) {
        this.painter.setModificationListener(modificationListener);
    }

    public long getContent() {
        return this.content;
    }
}
