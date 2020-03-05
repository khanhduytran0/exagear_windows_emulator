package com.eltechs.axs.xserver;

import com.eltechs.axs.geom.Point;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import java.util.ArrayList;

public class ViewFacade {
    final XServer xServer;

    public interface DrawableHandler {
        void handle(PlacedDrawable placedDrawable);
    }

    public ViewFacade(XServer xServer2) {
        this.xServer = xServer2;
    }

    public XServer getXServer() {
        return this.xServer;
    }

    public void injectKeyPress(byte b) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyPress(b, 0);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectKeyPressWithSym(byte b, int i) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyPress(b, i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectKeyRelease(byte b) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyRelease(b, 0);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectKeyReleaseWithSym(byte b, int i) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyRelease(b, i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectKeyType(byte b) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyPress(b, 0);
            this.xServer.getEventsInjector().injectKeyRelease(b, 0);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectKeyTypeWithSym(byte b, int i) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyPress(b, i);
            this.xServer.getEventsInjector().injectKeyRelease(b, i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectMultiKeyPress(byte[] bArr) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            for (byte injectKeyPress : bArr) {
                this.xServer.getEventsInjector().injectKeyPress(injectKeyPress, 0);
            }
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectMultiKeyPressWithSym(byte[] bArr, byte[] bArr2) {
        int i = 0;
        Assert.isTrue(bArr.length == bArr2.length);
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        while (i < bArr.length) {
            try {
                this.xServer.getEventsInjector().injectKeyPress(bArr[i], bArr2[i]);
                i++;
            } catch (Throwable th2) {
                throw new RuntimeException(th2);
            }
        }
        if (lockForInputDevicesManipulation != null) {
            lockForInputDevicesManipulation.close();
            return;
        }
        return;
    }

    public void injectMultiKeyRelease(byte[] bArr) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException unused) {
        }
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            for (byte injectKeyRelease : bArr) {
                this.xServer.getEventsInjector().injectKeyRelease(injectKeyRelease, 0);
            }
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectMultiKeyReleaseWithSym(byte[] bArr, int[] iArr) {
        int i = 0;
        Assert.isTrue(bArr.length == iArr.length);
        try {
            Thread.sleep(50);
        } catch (InterruptedException unused) {
        }
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        while (i < bArr.length) {
            try {
                this.xServer.getEventsInjector().injectKeyRelease(bArr[i], iArr[i]);
                i++;
            } catch (Throwable th2) {
                throw new RuntimeException(th2);
            }
        }
        if (lockForInputDevicesManipulation != null) {
            lockForInputDevicesManipulation.close();
            return;
        }
        return;
    }


    public void injectMultiKeyType(byte[] paramArrayOfbyte) {
        LocksManager.XLock xLock = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        Throwable throwable2 = null;
        Throwable throwable1 = throwable2;
        try {
            int j = paramArrayOfbyte.length;
            int i;
            for (i = 0; i < j; i++) {
                byte b = paramArrayOfbyte[i];
                throwable1 = throwable2;
                this.xServer.getEventsInjector().injectKeyPress(b, 0);
            }
            throwable1 = throwable2;
            try {
                Thread.sleep(50L);
            } catch (InterruptedException interruptedException) {}
            throwable1 = throwable2;
            j = paramArrayOfbyte.length;
            for (i = 0; i < j; i++) {
                byte b = paramArrayOfbyte[i];
                throwable1 = throwable2;
                this.xServer.getEventsInjector().injectKeyRelease(b, 0);
            }
            if (xLock != null)
                xLock.close();
            return;
        } catch (Throwable throwable) {
            throwable1 = throwable;
            throw throwable;
        } finally {
            if (xLock != null)
                if (throwable1 != null) {
                    try {
                        xLock.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }
                    throw new RuntimeException(throwable1);
                } else {
                    xLock.close();
                }
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x002f */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0032 A[Catch:{ Throwable -> 0x004a, all -> 0x0048, Throwable -> 0x0055 }] */
    public void injectMultiKeyTypeWithSym(byte[] bArr, int[] iArr) {
        Assert.isTrue(bArr.length == iArr.length);
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        int i = 0;
        while (i < bArr.length) {
            try {
                this.xServer.getEventsInjector().injectKeyPress(bArr[i], iArr[i]);
                i++;
            } catch (Throwable th2) {
                throw new RuntimeException(th2);
            }
        }
        try {
			Thread.sleep(50);
		} catch (InterruptedException e) {}
        for (int i2 = 0; i2 < bArr.length; i2++) {
            this.xServer.getEventsInjector().injectKeyRelease(bArr[i2], iArr[i2]);
        }
        if (lockForInputDevicesManipulation != null) {
            lockForInputDevicesManipulation.close();
            return;
        }
        return;
    }

    public void injectPointerMove(int i, int i2) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectPointerMove(i, i2);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectPointerDelta(int i, int i2, int i3) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            EventsInjector eventsInjector = this.xServer.getEventsInjector();
            for (int i4 = 0; i4 < i3; i4++) {
                eventsInjector.injectPointerMove(this.xServer.getPointer().getX() + i, this.xServer.getPointer().getY() + i2);
            }
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectPointerDelta(int i, int i2) {
        injectPointerDelta(i, i2, 1);
    }

    public void injectPointerButtonPress(int i) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectPointerButtonPress(i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void injectPointerWheelUp(int paramInt) {
        LocksManager.XLock xLock = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        while (paramInt > 0) {
            Throwable throwable2 = null;
            Throwable throwable1 = throwable2;
            try {
                this.xServer.getEventsInjector().injectPointerButtonPress(4);
                throwable1 = throwable2;
                this.xServer.getEventsInjector().injectPointerButtonRelease(4);
                paramInt--;
                continue;
            } catch (Throwable throwable3) {
                throwable1 = throwable3;
                throw throwable3;
            } finally {
                if (xLock != null)
                    if (throwable1 != null) {
                        try {
                            xLock.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }
                        throw new RuntimeException(throwable1);
                    } else {
                        xLock.close();
                    }
            }
        }

        if (xLock != null)
            xLock.close();

    }

    public void injectPointerWheelDown(int paramInt) {
        LocksManager.XLock xLock = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        while (paramInt > 0) {
            Throwable throwable2 = null;
            Throwable throwable1 = throwable2;
            try {
                this.xServer.getEventsInjector().injectPointerButtonPress(5);
                throwable1 = throwable2;
                this.xServer.getEventsInjector().injectPointerButtonRelease(5);
                paramInt--;
                continue;
            } catch (Throwable throwable3) {
                throwable1 = throwable3;
                throw throwable3;
            } finally {
                if (xLock != null)
                    if (throwable1 != null) {
                        try {
                            xLock.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }
                        throw new RuntimeException(throwable1);
                    } else {
                        xLock.close();
                    }
            }
        }
        if (xLock != null)
            xLock.close();
    }

    public void injectPointerButtonRelease(int i) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectPointerButtonRelease(i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
		// // throw th;
    }

    public void addPointerListener(PointerListener pointerListener) {
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.INPUT_DEVICES);
        try {
            this.xServer.getPointer().addListener(pointerListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void removePointerListener(PointerListener pointerListener) {
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.INPUT_DEVICES);
        try {
            this.xServer.getPointer().removeListener(pointerListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void addWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener) {
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.WINDOWS_MANAGER);
        try {
            this.xServer.getWindowsManager().addWindowLifecycleListener(windowLifecycleListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void removeWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener) {
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.WINDOWS_MANAGER);
        try {
            this.xServer.getWindowsManager().removeWindowLifecycleListener(windowLifecycleListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void addWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener) {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER});
        try {
            this.xServer.getWindowsManager().addWindowContentModificationListner(windowContentModificationListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void removeWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener) {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER});
        try {
            this.xServer.getWindowsManager().removeWindowContentModificationListner(windowContentModificationListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void addWindowChangeListener(WindowChangeListener windowChangeListener) {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER});
        try {
            this.xServer.getWindowsManager().addWindowChangeListener(windowChangeListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void removeWindowChangeListener(WindowChangeListener windowChangeListener) {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER});
        try {
            this.xServer.getWindowsManager().removeWindowChangeListener(windowChangeListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void addCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener) {
        this.xServer.getCursorsManager().addCursorLifecycleListener(cursorLifecycleListener);
    }

    public void removeCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener) {
        this.xServer.getCursorsManager().removeCursorLifecycleListener(cursorLifecycleListener);
    }

    public void walkDrawables(DrawableHandler drawableHandler) {
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER, Subsystem.CURSORS_MANAGER, Subsystem.INPUT_DEVICES});
        try {
            walkWindow(this.xServer.getWindowsManager().getRootWindow(), 0, 0, drawableHandler);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public ArrayList<PlacedDrawable> listNonRootWindowDrawables() {
        final ArrayList<PlacedDrawable> arrayList = new ArrayList<>();
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER});
        try {
            final Window rootWindow = this.xServer.getWindowsManager().getRootWindow();
            walkWindow(this.xServer.getWindowsManager().getRootWindow(), 0, 0, new DrawableHandler() {
                public void handle(PlacedDrawable placedDrawable) {
                    if (placedDrawable.getDrawable() != rootWindow.getActiveBackingStore()) {
                        arrayList.add(placedDrawable);
                    }
                }
            });
            if (lock != null) {
                lock.close();
            }
            return arrayList;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public PlacedDrawable getCursorDrawable() {
        final ArrayList arrayList = new ArrayList();
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER, Subsystem.INPUT_DEVICES, Subsystem.CURSORS_MANAGER});
        try {
            walkCursor(new DrawableHandler() {
                public void handle(PlacedDrawable placedDrawable) {
                    arrayList.add(placedDrawable);
                }
            });
            PlacedDrawable placedDrawable = arrayList.size() > 0 ? (PlacedDrawable) arrayList.get(0) : null;
            if (lock != null) {
                lock.close();
            }
            return placedDrawable;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    private void walkWindow(Window window, int i, int i2, DrawableHandler drawableHandler) {
        if (window.getWindowAttributes().isMapped()) {
            Drawable activeBackingStore = window.getActiveBackingStore();
            drawableHandler.handle(new PlacedDrawable(activeBackingStore, new Rectangle(i, i2, activeBackingStore.getWidth(), activeBackingStore.getHeight())));
            for (Window window2 : window.getChildrenBottomToTop()) {
                walkWindow(window2, window2.getBoundingRectangle().x + i, window2.getBoundingRectangle().y + i2, drawableHandler);
            }
        }
    }

    private void walkCursor(DrawableHandler drawableHandler) {
        Window leafMappedSubWindowByCoords = WindowHelpers.getLeafMappedSubWindowByCoords(this.xServer.getWindowsManager().getRootWindow(), this.xServer.getPointer().getX(), this.xServer.getPointer().getY());
        if (leafMappedSubWindowByCoords != null) {
            Cursor cursor = leafMappedSubWindowByCoords.getWindowAttributes().getCursor();
            if (cursor != null) {
                drawableHandler.handle(new PlacedDrawable(cursor.getCursorImage(), new Rectangle(this.xServer.getPointer().getX() - cursor.getHotSpotX(), this.xServer.getPointer().getY() - cursor.getHotSpotY(), cursor.getCursorImage().getWidth(), cursor.getCursorImage().getHeight())));
            }
        }
    }

    public ScreenInfo getScreenInfo() {
        return this.xServer.getScreenInfo();
    }

    public Point getPointerLocation() {
        Throwable throwable = null;
        LocksManager.XLock xLock = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        Point point2 = null;
        Point point1 = point2;
        try {
            Pointer pointer = this.xServer.getPointer();
            point1 = point2;
            point2 = new Point(pointer.getX(), pointer.getY());
            if (xLock != null)
                xLock.close();
            return point2;
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (xLock != null)
                if (throwable != null) {
                    try {
                        xLock.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                    throw new RuntimeException(throwable);
                } else {
                    xLock.close();
                }
        }
    }

    public void addKeyboardListener(KeyboardListener keyboardListener) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().addKeyListener(keyboardListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void removeKeyboardListener(KeyboardListener keyboardListener) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().removeKeyListener(keyboardListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void addKeyboardModifiersChangeListener(KeyboardModifiersListener keyboardModifiersListener) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().addModifierListener(keyboardModifiersListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void removeKeyboardModifiersChangeListener(KeyboardModifiersListener keyboardModifiersListener) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().removeModifierListener(keyboardModifiersListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void setModifierState(KeyButNames keyButNames, boolean z, byte b, boolean z2) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            if (this.xServer.getKeyboard().getModifiersMask().isSet(keyButNames) == z) {
                if (lockForInputDevicesManipulation != null) {
                    lockForInputDevicesManipulation.close();
                }
                return;
            }
            if (this.xServer.getKeyboard().getModifiersMask().isSet(keyButNames)) {
                if (z2) {
                    this.xServer.getKeyboard().keyReleased(b, 0);
                } else {
                    this.xServer.getKeyboard().keyPressed(b, 0);
                    this.xServer.getKeyboard().keyReleased(b, 0);
                }
            } else if (z2) {
                this.xServer.getKeyboard().keyPressed(b, 0);
            } else {
                this.xServer.getKeyboard().keyPressed(b, 0);
                this.xServer.getKeyboard().keyReleased(b, 0);
            }
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }

    public void switchModifierState(KeyButNames keyButNames, byte b, boolean z) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            setModifierState(keyButNames, !this.xServer.getKeyboard().getModifiersMask().isSet(keyButNames), b, z);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }
}
