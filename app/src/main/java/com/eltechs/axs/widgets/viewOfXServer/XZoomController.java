package com.eltechs.axs.widgets.viewOfXServer;

import android.graphics.Matrix;
import android.graphics.PointF;
import com.eltechs.axs.geom.RectangleF;
import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.ScreenInfo;

public class XZoomController {
    private final float MAX_ZOOM_FACTOR = 5.0f;
    private final float ZOOM_SENSETIVITY_THRESHOLD = 1.005f;
    private PointF anchorHost;
    private PointF anchorXServer;
    private boolean isZoomed;
    private final ViewOfXServer viewOfXServer;
    private RectangleF visibleRectangle;
    private final ScreenInfo xServerScreenInfo;
    private double zoomFactor;

    public XZoomController(ViewOfXServer viewOfXServer2, ScreenInfo screenInfo) {
        this.viewOfXServer = viewOfXServer2;
        this.xServerScreenInfo = screenInfo;
        this.visibleRectangle = new RectangleF(0.0f, 0.0f, (float) screenInfo.widthInPixels, (float) screenInfo.heightInPixels);
        this.zoomFactor = 1.0d;
    }

    public void setAnchorBoth(float f, float f2) {
        setAnchorHost(f, f2);
        float[] fArr = {f, f2};
        TransformationHelpers.mapPoints(this.viewOfXServer.getViewToXServerTransformationMatrix(), fArr);
        this.anchorXServer = new PointF(fArr[0], fArr[1]);
    }

    public void setAnchorHost(float f, float f2) {
        this.anchorHost = new PointF(f, f2);
    }

    public void insertZoomFactorChange(double d) {
        this.zoomFactor *= d;
    }

    private void setZoom() {
        Assert.isTrue(this.isZoomed);
        int i = this.xServerScreenInfo.widthInPixels;
        int i2 = this.xServerScreenInfo.heightInPixels;
        float unsignedSaturate = ArithHelpers.unsignedSaturate((float) (((double) i) / this.zoomFactor), (float) i);
        float unsignedSaturate2 = ArithHelpers.unsignedSaturate((float) (((double) i2) / this.zoomFactor), (float) i2);
        float[] fArr = {this.anchorHost.x, this.anchorHost.y};
        TransformationHelpers.mapPoints(this.viewOfXServer.getViewToXServerTransformationMatrix(), fArr);
        applyZoomRect(new RectangleF(this.anchorXServer.x - (fArr[0] - this.visibleRectangle.x), this.anchorXServer.y - (fArr[1] - this.visibleRectangle.y), unsignedSaturate, unsignedSaturate2));
    }

    public void refreshZoom() {
        if (this.zoomFactor < 1.0049999952316284d) {
            if (this.zoomFactor < 1.0d) {
                this.zoomFactor = 1.0d;
            }
            if (this.isZoomed) {
                revertZoom();
                this.isZoomed = false;
                return;
            }
            return;
        }
        if (this.zoomFactor > 5.0d) {
            this.zoomFactor = 5.0d;
        }
        this.isZoomed = true;
        setZoom();
    }

    public boolean isZoomed() {
        return this.isZoomed;
    }

    private void applyZoomRect(RectangleF rectangleF) {
        Matrix makeTransformationMatrix = TransformationHelpers.makeTransformationMatrix((float) this.viewOfXServer.getWidth(), (float) this.viewOfXServer.getHeight(), rectangleF.x, rectangleF.y, rectangleF.width, rectangleF.height, this.viewOfXServer.getConfiguration().getFitStyleHorizontal(), this.viewOfXServer.getConfiguration().getFitStyleVertical());
        Assert.state(makeTransformationMatrix.invert(makeTransformationMatrix), "xScreenRect is degenerate");
        this.viewOfXServer.setViewToXServerTransformationMatrix(makeTransformationMatrix);
        this.viewOfXServer.setXViewport(rectangleF);
        this.visibleRectangle = rectangleF;
    }

    public void revertZoom() {
        applyZoomRect(new RectangleF(0.0f, 0.0f, (float) this.xServerScreenInfo.widthInPixels, (float) this.xServerScreenInfo.heightInPixels));
    }
}
