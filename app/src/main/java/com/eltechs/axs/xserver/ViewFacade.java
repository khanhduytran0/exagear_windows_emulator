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
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyPress(b, 0);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // throw th;
    }

    public void injectKeyPressWithSym(byte b, int i) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyPress(b, i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // throw th;
    }

    public void injectKeyRelease(byte b) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyRelease(b, 0);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // throw th;
    }

    public void injectKeyReleaseWithSym(byte b, int i) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectKeyRelease(b, i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // throw th;
    }

    public void injectKeyType(byte b) {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        // throw th;
    }

    public void injectKeyTypeWithSym(byte b, int i) {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        // throw th;
    }

    public void injectMultiKeyPress(byte[] bArr) {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        // throw th;
    }

    public void injectMultiKeyPressWithSym(byte[] bArr, byte[] bArr2) {
        Throwable th = null;
        int i = 0;
        Assert.isTrue(bArr.length == bArr2.length);
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        while (i < bArr.length) {
            try {
                this.xServer.getEventsInjector().injectKeyPress(bArr[i], bArr2[i]);
                i++;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        }
        if (lockForInputDevicesManipulation != null) {
            lockForInputDevicesManipulation.close();
            return;
        }
        return;
        // // throw th;
    }

    public void injectMultiKeyRelease(byte[] bArr) {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void injectMultiKeyReleaseWithSym(byte[] bArr, int[] iArr) {
        Throwable th = null;
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
                th.addSuppressed(th2);
            }
        }
        if (lockForInputDevicesManipulation != null) {
            lockForInputDevicesManipulation.close();
            return;
        }
        return;
        // // throw th;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|(2:4|5)|29|6|7|8|9|10|(2:12|13)|30|(2:15|31)(1:32)|(1:(1:28))) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0023 */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027 A[Catch:{ Throwable -> 0x003d, all -> 0x003b, Throwable -> 0x0048 }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    public void injectMultiKeyType(byte[] bArr) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            for (byte injectKeyPress : bArr) {
                this.xServer.getEventsInjector().injectKeyPress(injectKeyPress, 0);
            }
            Thread.sleep(50);
            for (byte injectKeyRelease : bArr) {
                this.xServer.getEventsInjector().injectKeyRelease(injectKeyRelease, 0);
            }
            if (lockForInputDevicesManipulation == null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // throw th;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x002f */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0032 A[Catch:{ Throwable -> 0x004a, all -> 0x0048, Throwable -> 0x0055 }] */
    public void injectMultiKeyTypeWithSym(byte[] bArr, int[] iArr) {
        Throwable th = null;
        Assert.isTrue(bArr.length == iArr.length);
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        int i = 0;
        while (i < bArr.length) {
            try {
                this.xServer.getEventsInjector().injectKeyPress(bArr[i], iArr[i]);
                i++;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
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
        // // throw th;
    }

    public void injectPointerMove(int i, int i2) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectPointerMove(i, i2);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void injectPointerDelta(int i, int i2, int i3) {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void injectPointerDelta(int i, int i2) {
        injectPointerDelta(i, i2, 1);
    }

    public void injectPointerButtonPress(int i) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectPointerButtonPress(i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0028, code lost:
        if (r0 != null) goto L_0x002a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        if (r5 != null) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0030, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0031, code lost:
        r5.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0035, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0024, code lost:
        r1 = move-exception;
     */
    public void injectPointerWheelUp(int i) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                this.xServer.getEventsInjector().injectPointerButtonPress(4);
                this.xServer.getEventsInjector().injectPointerButtonRelease(4);
                i = i2;
            } else if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            } else {
                return;
            }
        }
        // // throw th;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0028, code lost:
        if (r0 != null) goto L_0x002a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        if (r5 != null) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0030, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0031, code lost:
        r5.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0035, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0024, code lost:
        r1 = move-exception;
     */
    public void injectPointerWheelDown(int i) {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                this.xServer.getEventsInjector().injectPointerButtonPress(5);
                this.xServer.getEventsInjector().injectPointerButtonRelease(5);
                i = i2;
            } else if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            } else {
                return;
            }
        }
        // // throw th;
    }

    public void injectPointerButtonRelease(int i) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getEventsInjector().injectPointerButtonRelease(i);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
		// // throw th;
    }

    public void addPointerListener(PointerListener pointerListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.INPUT_DEVICES);
        try {
            this.xServer.getPointer().addListener(pointerListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void removePointerListener(PointerListener pointerListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.INPUT_DEVICES);
        try {
            this.xServer.getPointer().removeListener(pointerListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void addWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.WINDOWS_MANAGER);
        try {
            this.xServer.getWindowsManager().addWindowLifecycleListener(windowLifecycleListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void removeWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(Subsystem.WINDOWS_MANAGER);
        try {
            this.xServer.getWindowsManager().removeWindowLifecycleListener(windowLifecycleListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void addWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER});
        try {
            this.xServer.getWindowsManager().addWindowContentModificationListner(windowContentModificationListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void removeWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER});
        try {
            this.xServer.getWindowsManager().removeWindowContentModificationListner(windowContentModificationListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void addWindowChangeListener(WindowChangeListener windowChangeListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER});
        try {
            this.xServer.getWindowsManager().addWindowChangeListener(windowChangeListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void removeWindowChangeListener(WindowChangeListener windowChangeListener) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER});
        try {
            this.xServer.getWindowsManager().removeWindowChangeListener(windowChangeListener);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void addCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener) {
        this.xServer.getCursorsManager().addCursorLifecycleListener(cursorLifecycleListener);
    }

    public void removeCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener) {
        this.xServer.getCursorsManager().removeCursorLifecycleListener(cursorLifecycleListener);
    }

    public void walkDrawables(DrawableHandler drawableHandler) {
        Throwable th = null;
        XLock lock = this.xServer.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER, Subsystem.CURSORS_MANAGER, Subsystem.INPUT_DEVICES});
        try {
            walkWindow(this.xServer.getWindowsManager().getRootWindow(), 0, 0, drawableHandler);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public ArrayList<PlacedDrawable> listNonRootWindowDrawables() {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        throw new RuntimeException(th);
    }

    public PlacedDrawable getCursorDrawable() {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        throw new RuntimeException(th);
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

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0028, code lost:
        if (r0 != null) goto L_0x002a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002a, code lost:
        if (r1 != null) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0030, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0031, code lost:
        r1.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0024, code lost:
        r2 = move-exception;
     */
    public Point getPointerLocation() {
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        Pointer pointer = this.xServer.getPointer();
        Point point = new Point(pointer.getX(), pointer.getY());
        if (lockForInputDevicesManipulation != null) {
            lockForInputDevicesManipulation.close();
        }
        return point;
        // // throw th;
    }

    public void addKeyboardListener(KeyboardListener keyboardListener) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().addKeyListener(keyboardListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void removeKeyboardListener(KeyboardListener keyboardListener) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().removeKeyListener(keyboardListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void addKeyboardModifiersChangeListener(KeyboardModifiersListener keyboardModifiersListener) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().addModifierListener(keyboardModifiersListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void removeKeyboardModifiersChangeListener(KeyboardModifiersListener keyboardModifiersListener) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            this.xServer.getKeyboard().removeModifierListener(keyboardModifiersListener);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void setModifierState(KeyButNames keyButNames, boolean z, byte b, boolean z2) {
        Throwable th = null;
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
            th.addSuppressed(th2);
        }
        // // throw th;
    }

    public void switchModifierState(KeyButNames keyButNames, byte b, boolean z) {
        Throwable th = null;
        XLock lockForInputDevicesManipulation = this.xServer.getLocksManager().lockForInputDevicesManipulation();
        try {
            setModifierState(keyButNames, !this.xServer.getKeyboard().getModifiersMask().isSet(keyButNames), b, z);
            if (lockForInputDevicesManipulation != null) {
                lockForInputDevicesManipulation.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        // // throw th;
    }
}
