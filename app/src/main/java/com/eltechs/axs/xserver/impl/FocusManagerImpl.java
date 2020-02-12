package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.FocusListener;
import com.eltechs.axs.xserver.FocusListenersList;
import com.eltechs.axs.xserver.FocusManager;
import com.eltechs.axs.xserver.FocusManager.FocusReversionPolicy;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import com.eltechs.axs.xserver.XServer;

public class FocusManagerImpl implements FocusManager, WindowLifecycleListener {
    private Window focusedWindow;
    private FocusListenersList listeners = new FocusListenersList();
    private FocusReversionPolicy reversionPolicy = FocusReversionPolicy.NONE;
    private XServer xServer;

    public void windowCreated(Window window) {
    }

    public void windowMapped(Window window) {
    }

    public void windowReparented(Window window, Window window2) {
    }

    public void windowZOrderChange(Window window) {
    }

    public FocusManagerImpl(Window window, XServer xServer2) {
        this.focusedWindow = window;
        this.xServer = xServer2;
    }

    private void revertFocus() {
        Window window = this.focusedWindow;
        switch (this.reversionPolicy) {
            case NONE:
                this.focusedWindow = null;
                break;
            case POINTER_ROOT:
                this.focusedWindow = this.xServer.getWindowsManager().getRootWindow();
                break;
            case PARENT:
                if (this.focusedWindow.getParent() != null) {
                    this.focusedWindow = this.focusedWindow.getParent();
                    break;
                }
                break;
        }
        if (window != this.focusedWindow) {
            this.listeners.sendFocusChanged(window, this.focusedWindow);
        }
    }

    public Window getFocusedWindow() {
        return this.focusedWindow;
    }

    public void setFocus(Window window, FocusReversionPolicy focusReversionPolicy) {
        Window window2 = this.focusedWindow;
        this.focusedWindow = window;
        this.reversionPolicy = focusReversionPolicy;
        if (window2 != window) {
            this.listeners.sendFocusChanged(window2, window);
        }
    }

    public FocusReversionPolicy getFocusReversionPolicy() {
        return this.reversionPolicy;
    }

    public void addFocusListner(FocusListener focusListener) {
        this.listeners.addListener(focusListener);
    }

    public void removeFocusListener(FocusListener focusListener) {
        this.listeners.removeListener(focusListener);
    }

    public void windowUnmapped(Window window) {
        if (window == this.focusedWindow) {
            revertFocus();
        }
    }

    public void windowDestroyed(Window window) {
        if (window == this.focusedWindow) {
            revertFocus();
        }
    }
}
