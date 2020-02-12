package com.eltechs.axs.xserver;

import com.eltechs.axs.geom.Point;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.events.KeyPress;
import com.eltechs.axs.xserver.events.KeyRelease;
import com.eltechs.axs.xserver.events.MappingNotify;
import com.eltechs.axs.xserver.events.MappingNotify.Request;
import com.eltechs.axs.xserver.helpers.EventHelpers;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class KeyboardEventSender implements KeyboardListener {
    private final XServer xServer;

    KeyboardEventSender(XServer xServer2) {
        this.xServer = xServer2;
        this.xServer.getKeyboard().addKeyListener(this);
    }

    /* JADX WARNING: type inference failed for: r5v0 */
    /* JADX WARNING: type inference failed for: r5v1, types: [com.eltechs.axs.xserver.events.Event] */
    /* JADX WARNING: type inference failed for: r16v0 */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r2v14, types: [com.eltechs.axs.xserver.events.KeyPress] */
    /* JADX WARNING: type inference failed for: r2v18, types: [com.eltechs.axs.xserver.events.KeyRelease] */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r2v19, types: [com.eltechs.axs.xserver.events.KeyPress] */
    /* JADX WARNING: type inference failed for: r2v20, types: [com.eltechs.axs.xserver.events.KeyRelease] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v19, types: [com.eltechs.axs.xserver.events.KeyPress]
  assigns: [com.eltechs.axs.xserver.events.KeyPress, com.eltechs.axs.xserver.events.KeyRelease]
  uses: [com.eltechs.axs.xserver.events.KeyPress, ?[OBJECT, ARRAY], com.eltechs.axs.xserver.events.KeyRelease]
  mth insns count: 91
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 5 */
    private void sendKeyEvent(EventName eventName, byte b, int i, Mask<KeyButNames> mask) {
        Window window;
        Window window2;
        Window window3;
        // ? r5;
        KeyPress r16 = null;
        EventName eventName2 = eventName;
        byte b2 = b;
        int i2 = i;
        Window focusedWindow = this.xServer.getFocusManager().getFocusedWindow();
        if (focusedWindow != null) {
            Window calculatePointWindow = WindowHelpers.calculatePointWindow(this.xServer);
            // ? r52 = 0;
            if (WindowHelpers.isAncestorOf(calculatePointWindow, focusedWindow)) {
                window2 = WindowHelpers.getAncestorWithDeviceEventNameInSubtree(calculatePointWindow, eventName2, focusedWindow);
                window = WindowHelpers.getDirectChild(calculatePointWindow, window2);
            } else {
                window2 = null;
                window = null;
            }
            if (window2 != null) {
                window3 = window2;
            } else if (focusedWindow.getEventListenersList().isListenerInstalledForEvent(eventName2)) {
                window3 = focusedWindow;
            } else {
                return;
            }
            Pointer pointer = this.xServer.getPointer();
            Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(window3, pointer.getX(), pointer.getY());
            Mask keyButMask = EventHelpers.getKeyButMask(this.xServer, mask);
            switch (eventName) {
                case KEY_PRESS:
                    KeyPress keyPress = new KeyPress(b2, (int) System.currentTimeMillis(), WindowHelpers.getRootWindowOf(window3), window3, window, (short) pointer.getX(), (short) pointer.getY(), (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask);
                    r16 = keyPress;
                    break;
                case KEY_RELEASE:
                    KeyRelease keyRelease = new KeyRelease(b2, (int) System.currentTimeMillis(), WindowHelpers.getRootWindowOf(window3), window3, window, (short) pointer.getX(), (short) pointer.getY(), (short) convertRootCoordsToWindow.x, (short) convertRootCoordsToWindow.y, keyButMask);
                    // r16 = keyRelease;
                    break;
                default:
                    Assert.unreachable();
                    // r5 = r52;
                    break;
            }
            // r5 = r16;
            if (i2 != 0 && eventName2 == EventName.KEY_PRESS) {
                KeyboardModel keyboardModel = this.xServer.getKeyboardModelManager().getKeyboardModel();
                int[] iArr = new int[2];
                keyboardModel.getKeysymsForKeycodeGroup1(b2, iArr);
                if (!(iArr[0] == i2 && iArr[1] == i2)) {
                    keyboardModel.setKeysymsForKeycodeGroup1(b2, i2, i2);
                    window3.getEventListenersList().sendEvent(new MappingNotify(Request.KEYBOARD, b2, 1));
                }
            }
            window3.getEventListenersList().sendEventForEventName(r16, eventName2);
        }
    }

    public void keyPressed(byte b, int i, Mask<KeyButNames> mask) {
        sendKeyEvent(EventName.KEY_PRESS, b, i, mask);
    }

    public void keyReleased(byte b, int i, Mask<KeyButNames> mask) {
        sendKeyEvent(EventName.KEY_RELEASE, b, i, mask);
    }
}
