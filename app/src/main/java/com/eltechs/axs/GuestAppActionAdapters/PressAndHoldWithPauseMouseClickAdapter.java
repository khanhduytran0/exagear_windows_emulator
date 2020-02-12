package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.PointerEventReporter;

public class PressAndHoldWithPauseMouseClickAdapter implements MouseClickAdapter {
    private final int buttonCode;
    private final PointerEventReporter per;
    private final int sleepMs;

    public PressAndHoldWithPauseMouseClickAdapter(PointerEventReporter pointerEventReporter, int i, int i2) {
        this.per = pointerEventReporter;
        this.buttonCode = i;
        this.sleepMs = i2;
    }

    public void click() {
        try {
            Thread.sleep((long) this.sleepMs);
        } catch (InterruptedException unused) {
        }
        this.per.buttonPressed(this.buttonCode);
        try {
            Thread.sleep((long) this.sleepMs);
        } catch (InterruptedException unused2) {
        }
    }

    public void finalizeClick() {
        this.per.buttonReleased(this.buttonCode);
    }
}
