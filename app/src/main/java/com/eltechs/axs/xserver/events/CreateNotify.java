package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Window;

public class CreateNotify extends Event {
    private final Window parent;
    private final Window window;

    public CreateNotify(Window window2, Window window3) {
        super(16);
        this.parent = window2;
        this.window = window3;
    }

    public Window getParent() {
        return this.parent;
    }

    public Window getWindow() {
        return this.window;
    }
}
