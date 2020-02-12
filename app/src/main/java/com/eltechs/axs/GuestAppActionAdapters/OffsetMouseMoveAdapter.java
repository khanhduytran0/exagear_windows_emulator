package com.eltechs.axs.GuestAppActionAdapters;

public class OffsetMouseMoveAdapter implements MouseMoveAdapter {
    private final MouseMoveAdapter moveAdapter;
    private final float offsetX;
    private final float offsetY;

    public OffsetMouseMoveAdapter(MouseMoveAdapter mouseMoveAdapter, float f, float f2) {
        this.moveAdapter = mouseMoveAdapter;
        this.offsetX = f;
        this.offsetY = f2;
    }

    public void moveTo(float f, float f2) {
        this.moveAdapter.moveTo(f + this.offsetX, f2 + this.offsetY);
    }

    public void prepareMoving(float f, float f2) {
        moveTo(f, f2);
    }
}
