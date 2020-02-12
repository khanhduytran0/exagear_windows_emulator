package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;

public class SelectionRequest extends Event {
    private final Window owner;
    private final Atom property;
    private final Window requestor;
    private final Atom selection;
    private final Atom target;
    private final int timestamp;

    public SelectionRequest(int i, Window window, Window window2, Atom atom, Atom atom2, Atom atom3) {
        super(30);
        this.timestamp = i;
        this.owner = window;
        this.requestor = window2;
        this.selection = atom;
        this.target = atom2;
        this.property = atom3;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public Window getOwner() {
        return this.owner;
    }

    public Window getRequestor() {
        return this.requestor;
    }

    public Atom getSelection() {
        return this.selection;
    }

    public Atom getTarget() {
        return this.target;
    }

    public Atom getProperty() {
        return this.property;
    }
}
