package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;
import com.eltechs.axs.GuestAppActionAdapters.SyncScrollAdapter;
import com.eltechs.axs.MovementAccumulator;
import com.eltechs.axs.MovementAccumulator.Direction;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.InfiniteTimer;

public class GestureState1FingerMoveToScrollSync extends AbstractGestureFSMState {
    public static FSMEvent GESTURE_COMPLETED = new FSMEvent() {
    };
    private final boolean breakIfFingerReleased;
    private final boolean doAdjustPointerPosition;
    private final long fingerLocationPollIntervalMs;
    private final float moveThresholdPixels;
    private final float movementUnitsInOnePixelX;
    private final float movementUnitsInOnePixelY;
    private MovementAccumulator movementX;
    private MovementAccumulator movementY;
    private final int pointerMargin;
    private Finger savedFinger;
    private final SyncScrollAdapter scrollAdapter;
    private InfiniteTimer timer;

    public GestureState1FingerMoveToScrollSync(GestureContext gestureContext, SyncScrollAdapter syncScrollAdapter, float f, float f2, float f3, boolean z, int i, long j, boolean z2) {
        super(gestureContext);
        this.scrollAdapter = syncScrollAdapter;
        this.movementUnitsInOnePixelX = f;
        this.movementUnitsInOnePixelY = f2;
        this.moveThresholdPixels = f3;
        this.doAdjustPointerPosition = z;
        this.pointerMargin = i;
        this.fingerLocationPollIntervalMs = j;
        this.breakIfFingerReleased = z2;
    }

    public void notifyBecomeActive() {
        this.timer = new InfiniteTimer(this.fingerLocationPollIntervalMs) {
            public void onTick(long j) {
                if (GestureState1FingerMoveToScrollSync.this.getContext().getMachine().isActiveState(GestureState1FingerMoveToScrollSync.this)) {
                    GestureState1FingerMoveToScrollSync.this.notifyTimer();
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
        this.movementX = new MovementAccumulator(this.movementUnitsInOnePixelX, 0.0f);
        this.movementY = new MovementAccumulator(this.movementUnitsInOnePixelY, 0.0f);
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
        this.scrollAdapter.scroll(directionX, directionY, 1);
    }

    private void changeMovementUnits(MovementAccumulator movementAccumulator, float f) {
        float movementUnitsAccumulated = movementAccumulator.getMovementUnitsAccumulated() - 1.0f;
        if (movementUnitsAccumulated > 0.0f) {
            movementAccumulator.setMovementUnitsAccumulated(movementUnitsAccumulated);
        } else {
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
        if (this.breakIfFingerReleased || (!z2 && !z)) {
            sendEvent(GESTURE_COMPLETED);
        }
    }
}
