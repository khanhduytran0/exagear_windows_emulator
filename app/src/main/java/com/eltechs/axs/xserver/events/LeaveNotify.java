package com.eltechs.axs.xserver.events;

import com.eltechs.axs.xserver.KeyButNames;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.events.PointerWindowEvent.Detail;
import com.eltechs.axs.xserver.events.PointerWindowEvent.Mode;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class LeaveNotify extends PointerWindowEvent {
    public LeaveNotify(Detail detail, Mode mode, int i, Window window, Window window2, Window window3, short s, short s2, short s3, short s4, Mask<KeyButNames> mask, boolean z) {
        super(8, detail, mode, i, window, window2, window3, s, s2, s3, s4, mask, z);
    }
}
