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
import org.apache.commons.io.FilenameUtils;

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
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        throw new RuntimeException(th);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0040, code lost:
        if (r1 != null) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0046, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        r1.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004b, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x003a, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x003e, code lost:
        if (r0 != null) goto L_0x0040;
     */
    public void detachFromXServer() {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.DESKTOP_EXPERIENCE, Subsystem.INPUT_DEVICES, Subsystem.WINDOWS_MANAGER});
        this.xServer.getPointer().removeListener(this);
        this.xServer.getWindowsManager().removeWindowLifecycleListener(this);
        this.xServer.desktopExperienceDetached(this);
        if (lock != null) {
            lock.close();
            return;
        }
        return;
        // throw th;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0060, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0064, code lost:
        if (r7 != null) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0066, code lost:
        if (r0 != null) goto L_0x0068;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006c, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006d, code lost:
        r0.addSuppressed(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0071, code lost:
        r7.close();
     */
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
            return;
        }
        return;
        // throw th;
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
        AtomsManager atomsManager = this.xServer.getAtomsManager();
        Atom atom = atomsManager.getAtom("RESOURCE_MANAGER");
        boolean z = false;
        Assert.state(atom != null, "Atom RESOURCE_MANAGER must be predefined");
        Atom atom2 = atomsManager.getAtom("STRING");
        if (atom2 != null) {
            z = true;
        }
        Assert.state(z, "Atom STRING must be predefined");
        StringBuilder sb = new StringBuilder();
        for (Entry entry : xResource.getKeyValPairs().entrySet()) {
            sb.append(xResource.getName());
            sb.append(FilenameUtils.EXTENSION_SEPARATOR);
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(9);
            sb.append(entry.getValue());
            sb.append(10);
        }
        window.getPropertiesManager().modifyProperty(atom, atom2, WindowProperty.ARRAY_OF_BYTES, PropertyModification.APPEND, sb.toString().getBytes(Charset.forName("latin1")));
    }
}
