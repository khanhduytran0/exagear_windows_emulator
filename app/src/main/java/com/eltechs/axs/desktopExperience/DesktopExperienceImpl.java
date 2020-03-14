package com.eltechs.axs.desktopExperience;

import com.eltechs.axs.desktopExperience.ICCCM.WMStateProperty;
import com.eltechs.axs.desktopExperience.ICCCM.WMStateValues;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.AtomsManager;
import com.eltechs.axs.xserver.DesktopExperience;
import com.eltechs.axs.xserver.FocusManager;
import com.eltechs.axs.xserver.FocusManager.FocusReversionPolicy;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.Pointer;
import com.eltechs.axs.xserver.PointerListener;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import com.eltechs.axs.xserver.WindowPropertiesManager.PropertyModification;
import com.eltechs.axs.xserver.WindowProperty;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import java.nio.charset.Charset;
import java.util.Map.Entry;

public class DesktopExperienceImpl implements DesktopExperience, PointerListener, WindowLifecycleListener {
    private XServer xServer;

    public void pointerButtonReleased(int i) {
    }

    public void pointerMoved(int i, int i2) {
    }

    public void pointerWarped(int i, int i2) {
    }

    public void windowCreated(Window window) {
    }

    public void windowDestroyed(Window window) {
    }

    public void windowReparented(Window window, Window window2) {
    }

    public void windowUnmapped(Window window) {
    }

    public void windowZOrderChange(Window window) {
    }

    public void attachToXServer(XServer xServer2) {
        Throwable th = null;
        this.xServer = xServer2;
        XLock lock = xServer2.getLocksManager().lock(new Subsystem[]{Subsystem.DESKTOP_EXPERIENCE, Subsystem.INPUT_DEVICES, Subsystem.WINDOWS_MANAGER, Subsystem.ATOMS_MANAGER});
        try {
            xServer2.desktopExperienceAttached(this);
            xServer2.getPointer().addListener(this);
            xServer2.getWindowsManager().addWindowLifecycleListener(this);
            initXResources();
            if (lock != null) {
                lock.close();
            }
        } catch (Throwable th2) {
            th.addSuppressed(th2);
            RuntimeException runtimeException = new RuntimeException(th);
        }
    }

    public void detachFromXServer() {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.DESKTOP_EXPERIENCE, Subsystem.INPUT_DEVICES, Subsystem.WINDOWS_MANAGER});
        this.xServer.getPointer().removeListener(this);
        this.xServer.getWindowsManager().removeWindowLifecycleListener(this);
        this.xServer.desktopExperienceDetached(this);
        if (lock != null) {
            lock.close();
        }
    }

    public void pointerButtonPressed(int i) {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.FOCUS_MANAGER, Subsystem.INPUT_DEVICES});
        FocusManager focusManager = this.xServer.getFocusManager();
        Window focusedWindow = focusManager.getFocusedWindow();
        Pointer pointer = this.xServer.getPointer();
        Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
        Window directMappedSubWindowByCoords = WindowHelpers.getDirectMappedSubWindowByCoords(rootWindow, pointer.getX(), pointer.getY());
        if (directMappedSubWindowByCoords == null && focusManager.getFocusedWindow() != rootWindow) {
            focusManager.setFocus(rootWindow, FocusReversionPolicy.NONE);
        } else if (directMappedSubWindowByCoords != focusedWindow) {
            focusManager.setFocus(directMappedSubWindowByCoords, focusManager.getFocusReversionPolicy());
        }
        if (lock != null) {
            lock.close();
        }
    }

    public void windowMapped(Window window) {
        if (window.getParent() == this.xServer.getWindowsManager().getRootWindow()) {
            this.xServer.getFocusManager().setFocus(window, FocusReversionPolicy.POINTER_ROOT);
            ICCCMHelpers.setWMState(this.xServer, window, new WMStateProperty(WMStateValues.NORMAL, null));
        }
    }

    private void initXResources() {
        setXResourceToWindow(this.xServer.getWindowsManager().getRootWindow(), new XResourceCursor(24, "dmz", true));
    }

    private void setXResourceToWindow(Window window, XResource xResource) {
        boolean z;
        AtomsManager atomsManager = this.xServer.getAtomsManager();
        Atom atom = atomsManager.getAtom("RESOURCE_MANAGER");
        Assert.state(atom != null, "Atom RESOURCE_MANAGER must be predefined");
        Atom atom2 = atomsManager.getAtom("STRING");
        if (atom2 != null) {
            z = true;
        } else {
            z = false;
        }
        Assert.state(z, "Atom STRING must be predefined");
        StringBuilder sb = new StringBuilder();
        for (Entry entry : xResource.getKeyValPairs().entrySet()) {
            sb.append(xResource.getName());
            sb.append('.');
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(9);
            sb.append(entry.getValue());
            sb.append(10);
        }
        window.getPropertiesManager().modifyProperty(atom, atom2, WindowProperty.ARRAY_OF_BYTES, PropertyModification.APPEND, sb.toString().getBytes(Charset.forName("latin1")));
    }
}
