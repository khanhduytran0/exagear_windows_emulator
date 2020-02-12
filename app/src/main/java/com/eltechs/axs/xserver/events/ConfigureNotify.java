package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Window;

public class ConfigureNotify extends Event {
    private final Window aboveSibling;
    private final int borderWidth;
    private final Window event;
    private final int height;
    private final boolean overrideRedirect;
    private final int width;
    private final Window window;
    private final int x;
    private final int y;

    public ConfigureNotify(Window window2, Window window3, Window window4, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(22);
        this.event = window2;
        this.window = window3;
        this.aboveSibling = window4;
        this.x = i;
        this.y = i2;
        this.width = i3;
        this.height = i4;
        this.borderWidth = i5;
        this.overrideRedirect = z;
    }

    public Window getEvent() {
        return this.event;
    }

    public Window getWindow() {
        return this.window;
    }

    public Window getAboveSibling() {
        return this.aboveSibling;
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

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public boolean isOverrideRedirect() {
        return this.overrideRedirect;
    }
}
