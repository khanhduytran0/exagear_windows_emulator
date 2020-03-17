package com.eltechs.axs;

import com.eltechs.axs.helpers.Assert;

public class MovementAccumulator {
    private float axis;
    private Direction direction;
    private float moveDeltaPixels;
    private long movementStartTimestamp;
    private float movementUnitsAccumulated;
    private final float movementUnitsInOnePixel;

    public enum Direction {
        DESC,
        NEUTRAL,
        ASC
    }

    public MovementAccumulator(float f, float f2) {
        this.moveDeltaPixels = f2;
        this.movementUnitsInOnePixel = f;
    }

    public void reset(float f) {
        this.direction = Direction.NEUTRAL;
        this.axis = f;
        this.movementUnitsAccumulated = 0.0f;
        this.movementStartTimestamp = 0;
    }

    private void handlePointPositionChange(float f) {
        Assert.isTrue(this.direction != Direction.NEUTRAL);
        float abs = Math.abs(f - this.axis);
        this.axis = f;
        this.movementUnitsAccumulated += abs * this.movementUnitsInOnePixel;
    }

    private boolean movementStopNeeded(float f, boolean z) {
        return (z && f < 5.0f) || f > this.moveDeltaPixels;
    }

    public void processFingerMovement(boolean z, float f, long j) {
        float axis2 = f - getAxis();
        float abs = Math.abs(axis2);
        switch (this.direction) {
            case NEUTRAL:
                if (abs <= this.moveDeltaPixels) {
                    return;
                }
                if (axis2 > 0.0f) {
                    start(Direction.ASC, f, j);
                    return;
                } else {
                    start(Direction.DESC, f, j);
                    return;
                }
            case ASC:
                if (axis2 > 0.0f) {
                    handlePointPositionChange(f);
                    return;
                } else if (movementStopNeeded(abs, z)) {
                    stop(f);
                    return;
                } else {
                    return;
                }
            case DESC:
                if (axis2 < 0.0f) {
                    handlePointPositionChange(f);
                    return;
                } else if (movementStopNeeded(abs, z)) {
                    stop(f);
                    return;
                } else {
                    return;
                }
            default:
                Assert.unreachable();
                return;
        }
    }

    public void start(Direction direction2, float f, long j) {
        this.direction = direction2;
        Assert.state(this.direction != Direction.NEUTRAL, "Movement in neutral direction is not a movement at all");
        this.movementStartTimestamp = j;
        handlePointPositionChange(f);
    }

    public void stop(float f) {
        Assert.isTrue(this.direction != Direction.NEUTRAL);
        reset(f);
    }

    public float getMovementUnitsAccumulated() {
        return this.movementUnitsAccumulated;
    }

    public void setMovementUnitsAccumulated(float f) {
        this.movementUnitsAccumulated = f;
    }

    public float getAxis() {
        return this.axis;
    }

    public void setAxis(float f) {
        this.axis = f;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction2) {
        this.direction = direction2;
    }

    public long getMovementStartTimestamp() {
        return this.movementStartTimestamp;
    }
}
