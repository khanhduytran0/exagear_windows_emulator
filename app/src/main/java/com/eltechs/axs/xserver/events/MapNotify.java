package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Window;

public class MapNotify extends Event {
    private final Window mappedWindow;
    private final Window originatedAt;

    public MapNotify(Window window, Window window2) {
        super(19);
        this.originatedAt = window;
        this.mappedWindow = window2;
    }

    public Window getOriginatedAt() {
        return this.originatedAt;
    }

    public Window getMappedWindow() {
        return this.mappedWindow;
    }
}
