package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.PointerEventReporter;

public class SimpleMouseMoveAdapter implements MouseMoveAdapter {
    private final PointerEventReporter per;

    public void prepareMoving(float f, float f2) {
    }

    public SimpleMouseMoveAdapter(PointerEventReporter pointerEventReporter) {
        this.per = pointerEventReporter;
    }

    public void moveTo(float f, float f2) {
        this.per.pointerMove(f, f2);
    }
}
