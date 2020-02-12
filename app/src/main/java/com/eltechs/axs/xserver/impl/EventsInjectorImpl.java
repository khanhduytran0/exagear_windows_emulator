package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.EventsInjector;
import com.eltechs.axs.xserver.XServer;

public class EventsInjectorImpl implements EventsInjector {
    private final XServer xServer;

    public EventsInjectorImpl(XServer xServer2) {
        this.xServer = xServer2;
    }

    public void injectKeyPress(byte b, int i) {
        this.xServer.getKeyboard().keyPressed(b, i);
    }

    public void injectKeyRelease(byte b, int i) {
        this.xServer.getKeyboard().keyReleased(b, i);
    }

    public void injectPointerMove(int i, int i2) {
        this.xServer.getPointer().setCoordinates(i, i2);
    }

    public void injectPointerButtonPress(int i) {
        this.xServer.getPointer().setButton(i, true);
    }

    public void injectPointerButtonRelease(int i) {
        this.xServer.getPointer().setButton(i, false);
    }
}
