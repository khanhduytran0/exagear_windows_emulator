package com.eltechs.axs;

import android.graphics.Bitmap;
import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.ViewFacade;

public class ButtonTouchScreenControlFactory {
    private final ViewOfXServer host;

    public ButtonTouchScreenControlFactory(ViewOfXServer viewOfXServer) {
        this.host = viewOfXServer;
    }

    public SimpleTouchScreenControl createSimpleButton(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, float f4, Bitmap bitmap, float f5, KeyCodesX... keyCodesXArr) {
        ButtonEventReporter buttonEventReporter = new ButtonEventReporter(viewFacade, keyCodesXArr);
        float f6 = f;
        float f7 = f2;
        float f8 = f3;
        float f9 = f4;
        ButtonVisualizer buttonVisualizer = new ButtonVisualizer(f6, f7, f8, f9, bitmap, f5, graphicsSceneConfigurer);
        ButtonTouchEventAdapter buttonTouchEventAdapter = new ButtonTouchEventAdapter();
        buttonTouchEventAdapter.addListener(buttonEventReporter, buttonVisualizer);
        TouchArea touchArea = new TouchArea(f6, f7, f8, f9, buttonTouchEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, new TouchScreenControlVisualizer[]{buttonVisualizer});
    }

    public SimpleTouchScreenControl createSimpleCircleButton(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, Bitmap bitmap, float f4, KeyCodesX... keyCodesXArr) {
        ButtonEventReporter buttonEventReporter = new ButtonEventReporter(viewFacade, keyCodesXArr);
        CircleButtonVisualizer circleButtonVisualizer = new CircleButtonVisualizer(f, f2, f3, bitmap, f4, graphicsSceneConfigurer);
        ButtonTouchEventAdapter buttonTouchEventAdapter = new ButtonTouchEventAdapter();
        buttonTouchEventAdapter.addListener(buttonEventReporter, circleButtonVisualizer);
        TouchArea touchArea = new TouchArea(f - f3, f2 - f3, f + f3, f2 + f3, buttonTouchEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, new TouchScreenControlVisualizer[]{circleButtonVisualizer});
    }

    public SimpleTouchScreenControl createPressOnlyButton(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, float f4, Bitmap bitmap, float f5, KeyCodesX... keyCodesXArr) {
        ButtonEventReporter buttonEventReporter = new ButtonEventReporter(viewFacade, keyCodesXArr);
        float f6 = f;
        float f7 = f2;
        float f8 = f3;
        float f9 = f4;
        ButtonVisualizer buttonVisualizer = new ButtonVisualizer(f6, f7, f8, f9, bitmap, f5, graphicsSceneConfigurer);
        ButtonPressOnlyEventAdapter buttonPressOnlyEventAdapter = new ButtonPressOnlyEventAdapter();
        buttonPressOnlyEventAdapter.addListener(buttonEventReporter, buttonVisualizer);
        TouchArea touchArea = new TouchArea(f6, f7, f8, f9, buttonPressOnlyEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, new TouchScreenControlVisualizer[]{buttonVisualizer});
    }

    public SimpleTouchScreenControl createTapSquareButton(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, float f4, Bitmap bitmap, float f5, KeyCodesX... keyCodesXArr) {
        ButtonTapEventReporter buttonTapEventReporter = new ButtonTapEventReporter(viewFacade, keyCodesXArr);
        TouchScreenControlVisualizer[] touchScreenControlVisualizerArr = null;
        ButtonVisualizer buttonVisualizer = bitmap != null ? new ButtonVisualizer(f, f2, f3, f4, bitmap, f5, graphicsSceneConfigurer) : null;
        ButtonTapAdapter buttonTapAdapter = new ButtonTapAdapter();
        buttonTapAdapter.addListener(buttonTapEventReporter);
        TouchArea touchArea = new TouchArea(f, f2, f3, f4, buttonTapAdapter);
        TouchArea[] touchAreaArr = {touchArea};
        if (buttonVisualizer != null) {
            touchScreenControlVisualizerArr = new TouchScreenControlVisualizer[]{buttonVisualizer};
        }
        return new SimpleTouchScreenControl(touchAreaArr, touchScreenControlVisualizerArr);
    }

    public SimpleTouchScreenControl createPressOnlyCircleButton(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, Bitmap bitmap, float f4, KeyCodesX... keyCodesXArr) {
        ButtonEventReporter buttonEventReporter = new ButtonEventReporter(viewFacade, keyCodesXArr);
        CircleButtonVisualizer circleButtonVisualizer = new CircleButtonVisualizer(f, f2, f3, bitmap, f4, graphicsSceneConfigurer);
        ButtonPressOnlyEventAdapter buttonPressOnlyEventAdapter = new ButtonPressOnlyEventAdapter();
        buttonPressOnlyEventAdapter.addListener(buttonEventReporter, circleButtonVisualizer);
        TouchArea touchArea = new TouchArea(f - f3, f2 - f3, f + f3, f2 + f3, buttonPressOnlyEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, new TouchScreenControlVisualizer[]{circleButtonVisualizer});
    }

    public SimpleTouchScreenControl createMouseButton(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, float f4, Bitmap bitmap, float f5, int i) {
        MouseButtonEventReporter mouseButtonEventReporter = new MouseButtonEventReporter(new PointerEventReporter(this.host), i);
        float f6 = f;
        float f7 = f2;
        float f8 = f3;
        float f9 = f4;
        ButtonVisualizer buttonVisualizer = new ButtonVisualizer(f6, f7, f8, f9, bitmap, f5, graphicsSceneConfigurer);
        ButtonPressOnlyEventAdapter buttonPressOnlyEventAdapter = new ButtonPressOnlyEventAdapter();
        buttonPressOnlyEventAdapter.addListener(mouseButtonEventReporter, buttonVisualizer);
        TouchArea touchArea = new TouchArea(f6, f7, f8, f9, buttonPressOnlyEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, new TouchScreenControlVisualizer[]{buttonVisualizer});
    }
}
