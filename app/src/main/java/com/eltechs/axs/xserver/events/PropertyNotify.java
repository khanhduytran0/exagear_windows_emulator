package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;

public class PropertyNotify extends Event {
    private final boolean delete;
    private final Atom name;
    private final int timestamp;
    private final Window window;

    public PropertyNotify(Window window2, Atom atom, int i, boolean z) {
        super(28);
        this.window = window2;
        this.name = atom;
        this.timestamp = i;
        this.delete = z;
    }

    public Window getWindow() {
        return this.window;
    }

    public Atom getName() {
        return this.name;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public boolean isDelete() {
        return this.delete;
    }
}
