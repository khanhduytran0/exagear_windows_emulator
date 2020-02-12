package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.client.XClientWindowListener;
import com.eltechs.axs.xserver.events.Event;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.ArrayList;
import java.util.Collection;

public class WindowListenersList {
    private final Window host;
    private final Collection<WindowListener> listeners = new ArrayList();

    public WindowListenersList(Window window) {
        this.host = window;
    }

    public void addListener(WindowListener windowListener) {
        this.listeners.add(windowListener);
    }

    public void removeListener(WindowListener windowListener) {
        this.listeners.remove(windowListener);
    }

    public void sendEvent(Event event) {
        for (WindowListener onEvent : this.listeners) {
            onEvent.onEvent(this.host, event);
        }
    }

    public void sendEventForEventName(Event event, EventName eventName) {
        for (WindowListener windowListener : this.listeners) {
            if (windowListener.isInterestedIn(eventName)) {
                windowListener.onEvent(this.host, event);
            }
        }
    }

    public void sendEventForEventMask(Event event, Mask<EventName> mask) {
        for (WindowListener windowListener : this.listeners) {
            if (windowListener.isInterestedIn(mask)) {
                windowListener.onEvent(this.host, event);
            }
        }
    }

    public void sendEventForEventNameToClient(Event event, EventName eventName, XClient xClient) {
        for (WindowListener windowListener : this.listeners) {
            if (windowListener.isInterestedIn(eventName) && (windowListener instanceof XClientWindowListener) && ((XClientWindowListener) windowListener).getClient() == xClient) {
                windowListener.onEvent(this.host, event);
            }
        }
    }

    public void sendEventForEventMaskToClient(Event event, Mask<EventName> mask, XClient xClient) {
        for (WindowListener windowListener : this.listeners) {
            if (windowListener.isInterestedIn(mask) && (windowListener instanceof XClientWindowListener) && ((XClientWindowListener) windowListener).getClient() == xClient) {
                windowListener.onEvent(this.host, event);
            }
        }
    }

    public boolean isListenerInstalledForEvent(EventName eventName) {
        for (WindowListener isInterestedIn : this.listeners) {
            if (isInterestedIn.isInterestedIn(eventName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isListenerInstalledForEventMask(Mask<EventName> mask) {
        for (WindowListener isInterestedIn : this.listeners) {
            if (isInterestedIn.isInterestedIn(mask)) {
                return true;
            }
        }
        return false;
    }

    public boolean isListenerInstalledForEventForClient(EventName eventName, XClient xClient) {
        for (WindowListener windowListener : this.listeners) {
            if (windowListener.isInterestedIn(eventName) && (windowListener instanceof XClientWindowListener) && ((XClientWindowListener) windowListener).getClient() == xClient) {
                return true;
            }
        }
        return false;
    }

    public boolean isListenerInstalledForEventMaskForClient(Mask<EventName> mask, XClient xClient) {
        for (WindowListener windowListener : this.listeners) {
            if (windowListener.isInterestedIn(mask) && (windowListener instanceof XClientWindowListener) && ((XClientWindowListener) windowListener).getClient() == xClient) {
                return true;
            }
        }
        return false;
    }

    public Mask<EventName> calculateAllEventsMask() {
        Mask<EventName> emptyMask = Mask.emptyMask(EventName.class);
        for (WindowListener mask : this.listeners) {
            emptyMask.joinWith(mask.getMask());
        }
        return emptyMask;
    }

    public XClientWindowListener getButtonPressListener() {
        for (WindowListener windowListener : this.listeners) {
            if (windowListener.isInterestedIn(EventName.BUTTON_PRESS) && (windowListener instanceof XClientWindowListener)) {
                return (XClientWindowListener) windowListener;
            }
        }
        return null;
    }
}
