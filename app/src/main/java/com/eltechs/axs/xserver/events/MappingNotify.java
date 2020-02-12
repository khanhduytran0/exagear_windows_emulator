package com.eltechs.axs.xserver.events;

public class MappingNotify extends Event {
    private final int count;
    private final int first_keycode;
    private final Request request;

    public enum Request {
        MODIFIER,
        KEYBOARD,
        POINTER
    }

    public MappingNotify(Request request2, int i, int i2) {
        super(34);
        this.request = request2;
        this.first_keycode = i;
        this.count = i2;
    }

    public Request getRequest() {
        return this.request;
    }

    public int getFirstKeycode() {
        return this.first_keycode;
    }

    public int getCount() {
        return this.count;
    }
}
