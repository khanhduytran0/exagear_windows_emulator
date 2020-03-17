package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.PointerEventReporter;

public class PressAndReleaseMouseClickAdapter implements MouseClickAdapter {
    private final int buttonCode;
    private final PointerEventReporter per;
    private final int sleepMs;

    public void finalizeClick() {
    }

    public PressAndReleaseMouseClickAdapter(PointerEventReporter pointerEventReporter, int i, int i2) {
        this.per = pointerEventReporter;
        this.buttonCode = i;
        this.sleepMs = i2;
    }

    public void click() {
        this.per.click(this.buttonCode, this.sleepMs);
    }
}
