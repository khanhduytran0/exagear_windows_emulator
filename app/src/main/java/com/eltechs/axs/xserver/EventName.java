package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.FlagsEnum;

public enum EventName implements FlagsEnum {
    KEY_PRESS(1),
    KEY_RELEASE(2),
    BUTTON_PRESS(4),
    BUTTON_RELEASE(8),
    ENTER_WINDOW(16),
    LEAVE_WINDOW(32),
    POINTER_MOTION(64),
    POINTER_MOTION_HINT(128),
    BUTTON_1_MOTION(256),
    BUTTON_2_MOTION(512),
    BUTTON_3_MOTION(1024),
    BUTTON_4_MOTION(2048),
    BUTTON_5_MOTION(4096),
    BUTTON_MOTION(8192),
    KEYMAP_STATE(16384),
    EXPOSURE(32768),
    VISIBILITY_CHANGE(65536),
    STRUCTURE_NOTIFY(131072),
    RESIZE_REDIRECT(262144),
    SUBSTRUCTURE_NOTIFY(524288),
    SUBSTRUCTURE_REDIRECT(1048576),
    FOCUS_CHANGE(2097152),
    PROPERTY_CHANGE(4194304),
    COLORMAP_CHANGE(8388608),
    OWNER_GRAB_BUTTON(16777216);
    
    private final int id;

    private EventName(int i) {
        this.id = i;
    }

    public int flagValue() {
        return this.id;
    }
}
