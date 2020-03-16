package com.eltechs.axs;

public class FingerTouchAdapter implements PointerEventListener {
    private final PointerEventReporter pointerEventReporter;

    public FingerTouchAdapter(PointerEventReporter pointerEventReporter2) {
        this.pointerEventReporter = pointerEventReporter2;
    }

    public void pointerEntered(float f, float f2) {
        this.pointerEventReporter.pointerEntered(f, f2);
    }

    public void pointerExited(float f, float f2) {
        this.pointerEventReporter.pointerExited(f, f2);
        this.pointerEventReporter.buttonPressed(1);
        try {
            Thread.sleep(200, 0);
        } catch (InterruptedException unused) {
        }
        this.pointerEventReporter.buttonReleased(1);
    }

    public void pointerMove(float f, float f2) {
        this.pointerEventReporter.pointerMove(f, f2);
    }
}
