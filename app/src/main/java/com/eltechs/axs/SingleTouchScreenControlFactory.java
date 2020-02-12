package com.eltechs.axs;

import android.graphics.Bitmap;
import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.ViewFacade;

public class SingleTouchScreenControlFactory {
    private final ViewOfXServer viewOfXServer;

    public SingleTouchScreenControlFactory(ViewOfXServer viewOfXServer2) {
        this.viewOfXServer = viewOfXServer2;
    }

    public SimpleTouchScreenControl createSimplePointerArea(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, float f4, Bitmap bitmap, float f5, float f6, float f7, float f8, int i, boolean z, boolean z2, boolean z3) {
        JoystickVisualiser joystickVisualiser;
        FingerSweepAdapter fingerSweepAdapter = new FingerSweepAdapter(new PointerEventReporter(this.viewOfXServer), i, z, z2, z3);
        SingleTouchEventAdapter singleTouchEventAdapter = new SingleTouchEventAdapter();
        singleTouchEventAdapter.addListener(fingerSweepAdapter);
        if (bitmap != null) {
            JoystickVisualiser joystickVisualiser2 = new JoystickVisualiser(f6, bitmap, f5, true, f7, f8, graphicsSceneConfigurer);
            singleTouchEventAdapter.addListener(joystickVisualiser2);
            joystickVisualiser = joystickVisualiser2;
        } else {
            joystickVisualiser = null;
        }
        TouchArea touchArea = new TouchArea(f, f2, f3, f4, singleTouchEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, bitmap == null ? null : new TouchScreenControlVisualizer[]{joystickVisualiser});
    }

    public SimpleTouchScreenControl createSweepAndStrokeArea(ViewFacade viewFacade, float f, float f2, float f3, float f4, double d, double d2) {
        FingerSweepAndStrokeAdapter fingerSweepAndStrokeAdapter = new FingerSweepAndStrokeAdapter(new PointerEventReporter(this.viewOfXServer), true, 1, d, d2);
        StrokeToKeyAdapter strokeToKeyAdapter = new StrokeToKeyAdapter(new KeyEventReporter(viewFacade), KeyCodesX.KEY_UP, KeyCodesX.KEY_DOWN, KeyCodesX.KEY_LEFT, KeyCodesX.KEY_RIGHT);
        fingerSweepAndStrokeAdapter.addStrokeListener(strokeToKeyAdapter);
        SingleTouchEventAdapter singleTouchEventAdapter = new SingleTouchEventAdapter();
        singleTouchEventAdapter.addListener(fingerSweepAndStrokeAdapter);
        TouchArea touchArea = new TouchArea(f, f2, f3, f4, singleTouchEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, null);
    }

    public SimpleTouchScreenControl createMovesToKeysAdapterArea(ViewFacade viewFacade, float f, float f2, float f3, float f4, float f5, float f6, KeyCodesX[] keyCodesXArr, KeyCodesX[] keyCodesXArr2, KeyCodesX[] keyCodesXArr3, KeyCodesX[] keyCodesXArr4, KeyCodesX keyCodesX) {
        PointerMoveToKeyAdapter pointerMoveToKeyAdapter = new PointerMoveToKeyAdapter(f5, f6, keyCodesXArr, keyCodesXArr2, keyCodesXArr3, keyCodesXArr4, keyCodesX, false, new KeyEventReporter(viewFacade));
        SingleTouchEventAdapter singleTouchEventAdapter = new SingleTouchEventAdapter();
        singleTouchEventAdapter.addListener(pointerMoveToKeyAdapter);
        TouchArea touchArea = new TouchArea(f, f2, f3, f4, singleTouchEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, null);
    }

    public SimpleTouchScreenControl createJoystickMovesToKeysAdapterArea(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, float f4, float f5, float f6, float f7, Bitmap bitmap, float f8, float f9, float f10, KeyCodesX[] keyCodesXArr, KeyCodesX[] keyCodesXArr2, KeyCodesX[] keyCodesXArr3, KeyCodesX[] keyCodesXArr4, KeyCodesX keyCodesX) {
        PointerMoveToKeyAdapter pointerMoveToKeyAdapter = new PointerMoveToKeyAdapter(f5, f6, keyCodesXArr, keyCodesXArr2, keyCodesXArr3, keyCodesXArr4, keyCodesX, true, new KeyEventReporter(viewFacade));
        JoystickVisualiser joystickVisualiser = new JoystickVisualiser(f7, bitmap, f8, true, f9, f10, graphicsSceneConfigurer);
        SingleTouchEventAdapter singleTouchEventAdapter = new SingleTouchEventAdapter();
        singleTouchEventAdapter.addListener(pointerMoveToKeyAdapter, joystickVisualiser);
        TouchArea touchArea = new TouchArea(f, f2, f3, f4, singleTouchEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, new TouchScreenControlVisualizer[]{joystickVisualiser});
    }

    public SimpleTouchScreenControl createRealPointerArea(ViewFacade viewFacade, float f, float f2, float f3, float f4) {
        FingerTouchAdapter fingerTouchAdapter = new FingerTouchAdapter(new PointerEventReporter(this.viewOfXServer));
        SingleTouchEventAdapter singleTouchEventAdapter = new SingleTouchEventAdapter();
        singleTouchEventAdapter.addListener(fingerTouchAdapter);
        TouchArea touchArea = new TouchArea(f, f2, f3, f4, singleTouchEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, null);
    }
}
