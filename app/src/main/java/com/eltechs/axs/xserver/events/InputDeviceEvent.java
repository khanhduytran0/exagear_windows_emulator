package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.KeyButNames;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.impl.masks.Mask;

public abstract class InputDeviceEvent extends Event {
    private final Window child;
    private final byte detail;
    private final Window event;
    private final short eventX;
    private final short eventY;
    private final Window root;
    private final short rootX;
    private final short rootY;
    private final Mask<KeyButNames> state;
    private final int timestamp;

    protected InputDeviceEvent(int i, byte b, int i2, Window window, Window window2, Window window3, short s, short s2, short s3, short s4, Mask<KeyButNames> mask) {
        super(i);
        this.detail = b;
        this.timestamp = i2;
        this.root = window;
        this.event = window2;
        this.child = window3;
        this.rootX = s;
        this.rootY = s2;
        this.eventX = s3;
        this.eventY = s4;
        this.state = mask;
    }

    public byte getDetail() {
        return this.detail;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public Window getRoot() {
        return this.root;
    }

    public Window getEvent() {
        return this.event;
    }

    public Window getChild() {
        return this.child;
    }

    public short getRootX() {
        return this.rootX;
    }

    public short getRootY() {
        return this.rootY;
    }

    public short getEventX() {
        return this.eventX;
    }

    public short getEventY() {
        return this.eventY;
    }

    public Mask<KeyButNames> getState() {
        return this.state;
    }
}
