package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;

public class SelectionClear extends Event {
    private final Window owner;
    private final Atom selection;
    private final int timestamp;

    public SelectionClear(int i, Window window, Atom atom) {
        super(29);
        this.timestamp = i;
        this.owner = window;
        this.selection = atom;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public Window getOwner() {
        return this.owner;
    }

    public Atom getSelection() {
        return this.selection;
    }
}
