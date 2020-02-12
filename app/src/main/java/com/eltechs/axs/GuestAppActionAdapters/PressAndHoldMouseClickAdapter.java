package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.PointerEventReporter;

public class PressAndHoldMouseClickAdapter implements MouseClickAdapter {
    private final int buttonCode;
    private final PointerEventReporter per;

    public PressAndHoldMouseClickAdapter(PointerEventReporter pointerEventReporter, int i) {
        this.per = pointerEventReporter;
        this.buttonCode = i;
    }

    public void click() {
        this.per.buttonPressed(this.buttonCode);
    }

    public void finalizeClick() {
        this.per.buttonReleased(this.buttonCode);
    }
}
