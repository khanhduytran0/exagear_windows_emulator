package com.eltechs.axs.xserver.events;

public abstract class CoreEventCodes {
    public static final int BUTTON_PRESS = 4;
    public static final int BUTTON_RELEASE = 5;
    public static final int CIRCULATE_NOTIFY = 26;
    public static final int CIRCULATE_REQUEST = 27;
    public static final int CLIENT_MESSAGE = 33;
    public static final int COLORMAP_NOTIFY = 32;
    public static final int CONFIGURE_NOTIFY = 22;
    public static final int CONFIGURE_REQUEST = 23;
    public static final int CREATE_NOTIFY = 16;
    public static final int DESTROY_NOTIFY = 17;
    public static final int ENTER_NOTIFY = 7;
    public static final int EXPOSE = 12;
    public static final int FOCUS_IN = 9;
    public static final int FOCUS_OUT = 10;
    public static final int GRAPHICS_EXPOSURE = 13;
    public static final int GRAVITY_NOTIFY = 24;
    public static final int KEYMAP_NOTIFY = 11;
    public static final int KEY_PRESS = 2;
    public static final int KEY_RELEASE = 3;
    public static final int LEAVE_NOTIFY = 8;
    public static final int MAPPING_NOTIFY = 34;
    public static final int MAP_NOTIFY = 19;
    public static final int MAP_REQUEST = 20;
    public static final int MAX_EVENT = 34;
    public static final int MIN_EVENT = 2;
    public static final int MOTION_NOTIFY = 6;
    public static final int NO_EXPOSURE = 14;
    public static final int PROPERTY_NOTIFY = 28;
    public static final int REPARENT_NOTIFY = 21;
    public static final int RESIZE_REQUEST = 25;
    public static final int SELECTION_CLEAR = 29;
    public static final int SELECTION_NOTIFY = 31;
    public static final int SELECTION_REQUEST = 30;
    public static final int UNMAP_NOTIFY = 18;
    public static final int VISIBILITY_NOTIFY = 15;

    private CoreEventCodes() {
    }
}
