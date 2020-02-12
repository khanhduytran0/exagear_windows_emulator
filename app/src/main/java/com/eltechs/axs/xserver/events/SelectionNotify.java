package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;

public class SelectionNotify extends Event {
    private final Atom property;
    private final Window requestor;
    private final Atom selection;
    private final Atom target;
    private final int timestamp;

    public SelectionNotify(int i, Window window, Atom atom, Atom atom2, Atom atom3) {
        super(31);
        this.timestamp = i;
        this.requestor = window;
        this.selection = atom;
        this.target = atom2;
        this.property = atom3;
    }

    public int getTimestamp() {
        return this.timestamp;
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
