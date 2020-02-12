package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Window;

public class UnmapNotify extends Event {
    private final boolean fromConfigure;
    private final Window originatedAt;
    private final Window unmappedWindow;

    public UnmapNotify(Window window, Window window2, boolean z) {
        super(18);
        this.originatedAt = window;
        this.unmappedWindow = window2;
        this.fromConfigure = z;
    }

    public Window getOriginatedAt() {
        return this.originatedAt;
    }

    public Window getUnmappedWindow() {
        return this.unmappedWindow;
    }

    public boolean isFromConfigure() {
        return this.fromConfigure;
    }
}
