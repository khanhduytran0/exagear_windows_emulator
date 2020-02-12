package com.eltechs.axs;

public class MouseButtonEventReporter implements ButtonEventListener {
    private final int button;
    private final PointerEventReporter pointerEventReporter;

    public MouseButtonEventReporter(PointerEventReporter pointerEventReporter2, int i) {
        this.button = i;
        this.pointerEventReporter = pointerEventReporter2;
    }

    public void pressed() {
        this.pointerEventReporter.buttonPressed(this.button);
    }

    public void released() {
        this.pointerEventReporter.buttonReleased(this.button);
    }
}
