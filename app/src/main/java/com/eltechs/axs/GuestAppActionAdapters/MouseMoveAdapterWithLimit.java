package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.PointerEventReporter;
import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.widgets.viewOfXServer.TransformationHelpers;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;

public class MouseMoveAdapterWithLimit implements MouseMoveAdapter {
    private final ViewOfXServer host;
    private final PointerEventReporter per;
    final float xLimitUp;
    final float yLimitUp;

    public void prepareMoving(float f, float f2) {
    }

    public MouseMoveAdapterWithLimit(ViewOfXServer viewOfXServer, float f, float f2) {
        this.host = viewOfXServer;
        this.xLimitUp = f;
        this.yLimitUp = f2;
        this.per = new PointerEventReporter(viewOfXServer);
    }

    public void moveTo(float f, float f2) {
        float[] fArr = {this.xLimitUp, this.yLimitUp};
        TransformationHelpers.mapPoints(this.host.getXServerToViewTransformationMatrix(), fArr);
        this.per.pointerMove(ArithHelpers.saturateInRange(f, f, fArr[0]), ArithHelpers.saturateInRange(f2, f2, fArr[1]));
    }
}
