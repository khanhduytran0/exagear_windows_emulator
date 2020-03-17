package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GestureStateMachine.PointerContext;
import com.eltechs.axs.GestureStateMachine.PointerContext.MoveMethod;

public class SimpleMousePointAndClickAdapter implements MousePointAndClickAdapter {
    private final MouseClickAdapter clicker;
    private final MouseMoveAdapter mover;
    private final PointerContext pointerContext;

    public SimpleMousePointAndClickAdapter(MouseMoveAdapter mouseMoveAdapter, MouseClickAdapter mouseClickAdapter, PointerContext pointerContext2) {
        this.clicker = mouseClickAdapter;
        this.pointerContext = pointerContext2;
        this.mover = mouseMoveAdapter;
    }

    public void click(float f, float f2) {
        this.mover.moveTo(f, f2);
        this.clicker.click();
    }

    public void finalizeClick(float f, float f2) {
        this.clicker.finalizeClick();
        this.pointerContext.setLastMoveMethod(MoveMethod.TAP);
    }
}
