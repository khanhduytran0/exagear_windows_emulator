package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.client.XClientWindowListener;
import com.eltechs.axs.xserver.impl.masks.Mask;

public interface GrabsManager {
    void disableAutomaticOrPassiveGrab();

    void disablePointerGrab();

    XClientWindowListener getPointerGrabListener();

    boolean getPointerGrabOwnerEvents();

    int getPointerGrabTime();

    Window getPointerGrabWindow();

    void initiateActivePointerGrab(Window window, boolean z, Mask<EventName> mask, Cursor cursor, Window window2, DeviceGrabMode deviceGrabMode, DeviceGrabMode deviceGrabMode2, int i, XClient xClient);

    void initiateAutomaticPointerGrab(Window window);
}
