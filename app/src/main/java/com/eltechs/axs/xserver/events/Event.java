package com.eltechs.axs.xserver.events;

public abstract class Event {
    private final int id;

    protected Event(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }
}
