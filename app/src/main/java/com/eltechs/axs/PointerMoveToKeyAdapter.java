package com.eltechs.axs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PointerMoveToKeyAdapter implements PointerEventListener {
    private static final int MOVE_ACC = 16;
    private static final int MOVE_DOWN = 4;
    private static final int MOVE_LEFT = 2;
    private static final int MOVE_NONE = 0;
    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_UP = 8;
    private final float accelerateDelta;
    private Collection<KeyCodesX> activeKeyCode = new ArrayList();
    private Finger finger;
    private final KeyCodesX keyAccelerate;
    private final KeyEventReporter keyEventReporter;
    private final KeyCodesX[] keyMoveDown;
    private final KeyCodesX[] keyMoveLeft;
    private final KeyCodesX[] keyMoveRight;
    private final KeyCodesX[] keyMoveUp;
    private final boolean likeJoystick;
    private final float minimalCoordinateDelta;
    private final float minimalDelta;
    private int prevMovementMask = 0;

    public PointerMoveToKeyAdapter(float f, float f2, KeyCodesX[] keyCodesXArr, KeyCodesX[] keyCodesXArr2, KeyCodesX[] keyCodesXArr3, KeyCodesX[] keyCodesXArr4, KeyCodesX keyCodesX, boolean z, KeyEventReporter keyEventReporter2) {
        if (keyCodesXArr == null) {
            keyCodesXArr = new KeyCodesX[0];
        }
        this.keyMoveUp = keyCodesXArr;
        if (keyCodesXArr2 == null) {
            keyCodesXArr2 = new KeyCodesX[0];
        }
        this.keyMoveDown = keyCodesXArr2;
        if (keyCodesXArr3 == null) {
            keyCodesXArr3 = new KeyCodesX[0];
        }
        this.keyMoveLeft = keyCodesXArr3;
        if (keyCodesXArr4 == null) {
            keyCodesXArr4 = new KeyCodesX[0];
        }
        this.keyMoveRight = keyCodesXArr4;
        this.keyAccelerate = keyCodesX;
        this.minimalDelta = f;
        this.accelerateDelta = f2;
        this.likeJoystick = z;
        this.keyEventReporter = keyEventReporter2;
        this.minimalCoordinateDelta = f / 2.0f;
    }

    public void pointerEntered(float f, float f2) {
        this.finger = new Finger(f, f2);
    }

    public void pointerExited(float f, float f2) {
        this.finger = null;
        reportKeysReleased();
    }

    public void pointerMove(float f, float f2) {
        float x = this.finger.getX() - f;
        float y = this.finger.getY() - f2;
        if (((float) Math.sqrt((double) ((x * x) + (y * y)))) < this.minimalDelta) {
            if (this.likeJoystick) {
                reportKeysReleased();
            }
            return;
        }
        reportMoveDelta(x, y);
        if (!this.likeJoystick) {
            this.finger.update(f, f2);
        }
    }

    private void reportMoveDelta(float f, float f2) {
        float f3 = this.minimalCoordinateDelta;
        if (Math.abs(f) > f3) {
            f3 = Math.abs(f);
        }
        if (Math.abs(f2) > f3) {
            f3 = Math.abs(f2);
        }
        int i = 0;
        ArrayList arrayList = new ArrayList();
        if (Math.abs(f) > this.accelerateDelta || Math.abs(f2) > this.accelerateDelta) {
            arrayList.add(this.keyAccelerate);
            i = 16;
        }
        float f4 = f3 / 2.0f;
        if (Math.abs(f) >= f4) {
            if (f < 0.0f) {
                arrayList.addAll(Arrays.asList(this.keyMoveRight));
                i |= 1;
            } else if (f > 0.0f) {
                arrayList.addAll(Arrays.asList(this.keyMoveLeft));
                i |= 2;
            }
        }
        if (Math.abs(f2) >= f4) {
            if (f2 < 0.0f) {
                arrayList.addAll(Arrays.asList(this.keyMoveDown));
                i |= 4;
            } else if (f2 > 0.0f) {
                arrayList.addAll(Arrays.asList(this.keyMoveUp));
                i |= 8;
            }
        }
        if (this.prevMovementMask != i) {
            reportKeysReleased();
            this.activeKeyCode = arrayList;
            this.prevMovementMask = i;
            if (i != 0) {
                reportKeysPressed();
            }
        }
    }

    private void reportKeysReleased() {
        if (this.prevMovementMask != 0) {
            for (KeyCodesX keyCodesX : this.activeKeyCode) {
                this.keyEventReporter.reportKeysRelease(keyCodesX);
            }
            this.prevMovementMask = 0;
        }
    }

    private void reportKeysPressed() {
        for (KeyCodesX keyCodesX : this.activeKeyCode) {
            this.keyEventReporter.reportKeysPress(keyCodesX);
        }
    }
}
