package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.GiantLocked;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.events.Event;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class EventsRelatedRequests extends HandlerObjectBase {
    public EventsRelatedRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 25)
    @GiantLocked
    public void SendEvent(XResponse xResponse, @OOBParam @RequestParam boolean z, @RequestParam int i, @RequestParam Mask<EventName> mask, @RequestParam Event event) throws XProtocolError {
        boolean z2 = true;
        if (i == 0 || i == 1) {
            z2 = false;
        }
        Assert.state(z2);
        Window window = this.xServer.getWindowsManager().getWindow(i);
        if (mask.isEmpty()) {
            window.getCreator().createEventSender().sendEvent(event);
        } else {
            window.getEventListenersList().sendEventForEventMask(event, mask);
        }
    }
}
