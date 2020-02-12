package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Window;

public class MapRequest extends Event {
    private final Window mappedWindow;
    private final Window parentWindow;

    public MapRequest(Window window, Window window2) {
        super(20);
        this.parentWindow = window;
        this.mappedWindow = window2;
    }

    public Window getParentWindow() {
        return this.parentWindow;
    }

    public Window getMappedWindow() {
        return this.mappedWindow;
    }
}
