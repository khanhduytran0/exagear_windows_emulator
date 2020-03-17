package com.eltechs.axs.GuestAppActionAdapters;

public class SimpleDragAndDropAdapter implements DragAndDropAdapter {
    private final Runnable cancellationHandler;
    private final MouseClickAdapter clicker;
    private final MouseMoveAdapter mover;

    public SimpleDragAndDropAdapter(MouseMoveAdapter mouseMoveAdapter, MouseClickAdapter mouseClickAdapter, Runnable runnable) {
        this.mover = mouseMoveAdapter;
        this.clicker = mouseClickAdapter;
        this.cancellationHandler = runnable;
    }

    public void start(float f, float f2) {
        this.mover.prepareMoving(f, f2);
        this.mover.moveTo(f, f2);
        this.clicker.click();
    }

    public void move(float f, float f2) {
        this.mover.moveTo(f, f2);
    }

    public void stop(float f, float f2) {
        this.clicker.finalizeClick();
    }

    public void cancel(float f, float f2) {
        this.cancellationHandler.run();
    }
}
