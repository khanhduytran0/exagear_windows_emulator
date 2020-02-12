package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.DeviceGrabMode;
import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.GrabsManager;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.client.XClientWindowListener;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import com.eltechs.axs.xserver.helpers.WindowHelpers.MapState;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class GrabsManagerImpl implements GrabsManager, WindowLifecycleListener {
    private Cursor grabCursor = null;
    private int lastPointerGrabTime = 0;
    private XClientWindowListener pointerGrabListener = null;
    private boolean pointerGrabOwnerEvents;
    private boolean pointerGrabReleaseWithButtons;
    private Window pointerGrabWindow = null;
    private final XServer xServer;

    public void windowCreated(Window window) {
    }

    public void windowDestroyed(Window window) {
    }

    public void windowMapped(Window window) {
    }

    public void windowZOrderChange(Window window) {
    }

    public GrabsManagerImpl(XServer xServer2) {
        this.xServer = xServer2;
        this.xServer.getWindowsManager().addWindowLifecycleListener(this);
    }

    private void initiateActivePointerGrab(Window window, XClientWindowListener xClientWindowListener, boolean z, Cursor cursor, Window window2, DeviceGrabMode deviceGrabMode, DeviceGrabMode deviceGrabMode2, int i, boolean z2) {
        Assert.state(window != null);
        if (i == 0) {
            this.lastPointerGrabTime = (int) System.currentTimeMillis();
        }
        if (!(deviceGrabMode == DeviceGrabMode.ASYNCHRONOUS && deviceGrabMode2 == DeviceGrabMode.ASYNCHRONOUS)) {
            Assert.notImplementedYet();
        }
        if (this.pointerGrabWindow == null) {
            this.xServer.getPointerEventSender().sendGrabActivationEvents(window);
        }
        this.grabCursor = cursor;
        this.pointerGrabWindow = window;
        this.pointerGrabReleaseWithButtons = z2;
        this.pointerGrabOwnerEvents = z;
        this.pointerGrabListener = xClientWindowListener;
    }

    public void disablePointerGrab() {
        if (this.pointerGrabWindow != null) {
            Window window = this.pointerGrabWindow;
            this.pointerGrabWindow = null;
            this.grabCursor = null;
            this.pointerGrabListener = null;
            this.xServer.getPointerEventSender().sendGrabDeactivationEvents(window);
        }
    }

    public void disableAutomaticOrPassiveGrab() {
        if (this.pointerGrabReleaseWithButtons) {
            disablePointerGrab();
        }
    }

    public void initiateActivePointerGrab(Window window, boolean z, Mask<EventName> mask, Cursor cursor, Window window2, DeviceGrabMode deviceGrabMode, DeviceGrabMode deviceGrabMode2, int i, XClient xClient) {
        initiateActivePointerGrab(window, new XClientWindowListener(xClient, mask), z, cursor, window2, deviceGrabMode, deviceGrabMode2, i, false);
    }

    public void initiateAutomaticPointerGrab(Window window) {
        XClientWindowListener buttonPressListener = window.getEventListenersList().getButtonPressListener();
        initiateActivePointerGrab(window, buttonPressListener, buttonPressListener.getMask().isSet(EventName.OWNER_GRAB_BUTTON), (Cursor) null, (Window) null, DeviceGrabMode.ASYNCHRONOUS, DeviceGrabMode.ASYNCHRONOUS, 0, true);
    }

    public Window getPointerGrabWindow() {
        return this.pointerGrabWindow;
    }

    public XClientWindowListener getPointerGrabListener() {
        return this.pointerGrabListener;
    }

    public boolean getPointerGrabOwnerEvents() {
        return this.pointerGrabOwnerEvents;
    }

    public int getPointerGrabTime() {
        return this.lastPointerGrabTime;
    }

    public void windowUnmapped(Window window) {
        if (this.pointerGrabWindow != null && WindowHelpers.getWindowMapState(this.pointerGrabWindow) != MapState.VIEWABLE) {
            disablePointerGrab();
        }
    }

    public void windowReparented(Window window, Window window2) {
        if (this.pointerGrabWindow != null && WindowHelpers.getWindowMapState(this.pointerGrabWindow) != MapState.VIEWABLE) {
            disablePointerGrab();
        }
    }
}
