package com.eltechs.axs.xserver;

import com.eltechs.axs.geom.Point;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.client.XClientWindowListener;
import com.eltechs.axs.xserver.events.ButtonPress;
import com.eltechs.axs.xserver.events.ButtonRelease;
import com.eltechs.axs.xserver.events.EnterNotify;
import com.eltechs.axs.xserver.events.Event;
import com.eltechs.axs.xserver.events.LeaveNotify;
import com.eltechs.axs.xserver.events.MotionNotify;
import com.eltechs.axs.xserver.events.PointerWindowEvent.Detail;
import com.eltechs.axs.xserver.events.PointerWindowEvent.Mode;
import com.eltechs.axs.xserver.helpers.EventHelpers;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class PointerEventSender implements PointerListener, WindowLifecycleListener, WindowChangeListener {
    private Window pointWindow;
    private final XServer xServer;

    public void attributesChanged(Window window, Mask<WindowAttributeNames> mask) {
    }

    public PointerEventSender(XServer xServer2) {
        this.pointWindow = xServer2.getWindowsManager().getRootWindow();
        this.xServer = xServer2;
        this.xServer.getPointer().addListener(this);
        this.xServer.getWindowsManager().addWindowLifecycleListener(this);
        this.xServer.getWindowsManager().addWindowChangeListener(this);
    }

    private void sendEventForEventMask(Event event, EventName eventName, Window window) {
        GrabsManager grabsManager = this.xServer.getGrabsManager();
        Window pointerGrabWindow = grabsManager.getPointerGrabWindow();
        if (pointerGrabWindow != null) {
            XClientWindowListener pointerGrabListener = grabsManager.getPointerGrabListener();
            if (grabsManager.getPointerGrabOwnerEvents() && window != null) {
                window.getEventListenersList().sendEventForEventNameToClient(event, eventName, pointerGrabListener.getClient());
            } else if (pointerGrabListener.isInterestedIn(eventName)) {
                pointerGrabListener.onEvent(pointerGrabWindow, event);
            }
        } else {
            window.getEventListenersList().sendEventForEventName(event, eventName);
        }
    }

    private void sendVirtualLeaveNotify(Window window, Window window2, Detail detail, Mode mode, int i) {
        Window window3 = window2;
        Detail detail2 = detail;
        boolean z = false;
        Window window4 = window;
        Assert.isTrue(window4 != window3);
        Assert.isTrue(WindowHelpers.isAncestorOf(window, window2));
        if (detail2 == Detail.VIRTUAL || detail2 == Detail.NONLINEAR_VIRTUAL) {
            z = true;
        }
        Assert.isTrue(z);
        Mask keyButMask = EventHelpers.getKeyButMask(this.xServer);
        int x = this.xServer.getPointer().getX();
        int y = this.xServer.getPointer().getY();
        Window focusedWindow = this.xServer.getFocusManager().getFocusedWindow();
        Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
        Window parent = window.getParent();
        Window window5 = window4;
        while (parent != window3) {
            boolean isAncestorOf = WindowHelpers.isAncestorOf(parent, focusedWindow);
            Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(parent, x, y);
            LeaveNotify leaveNotify = new LeaveNotify(detail2, mode, i, rootWindow, parent, window5, (short) x, (short) y, (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask, isAncestorOf);
			Window window6 = parent;
            Window window7 = focusedWindow;
            int i2 = y;
            int i3 = x;
            sendEventForEventMask(leaveNotify, EventName.LEAVE_WINDOW, window6);
            parent = window6.getParent();
            window5 = window6;
            y = i2;
            focusedWindow = window7;
            x = i3;
            window3 = window2;
            detail2 = detail;
        }
    }

    private void sendVirtualEnterNotify(Window window, Window window2, Detail detail, Mode mode, int i) {
        Window window3 = window;
        Window window4 = window2;
        Detail detail2 = detail;
        boolean z = false;
        Assert.isTrue(window3 != window4);
        Assert.isTrue(WindowHelpers.isAncestorOf(window4, window3));
        if (detail2 == Detail.VIRTUAL || detail2 == Detail.NONLINEAR_VIRTUAL) {
            z = true;
        }
        Assert.isTrue(z);
        Mask keyButMask = EventHelpers.getKeyButMask(this.xServer);
        int x = this.xServer.getPointer().getX();
        int y = this.xServer.getPointer().getY();
        Window focusedWindow = this.xServer.getFocusManager().getFocusedWindow();
        Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
        Window directChild = WindowHelpers.getDirectChild(window4, window3);
        while (directChild != window4) {
            boolean isAncestorOf = WindowHelpers.isAncestorOf(directChild, focusedWindow);
            Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(directChild, x, y);
            Window directChild2 = WindowHelpers.getDirectChild(window4, directChild);
            EnterNotify enterNotify = new EnterNotify(detail2, mode, i, rootWindow, directChild, directChild2, (short) x, (short) y, (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask, isAncestorOf);
            Window window5 = focusedWindow;
            int i2 = y;
            int i3 = x;
            sendEventForEventMask(enterNotify, EventName.ENTER_WINDOW, directChild);
            detail2 = detail;
            directChild = directChild2;
            x = i3;
            y = i2;
            focusedWindow = window5;
            window4 = window2;
        }
    }

    private void sendEnterLeaveNotify(Window window, Window window2, Mode mode) {
        Detail detail;
        Detail detail2;
        short s;
        short s2;
        Window window3;
        Point point;
        Window window4 = window;
        Window window5 = window2;
        if (window5 != window4) {
            Mask keyButMask = EventHelpers.getKeyButMask(this.xServer);
            int x = this.xServer.getPointer().getX();
            int y = this.xServer.getPointer().getY();
            Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(window4, x, y);
            Point convertRootCoordsToWindow2 = WindowHelpers.convertRootCoordsToWindow(window5, x, y);
            Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
            boolean isAncestorOf = WindowHelpers.isAncestorOf(window5, this.xServer.getFocusManager().getFocusedWindow());
            int currentTimeMillis = (int) System.currentTimeMillis();
            if (WindowHelpers.isAncestorOf(window, window2)) {
                detail = Detail.ANCESTOR;
                detail2 = Detail.INFERIOR;
            } else if (WindowHelpers.isAncestorOf(window5, window4)) {
                detail = Detail.INFERIOR;
                detail2 = Detail.ANCESTOR;
            } else {
                detail = Detail.NONLINEAR;
                detail2 = Detail.NONLINEAR;
            }
            Detail detail3 = detail;
            Detail detail4 = detail2;
            short s3 = (short) x;
            short s4 = (short) y;
            Point point2 = convertRootCoordsToWindow2;
            LeaveNotify leaveNotify = new LeaveNotify(detail3, mode, currentTimeMillis, rootWindow, window4, null, s3, s4, (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask, isAncestorOf);
            Detail detail5 = detail3;
            int i = currentTimeMillis;
            sendEventForEventMask(leaveNotify, EventName.LEAVE_WINDOW, window4);
            switch (detail5) {
                case ANCESTOR:
                    s2 = s3;
                    s = s4;
                    window3 = window5;
                    point = point2;
                    sendVirtualLeaveNotify(window4, window3, Detail.VIRTUAL, mode, i);
                    break;
                case INFERIOR:
                    s2 = s3;
                    s = s4;
                    window3 = window5;
                    point = point2;
                    sendVirtualEnterNotify(window4, window3, Detail.VIRTUAL, mode, i);
                    break;
                case NONLINEAR:
                    Window leastCommonAncestor = WindowHelpers.getLeastCommonAncestor(window, window2);
                    s2 = s3;
                    s = s4;
                    point = point2;
                    Mode mode2 = mode;
                    window3 = window5;
                    int i2 = i;
                    sendVirtualLeaveNotify(window4, leastCommonAncestor, Detail.NONLINEAR_VIRTUAL, mode2, i2);
                    sendVirtualEnterNotify(leastCommonAncestor, window3, Detail.NONLINEAR_VIRTUAL, mode2, i2);
                    break;
                default:
                    s2 = s3;
                    s = s4;
                    window3 = window5;
                    point = point2;
                    break;
            }
            Window window6 = window3;
            EnterNotify enterNotify = new EnterNotify(detail4, mode, i, rootWindow, window3, null, s2, s, (short) point.x, (short) point.y, keyButMask, isAncestorOf);
            sendEventForEventMask(enterNotify, EventName.ENTER_WINDOW, window6);
        }
    }

    private void updatePointWindow() {
        Window calculatePointWindow = WindowHelpers.calculatePointWindow(this.xServer);
        sendEnterLeaveNotify(this.pointWindow, calculatePointWindow, Mode.NORMAL);
        this.pointWindow = calculatePointWindow;
    }

    public void sendGrabActivationEvents(Window window) {
        Assert.state(this.xServer.getGrabsManager().getPointerGrabWindow() == null);
        sendEnterLeaveNotify(this.pointWindow, window, Mode.GRAB);
    }

    public void sendGrabDeactivationEvents(Window window) {
        Assert.state(this.xServer.getGrabsManager().getPointerGrabWindow() == null);
        sendEnterLeaveNotify(window, this.pointWindow, Mode.UNGRAB);
    }

    private Mask<EventName> createCurrentEventMask() {
        Mask buttonMask = this.xServer.getPointer().getButtonMask();
        Mask<EventName> emptyMask = Mask.emptyMask(EventName.class);
        emptyMask.set(EventName.POINTER_MOTION);
        if (!buttonMask.isEmpty()) {
            emptyMask.set(EventName.BUTTON_MOTION);
            if (buttonMask.isSet(KeyButNames.BUTTON1)) {
                emptyMask.set(EventName.BUTTON_1_MOTION);
            }
            if (buttonMask.isSet(KeyButNames.BUTTON2)) {
                emptyMask.set(EventName.BUTTON_2_MOTION);
            }
            if (buttonMask.isSet(KeyButNames.BUTTON3)) {
                emptyMask.set(EventName.BUTTON_3_MOTION);
            }
            if (buttonMask.isSet(KeyButNames.BUTTON4)) {
                emptyMask.set(EventName.BUTTON_4_MOTION);
            }
            if (buttonMask.isSet(KeyButNames.BUTTON5)) {
                emptyMask.set(EventName.BUTTON_5_MOTION);
            }
        }
        return emptyMask;
    }

    public void pointerMoved(int i, int i2) {
        Window window;
        int i3 = i;
        int i4 = i2;
        updatePointWindow();
        Mask createCurrentEventMask = createCurrentEventMask();
        GrabsManager grabsManager = this.xServer.getGrabsManager();
        XClientWindowListener pointerGrabListener = grabsManager.getPointerGrabListener();
        Window pointerGrabWindow = grabsManager.getPointerGrabWindow();
        Window window2 = null;
        if (pointerGrabWindow == null || grabsManager.getPointerGrabOwnerEvents()) {
            window = WindowHelpers.getAncestorWithDeviceEventMask(this.pointWindow, createCurrentEventMask);
        } else {
            window = null;
        }
        if (pointerGrabWindow != null || window != null) {
            Window window3 = window != null ? window : pointerGrabWindow;
            Mask keyButMask = EventHelpers.getKeyButMask(this.xServer);
            Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(window3, i3, i4);
            Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
            if (this.pointWindow != window3 && WindowHelpers.isAncestorOf(this.pointWindow, window3)) {
                window2 = WindowHelpers.getDirectChild(this.pointWindow, window3);
            }
            MotionNotify motionNotify = new MotionNotify(false, (int) System.currentTimeMillis(), rootWindow, window3, window2, (short) i3, (short) i4, (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask);
            if (pointerGrabWindow == null) {
                window.getEventListenersList().sendEventForEventMask(motionNotify, createCurrentEventMask);
            } else if (grabsManager.getPointerGrabOwnerEvents() && window != null) {
                window.getEventListenersList().sendEventForEventMaskToClient(motionNotify, createCurrentEventMask, pointerGrabListener.getClient());
            } else if (pointerGrabListener.isInterestedIn(createCurrentEventMask)) {
                pointerGrabListener.onEvent(pointerGrabWindow, motionNotify);
            }
        }
    }

    public void pointerButtonPressed(int i) {
        Window pointerGrabWindow = this.xServer.getGrabsManager().getPointerGrabWindow();
        if (pointerGrabWindow == null) {
            pointerGrabWindow = WindowHelpers.getAncestorWithDeviceEventName(this.pointWindow, EventName.BUTTON_PRESS);
            if (pointerGrabWindow != null) {
                this.xServer.getGrabsManager().initiateAutomaticPointerGrab(pointerGrabWindow);
            }
        }
        if (pointerGrabWindow != null) {
            Mask keyButMask = EventHelpers.getKeyButMask(this.xServer);
            keyButMask.clear(KeyButNames.getFlagForButtonNumber(i));
            int x = this.xServer.getPointer().getX();
            int y = this.xServer.getPointer().getY();
            Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(pointerGrabWindow, x, y);
            Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
            Window window = null;
            if (this.pointWindow != pointerGrabWindow && WindowHelpers.isAncestorOf(this.pointWindow, pointerGrabWindow)) {
                window = WindowHelpers.getDirectChild(this.pointWindow, pointerGrabWindow);
            }
            byte b = (byte) i;
            Window window2 = pointerGrabWindow;
            ButtonPress buttonPress = new ButtonPress(b, (int) System.currentTimeMillis(), rootWindow, window2, window, (short) x, (short) y, (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask);
            pointerGrabWindow.getEventListenersList().sendEventForEventName(buttonPress, EventName.BUTTON_PRESS);
        }
    }

    public void pointerButtonReleased(int i) {
        Window window;
        GrabsManager grabsManager = this.xServer.getGrabsManager();
        Window pointerGrabWindow = grabsManager.getPointerGrabWindow();
        KeyButNames flagForButtonNumber = KeyButNames.getFlagForButtonNumber(i);
        Mask keyButMask = EventHelpers.getKeyButMask(this.xServer);
        if (KeyButNames.isModifierReal(flagForButtonNumber)) {
            keyButMask.set(flagForButtonNumber);
        }
        Window window2 = null;
        if (pointerGrabWindow == null || grabsManager.getPointerGrabOwnerEvents()) {
            window = WindowHelpers.getAncestorWithDeviceEventName(this.pointWindow, EventName.BUTTON_RELEASE);
        } else {
            window = null;
        }
        if (!(pointerGrabWindow == null && window == null)) {
            Window window3 = window != null ? window : pointerGrabWindow;
            int x = this.xServer.getPointer().getX();
            int y = this.xServer.getPointer().getY();
            Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(window3, x, y);
            Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
            if (this.pointWindow != window3 && WindowHelpers.isAncestorOf(this.pointWindow, window3)) {
                window2 = WindowHelpers.getDirectChild(this.pointWindow, window3);
            }
            ButtonRelease buttonRelease = new ButtonRelease((byte) i, (int) System.currentTimeMillis(), rootWindow, window3, window2, (short) x, (short) y, (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask);
            sendEventForEventMask(buttonRelease, EventName.BUTTON_RELEASE, window);
        }
        if (this.xServer.getPointer().getButtonMask().isEmpty()) {
            grabsManager.disableAutomaticOrPassiveGrab();
        }
    }

    public void pointerWarped(int i, int i2) {
        pointerMoved(i, i2);
    }

    public void windowCreated(Window window) {
        updatePointWindow();
    }

    public void windowMapped(Window window) {
        updatePointWindow();
    }

    public void windowUnmapped(Window window) {
        updatePointWindow();
    }

    public void windowReparented(Window window, Window window2) {
        updatePointWindow();
    }

    public void windowZOrderChange(Window window) {
        updatePointWindow();
    }

    public void windowDestroyed(Window window) {
        updatePointWindow();
    }

    public void geometryChanged(Window window) {
        updatePointWindow();
    }
}
