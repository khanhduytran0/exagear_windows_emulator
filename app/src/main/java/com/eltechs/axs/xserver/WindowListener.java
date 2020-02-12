package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.events.Event;
import com.eltechs.axs.xserver.impl.masks.Mask;

public interface WindowListener {
    Mask<EventName> getMask();

    boolean isInterestedIn(EventName eventName);

    boolean isInterestedIn(Mask<EventName> mask);

    void onEvent(Window window, Event event);
}
