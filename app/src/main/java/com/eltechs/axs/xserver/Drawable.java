package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.drawables.Visual;

public interface Drawable {

    public interface ModificationListener {
        void changed(int i, int i2, int i3, int i4);
    }

    int getHeight();

    int getId();

    Painter getPainter();

    Window getRoot();

    Visual getVisual();

    int getWidth();

    void installModificationListener(ModificationListener modificationListener);
}
