package com.eltechs.axs;

import com.eltechs.axs.widgets.viewOfXServer.TransformationHelpers;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.ViewFacade;

public class PointerEventReporter implements PointerEventListener {
    final ViewOfXServer host;
    final int maxDivisor = 20;
    final float maximalDelta;
    private final ViewFacade xServerFacade;

    public PointerEventReporter(ViewOfXServer viewOfXServer) {
        this.host = viewOfXServer;
        this.xServerFacade = viewOfXServer.getXServerFacade();
        this.maximalDelta = ((float) (Math.min(this.xServerFacade.getScreenInfo().heightInPixels, this.xServerFacade.getScreenInfo().widthInPixels) / this.maxDivisor));
    }

    private void sendCoordinates(float f, float f2) {
        float[] fArr = {f, f2};
        TransformationHelpers.mapPoints(this.host.getViewToXServerTransformationMatrix(), fArr);
        this.xServerFacade.injectPointerMove((int) fArr[0], (int) fArr[1]);
    }

    public void pointerEntered(float f, float f2) {
        sendCoordinates(f, f2);
    }

    public void pointerExited(float f, float f2) {
        sendCoordinates(f, f2);
    }

    public void pointerMove(float f, float f2) {
        sendCoordinates(f, f2);
    }

    public void clickAtPoint(float f, float f2, int i, int i2) {
        pointerMove(f, f2);
        click(i, i2);
    }

    public void click(int i, int i2) {
        long j = (long) i2;
        try {
            Thread.sleep(j, 0);
        } catch (InterruptedException unused) {
        }
        buttonPressed(i);
        try {
            Thread.sleep(j, 0);
        } catch (InterruptedException unused2) {
        }
        buttonReleased(i);
    }

    public void pointerMoveDelta(float f, float f2) {
        int min = Math.min((int) Math.max(Math.abs(f / this.maximalDelta), Math.abs(f2 / this.maximalDelta)), this.maxDivisor);
        float f3 = (float) min;
        float f4 = f / f3;
        float f5 = f2 / f3;
        this.xServerFacade.injectPointerDelta((int) f4, (int) f5, min);
        this.xServerFacade.injectPointerDelta((int) (f - (f4 * f3)), (int) (f2 - (f5 * f3)));
    }

    public void buttonPressed(int i) {
        this.xServerFacade.injectPointerButtonPress(i);
    }

    public void buttonReleased(int i) {
        this.xServerFacade.injectPointerButtonRelease(i);
    }

    public void wheelScrolledUp() {
        this.xServerFacade.injectPointerWheelUp(1);
    }

    public void wheelScrolledDown() {
        this.xServerFacade.injectPointerWheelDown(1);
    }
}
