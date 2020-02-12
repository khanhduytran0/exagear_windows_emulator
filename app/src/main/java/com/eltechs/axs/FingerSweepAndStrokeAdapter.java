package com.eltechs.axs;

// import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.util.ArrayList;
import java.util.Collection;

public class FingerSweepAndStrokeAdapter implements PointerEventListener {
    private final int buttonNum;
    private boolean enterInfoReported;
    private boolean isStrokePossible;
    private float pointerEnterX;
    private float pointerEnterY;
    private final PointerEventReporter pointerEventReporter;
    private float pointerX;
    private float pointerY;
    private final boolean pressButton;
    private final double strokeDistinctness;
    private final Collection<StrokeEventListener> strokeEventListeners = new ArrayList();
    private final double strokeSpeed;
    private long time;

    public FingerSweepAndStrokeAdapter(PointerEventReporter pointerEventReporter2, boolean z, int i, double d, double d2) {
        this.pointerEventReporter = pointerEventReporter2;
        this.pressButton = z;
        this.buttonNum = i;
        this.strokeSpeed = d;
        this.strokeDistinctness = d2;
    }

    public void addStrokeListener(StrokeEventListener strokeEventListener) {
        this.strokeEventListeners.add(strokeEventListener);
    }

    public void removeStrokeListener(StrokeEventListener strokeEventListener) {
        this.strokeEventListeners.remove(strokeEventListener);
    }

    private boolean isStroke(float f, float f2) {
        if (!this.isStrokePossible) {
            return false;
        }
        double d = (double) (this.pointerEnterX - f);
        double d2 = (double) (this.pointerEnterY - f2);
        double sqrt = Math.sqrt((d * d) + (d2 * d2));
        if (sqrt > this.strokeDistinctness) {
            double currentTimeMillis = (double) System.currentTimeMillis();
            if (currentTimeMillis == ((double) this.time)) {
                return false;
            }
            if (sqrt / (currentTimeMillis - ((double) this.time)) >= this.strokeSpeed) {
                return true;
            }
            this.isStrokePossible = false;
        }
        return false;
    }

    private void reportStroke(double d, double d2) {
        double d3 = ((double) this.pointerEnterX) - d;
        double d4 = ((double) this.pointerEnterY) - d2;
        boolean z = Math.abs(d3) > Math.abs(d4);
        double sqrt = Math.sqrt((d3 * d3) + (d4 * d4));
        if (z) {
            if (d3 > 0d) {
                for (StrokeEventListener strokeLeft : this.strokeEventListeners) {
                    strokeLeft.strokeLeft(sqrt);
                }
                return;
            }
            for (StrokeEventListener strokeRight : this.strokeEventListeners) {
                strokeRight.strokeRight(sqrt);
            }
        } else if (d4 > 0d) {
            for (StrokeEventListener strokeTop : this.strokeEventListeners) {
                strokeTop.strokeTop(sqrt);
            }
        } else {
            for (StrokeEventListener strokeBottom : this.strokeEventListeners) {
                strokeBottom.strokeBottom(sqrt);
            }
        }
    }

    private void reportEnterInfoIfNeed() {
        if (!this.enterInfoReported) {
            this.pointerEventReporter.pointerMove(this.pointerEnterX, this.pointerEnterY);
            if (this.pressButton) {
                this.pointerEventReporter.buttonPressed(this.buttonNum);
            }
            this.enterInfoReported = true;
        }
    }

    public void pointerEntered(float f, float f2) {
        this.time = System.currentTimeMillis();
        this.pointerEnterX = f;
        this.pointerX = f;
        this.pointerEnterY = f2;
        this.pointerY = f2;
        this.isStrokePossible = !this.strokeEventListeners.isEmpty();
        this.enterInfoReported = false;
    }

    public void pointerExited(float f, float f2) {
        if (isStroke(f, f2)) {
            reportStroke((double) f, (double) f2);
            return;
        }
        reportEnterInfoIfNeed();
        this.pointerEventReporter.pointerMove(f, f2);
        if (this.pressButton) {
            this.pointerEventReporter.buttonReleased(this.buttonNum);
        }
    }

    public void pointerMove(float f, float f2) {
        if (!isStroke(f, f2)) {
            reportEnterInfoIfNeed();
            this.pointerX = f;
            this.pointerY = f2;
            this.pointerEventReporter.pointerMove(f, f2);
        }
    }
}
