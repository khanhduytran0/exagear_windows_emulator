package com.eltechs.axs.xserver.client;

import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowListener;
import com.eltechs.axs.xserver.events.Event;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class XClientWindowListener implements WindowListener {
    private final XClient client;
    private final Mask<EventName> eventMask;

    private boolean isInterestedIn(Class<? extends Event> cls) {
        return true;
    }

    public XClientWindowListener(XClient xClient, Mask<EventName> mask) {
        this.eventMask = mask;
        this.client = xClient;
    }

    public void onEvent(Window window, Event event) {
        if (isInterestedIn(event.getClass())) {
            this.client.createEventSender().sendEvent(event);
        }
    }

    public boolean isInterestedIn(EventName eventName) {
        return this.eventMask.isSet(eventName);
    }

    public boolean isInterestedIn(Mask<EventName> mask) {
        return this.eventMask.intersects(mask);
    }

    public Mask<EventName> getMask() {
        return this.eventMask;
    }

    public XClient getClient() {
        return this.client;
    }
}
