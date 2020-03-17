package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GuestAppActionAdapters.AsyncScrollAdapter;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;
import com.eltechs.axs.MovementAccumulator;
import com.eltechs.axs.MovementAccumulator.Direction;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.InfiniteTimer;

public class GestureState1FingerMoveToScrollAsync extends AbstractGestureFSMState {
    public static FSMEvent GESTURE_COMPLETED = new FSMEvent() {
    };
    private static final long timerPeriodMs = 40;
    private final boolean cancelIfFingerReleased;
    private final boolean doAdjustPointerPosition;
    private final float moveThresholdPixels;
    private final float movementUnitsInOnePixelX;
    private final float movementUnitsInOnePixelY;
    private MovementAccumulator movementX;
    private MovementAccumulator movementY;
    private final int pointerMargin;
    private Finger savedFinger;
    private final AsyncScrollAdapter scrollAdapter;
    private InfiniteTimer timer;

    public GestureState1FingerMoveToScrollAsync(GestureContext gestureContext, AsyncScrollAdapter asyncScrollAdapter, float f, float f2, float f3, boolean z, int i, boolean z2) {
        super(gestureContext);
        this.scrollAdapter = asyncScrollAdapter;
        this.movementUnitsInOnePixelX = f;
        this.movementUnitsInOnePixelY = f2;
        this.moveThresholdPixels = f3;
        this.doAdjustPointerPosition = z;
        this.pointerMargin = i;
        this.cancelIfFingerReleased = z2;
    }

    public void notifyBecomeActive() {
        this.timer = new InfiniteTimer(timerPeriodMs) {
            public void onTick(long j) {
                if (GestureState1FingerMoveToScrollAsync.this.getContext().getMachine().isActiveState(GestureState1FingerMoveToScrollAsync.this)) {
                    GestureState1FingerMoveToScrollAsync.this.notifyTimer();
                }
            }
        };
        this.timer.start();
        boolean z = true;
        if (getContext().getFingers().size() != 1) {
            z = false;
        }
        Assert.isTrue(z);
        this.savedFinger = (Finger) getContext().getFingers().get(0);
        this.movementX = new MovementAccumulator(this.movementUnitsInOnePixelX, this.moveThresholdPixels);
        this.movementY = new MovementAccumulator(this.movementUnitsInOnePixelY, this.moveThresholdPixels);
        this.movementX.reset(this.savedFinger.getXWhenFirstTouched());
        this.movementY.reset(this.savedFinger.getYWhenFirstTouched());
        if (this.doAdjustPointerPosition) {
            Helpers.adjustPointerPosition(getContext().getViewFacade(), this.pointerMargin);
        }
        this.scrollAdapter.notifyStart();
    }

    public void notifyBecomeInactive() {
        this.scrollAdapter.notifyStop();
        this.timer.cancel();
    }

    private void scrollImpl(Direction direction, Direction direction2, boolean z, boolean z2) {
        DirectionX directionX = DirectionX.NONE;
        DirectionY directionY = DirectionY.NONE;
        if (z) {
            switch (direction) {
                case ASC:
                    directionX = DirectionX.LEFT;
                    break;
                case DESC:
                    directionX = DirectionX.RIGHT;
                    break;
            }
        }
        if (z2) {
            switch (direction2) {
                case ASC:
                    directionY = DirectionY.UP;
                    break;
                case DESC:
                    directionY = DirectionY.DOWN;
                    break;
            }
        }
        this.scrollAdapter.setScrolling(directionX, directionY);
    }

    private void changeMovementUnits(MovementAccumulator movementAccumulator, float f) {
        if (((float) (System.currentTimeMillis() - movementAccumulator.getMovementStartTimestamp())) > movementAccumulator.getMovementUnitsAccumulated()) {
            movementAccumulator.stop(f);
        }
    }

    /* access modifiers changed from: private */
    public void notifyTimer() {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = false;
        this.movementX.processFingerMovement(false, this.savedFinger.getX(), currentTimeMillis);
        this.movementY.processFingerMovement(false, this.savedFinger.getY(), currentTimeMillis);
        Direction direction = this.movementX.getDirection();
        Direction direction2 = this.movementY.getDirection();
        boolean z2 = this.movementX.getDirection() != Direction.NEUTRAL && this.movementX.getMovementUnitsAccumulated() >= 1.0f;
        if (this.movementY.getDirection() != Direction.NEUTRAL && this.movementY.getMovementUnitsAccumulated() >= 1.0f) {
            z = true;
        }
        scrollImpl(direction, direction2, z2, z);
        if (z2) {
            changeMovementUnits(this.movementX, this.savedFinger.getX());
        }
        if (z) {
            changeMovementUnits(this.movementY, this.savedFinger.getY());
        }
        if (!getContext().getFingers().isEmpty()) {
            return;
        }
        if (this.cancelIfFingerReleased || (!z2 && !z)) {
            if (z2 || z) {
                this.scrollAdapter.setScrolling(DirectionX.NONE, DirectionY.NONE);
            }
            sendEvent(GESTURE_COMPLETED);
        }
    }
}
