package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GestureStateMachine.PointerContext;
import com.eltechs.axs.GestureStateMachine.PointerContext.MoveMethod;

public class MouseClickAdapterWithCheckPlacementContext implements MousePointAndClickAdapter {
    private MousePointAndClickAdapter actualClicker;
    private final MousePointAndClickAdapter clickerAim;
    private final MousePointAndClickAdapter clickerDouble;
    private final MousePointAndClickAdapter clickerTap;
    private final int doubleClickMaxInterval;
    private final PointerContext pointerContext;

    public MouseClickAdapterWithCheckPlacementContext(MousePointAndClickAdapter mousePointAndClickAdapter, MousePointAndClickAdapter mousePointAndClickAdapter2, MousePointAndClickAdapter mousePointAndClickAdapter3, PointerContext pointerContext2, int i) {
        this.clickerTap = mousePointAndClickAdapter;
        this.clickerAim = mousePointAndClickAdapter2;
        this.clickerDouble = mousePointAndClickAdapter3;
        this.pointerContext = pointerContext2;
        this.doubleClickMaxInterval = i;
    }

    public void click(float f, float f2) {
        if (System.currentTimeMillis() - this.pointerContext.getLastMoveTimestamp() < ((long) this.doubleClickMaxInterval)) {
            this.actualClicker = this.clickerDouble;
        } else if (this.pointerContext.getLastMoveMethod() == MoveMethod.AIM) {
            this.actualClicker = this.clickerAim;
        } else {
            this.actualClicker = this.clickerTap;
        }
        this.actualClicker.click(f, f2);
    }

    public void finalizeClick(float f, float f2) {
        this.actualClicker.finalizeClick(f, f2);
    }
}
