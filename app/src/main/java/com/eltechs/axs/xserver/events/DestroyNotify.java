package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Window;

public class DestroyNotify extends Event {
    private final Window deletedWindow;
    private final Window originatedAt;

    public DestroyNotify(Window window, Window window2) {
        super(17);
        this.originatedAt = window;
        this.deletedWindow = window2;
    }

    public Window getOriginatedAt() {
        return this.originatedAt;
    }

    public Window getDeletedWindow() {
        return this.deletedWindow;
    }
}
