package com.eltechs.axs.xserver;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class Pointer {
    public static final int BUTTON_CENTER = 2;
    public static final int BUTTON_LEFT = 1;
    public static final int BUTTON_RIGHT = 3;
    public static final int BUTTON_SCROLL_CLICK_LEFT = 6;
    public static final int BUTTON_SCROLL_CLICK_RIGHT = 7;
    public static final int BUTTON_SCROLL_DOWN = 5;
    public static final int BUTTON_SCROLL_UP = 4;
    public static final int MAX_BUTTONS = 7;
    private final Mask<KeyButNames> buttons = Mask.emptyMask(KeyButNames.class);
    private final PointerListenersList listeners = new PointerListenersList();
    private int xPos;
    private final XServer xServer;
    private int yPos;

    public boolean isButtonValid(byte b) {
        return b >= 1 && b <= 7;
    }

    public Pointer(XServer xServer2) {
        this.xServer = xServer2;
    }

    public int getX() {
        return this.xPos;
    }

    public int getY() {
        return this.yPos;
    }

    private void updateCoordinates(int i, int i2) {
        this.xPos = ArithHelpers.unsignedSaturate(i, this.xServer.getScreenInfo().widthInPixels - 1);
        this.yPos = ArithHelpers.unsignedSaturate(i2, this.xServer.getScreenInfo().heightInPixels - 1);
    }

    public void setCoordinates(int i, int i2) {
        updateCoordinates(i, i2);
        this.listeners.sendPointerMoved(this.xPos, this.yPos);
    }

    public void warpOnCoordinates(int i, int i2) {
        updateCoordinates(i, i2);
        this.listeners.sendPointerWarped(this.xPos, this.yPos);
    }

    public boolean isButtonPressed(int i) {
        return this.buttons.isSet(KeyButNames.getFlagForButtonNumber(i));
    }

    public void setButton(int i, boolean z) {
        KeyButNames flagForButtonNumber = KeyButNames.getFlagForButtonNumber(i);
        boolean isSet = this.buttons.isSet(flagForButtonNumber);
        this.buttons.setValue(flagForButtonNumber, z);
        if (isSet == z) {
            return;
        }
        if (z) {
            this.listeners.sendPointerButtonPressed(i);
        } else {
            this.listeners.sendPointerButtonReleased(i);
        }
    }

    public Mask<KeyButNames> getButtonMask() {
        return this.buttons;
    }

    public void addListener(PointerListener pointerListener) {
        this.listeners.addListener(pointerListener);
    }

    public void removeListener(PointerListener pointerListener) {
        this.listeners.removeListener(pointerListener);
    }
}
