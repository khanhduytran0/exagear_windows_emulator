package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.impl.masks.Mask;

public class WindowAttributes {
    private int backingPixel;
    private int backingPlanes;
    private BackingStore backingStore = BackingStore.NOT_USEFUL;
    private BitGravity bitGravity;
    private int borderWidth;
    private Cursor cursor;
    private Mask<EventName> doNotPropagateMask;
    private boolean isMapped;
    private boolean overrideRedirect;
    private boolean saveUnder;
    private WinGravity winGravity;
    private final Window window;
    private final WindowChangeListenersList windowChangeListenersList;
    private final WindowClass windowClass;

    public enum BackingStore {
        NOT_USEFUL,
        WHEN_MAPPED,
        ALWAYS
    }

    public enum WindowClass {
        INPUT_OUTPUT,
        INPUT_ONLY
    }

    public WindowAttributes(WindowClass windowClass2, WindowChangeListenersList windowChangeListenersList2, Window window2) {
        this.windowClass = windowClass2;
        this.bitGravity = BitGravity.CENTER;
        this.winGravity = WinGravity.CENTER;
        this.backingPlanes = 1;
        this.backingPixel = 0;
        this.saveUnder = false;
        this.isMapped = false;
        this.overrideRedirect = false;
        this.doNotPropagateMask = Mask.emptyMask(EventName.class);
        this.windowChangeListenersList = windowChangeListenersList2;
        this.window = window2;
    }

    public BackingStore getBackingStore() {
        return this.backingStore;
    }

    public WindowClass getWindowClass() {
        return this.windowClass;
    }

    public BitGravity getBitGravity() {
        return this.bitGravity;
    }

    public WinGravity getWinGravity() {
        return this.winGravity;
    }

    public int getBackingPlanes() {
        return this.backingPlanes;
    }

    public int getBackingPixel() {
        return this.backingPixel;
    }

    public boolean isSaveUnder() {
        return this.saveUnder;
    }

    public boolean isMapped() {
        return this.isMapped;
    }

    public void setMapped(boolean z) {
        this.isMapped = z;
    }

    public boolean isOverrideRedirect() {
        return this.overrideRedirect;
    }

    public Mask<EventName> getDoNotPropagateMask() {
        return this.doNotPropagateMask;
    }

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(int i) {
        this.borderWidth = i;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public void setCursor(Cursor cursor2) {
        this.cursor = cursor2;
    }

    public void update(Mask<WindowAttributeNames> mask, Integer num, Integer num2, BitGravity bitGravity2, WinGravity winGravity2, BackingStore backingStore2, Integer num3, Integer num4, Boolean bool, Boolean bool2, Mask<EventName> mask2, Integer num5, Cursor cursor2) {
        if (mask.isSet(WindowAttributeNames.BACKING_PIXEL)) {
            this.backingPixel = num4.intValue();
        }
        if (mask.isSet(WindowAttributeNames.BACKING_PLANES)) {
            this.backingPlanes = num3.intValue();
        }
        if (mask.isSet(WindowAttributeNames.BIT_GRAVITY)) {
            this.bitGravity = bitGravity2;
        }
        if (mask.isSet(WindowAttributeNames.WIN_GRAVITY)) {
            this.winGravity = winGravity2;
        }
        if (mask.isSet(WindowAttributeNames.BACKING_STORE)) {
            this.backingStore = backingStore2;
        }
        if (mask.isSet(WindowAttributeNames.SAVE_UNDER)) {
            this.saveUnder = bool2.booleanValue();
        }
        if (mask.isSet(WindowAttributeNames.OVERRIDE_REDIRECT)) {
            this.overrideRedirect = bool.booleanValue();
        }
        if (mask.isSet(WindowAttributeNames.DO_NOT_PROPAGATE_MASK)) {
            this.doNotPropagateMask = mask2;
        }
        if (mask.isSet(WindowAttributeNames.CURSOR)) {
            this.cursor = cursor2;
        }
        this.windowChangeListenersList.sendWindowAttributeChanged(this.window, mask);
    }
}
