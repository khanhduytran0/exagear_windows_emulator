package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GeometryHelpers;
import com.eltechs.axs.GestureStateMachine.PointerContext;
import com.eltechs.axs.GestureStateMachine.PointerContext.MoveMethod;
import com.eltechs.axs.geom.Point;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;

public class AlignedMouseClickAdapter implements MousePointAndClickAdapter {
    private MouseClickAdapter actualClicker = null;
    private final float clickAlignThreshold;
    private final MouseClickAdapter clickerIfFar;
    private final MouseClickAdapter clickerIfNear;
    private final ViewOfXServer host;
    private final MouseMoveAdapter mover;
    private final PointerContext pointerContext;

    public AlignedMouseClickAdapter(MouseMoveAdapter mouseMoveAdapter, MouseClickAdapter mouseClickAdapter, MouseClickAdapter mouseClickAdapter2, ViewOfXServer viewOfXServer, PointerContext pointerContext2, float f) {
        this.mover = mouseMoveAdapter;
        this.clickerIfNear = mouseClickAdapter;
        this.clickerIfFar = mouseClickAdapter2;
        this.host = viewOfXServer;
        this.pointerContext = pointerContext2;
        this.clickAlignThreshold = f;
    }

    public void click(float f, float f2) {
        Assert.state(this.actualClicker == null, "click() and finalizeClick() were called in wrong order!");
        Point pointerLocation = this.host.getXServerFacade().getPointerLocation();
        float[] fArr = {(float) pointerLocation.x, (float) pointerLocation.y};
        float[] fArr2 = new float[2];
        this.host.getXServerToViewTransformationMatrix().mapPoints(fArr2, fArr);
        this.mover.prepareMoving(f, f2);
        if (GeometryHelpers.distance(f, f2, fArr2[0], fArr2[1]) >= this.clickAlignThreshold) {
            this.mover.moveTo(f, f2);
            this.actualClicker = this.clickerIfFar;
        } else {
            this.actualClicker = this.clickerIfNear;
        }
        this.actualClicker.click();
    }

    public void finalizeClick(float f, float f2) {
        Assert.state(this.actualClicker != null, "click() and finalizeClick() were called in wrong order!");
        this.actualClicker.finalizeClick();
        this.pointerContext.setLastMoveMethod(MoveMethod.TAP);
        this.actualClicker = null;
    }
}
