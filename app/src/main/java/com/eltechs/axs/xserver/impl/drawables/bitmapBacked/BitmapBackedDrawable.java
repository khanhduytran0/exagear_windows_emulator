package com.eltechs.axs.xserver.impl.drawables.bitmapBacked;

import android.graphics.Bitmap;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Drawable.ModificationListener;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.impl.drawables.Visual;

public class BitmapBackedDrawable implements Drawable {
    private final Bitmap content;
    private final int height;
    private final int id;
    private final PainterOnBitmap painter;
    private final Window rootWindow;
    private final Visual visual;
    private final int width;

    public BitmapBackedDrawable(int i, Window window, Bitmap bitmap, int i2, int i3, Visual visual2) {
        Assert.isTrue(i2 <= bitmap.getWidth() && i3 <= bitmap.getHeight(), "Bitmap smaller than the Drawable");
        this.id = i;
        this.rootWindow = window;
        this.visual = visual2;
        this.content = bitmap;
        this.width = i2;
        this.height = i3;
        this.painter = new PainterOnBitmap(this.content, this, i2, i3);
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

    public Bitmap getContent() {
        return this.content;
    }
}
