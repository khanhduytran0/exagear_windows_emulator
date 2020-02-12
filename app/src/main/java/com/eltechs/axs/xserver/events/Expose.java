package com.eltechs.axs.xserver.events;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.xserver.Window;

public class Expose extends Event {
    private final int height;
    private final int width;
    private final Window window;
    private final int x;
    private final int y;

    public Expose(Window window2) {
        super(12);
        this.window = window2;
        Rectangle boundingRectangle = window2.getBoundingRectangle();
        this.y = 0;
        this.x = 0;
        this.width = boundingRectangle.width;
        this.height = boundingRectangle.height;
    }

    public Expose(Window window2, int i, int i2, int i3, int i4) {
        super(12);
        this.window = window2;
        this.x = i;
        this.y = i2;
        this.width = i3;
        this.height = i4;
    }

    public Window getWindow() {
        return this.window;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
