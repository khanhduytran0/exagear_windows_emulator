package com.eltechs.axs;

public class StrokeToKeyAdapter implements StrokeEventListener {
    private final KeyCodesX keyCodeBottom;
    private final KeyCodesX keyCodeLeft;
    private final KeyCodesX keyCodeRight;
    private final KeyCodesX keyCodeTop;
    private final KeyEventReporter keyEventReporter;

    public StrokeToKeyAdapter(KeyEventReporter keyEventReporter2, KeyCodesX keyCodesX, KeyCodesX keyCodesX2, KeyCodesX keyCodesX3, KeyCodesX keyCodesX4) {
        this.keyEventReporter = keyEventReporter2;
        this.keyCodeTop = keyCodesX;
        this.keyCodeBottom = keyCodesX2;
        this.keyCodeLeft = keyCodesX3;
        this.keyCodeRight = keyCodesX4;
    }

    public void strokeTop(double d) {
        this.keyEventReporter.reportKeys(this.keyCodeTop);
    }

    public void strokeRight(double d) {
        this.keyEventReporter.reportKeys(this.keyCodeRight);
    }

    public void strokeBottom(double d) {
        this.keyEventReporter.reportKeys(this.keyCodeBottom);
    }

    public void strokeLeft(double d) {
        this.keyEventReporter.reportKeys(this.keyCodeLeft);
    }
}
