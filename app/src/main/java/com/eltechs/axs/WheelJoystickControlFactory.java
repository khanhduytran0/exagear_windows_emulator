package com.eltechs.axs;

import android.graphics.Bitmap;
import com.eltechs.axs.graphicsScene.GraphicsSceneConfigurer;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.ViewFacade;

public class WheelJoystickControlFactory {
    private final ViewOfXServer viewOfXServer;

    public WheelJoystickControlFactory(ViewOfXServer viewOfXServer2) {
        this.viewOfXServer = viewOfXServer2;
    }

    public SimpleTouchScreenControl createSimpleWheel(GraphicsSceneConfigurer graphicsSceneConfigurer, ViewFacade viewFacade, float f, float f2, float f3, float f4, float f5, Bitmap bitmap, float f6) {
        ViewFacade viewFacade2 = viewFacade;
        WheelToPointerAdapter wheelToPointerAdapter = new WheelToPointerAdapter(viewFacade2, new PointerEventReporter(this.viewOfXServer), true, false);
        WheelJoystickEventAdapter wheelJoystickEventAdapter = new WheelJoystickEventAdapter(viewFacade2, f, f2, f3, f4, f5);
        WheelJoystickVisualizer wheelJoystickVisualizer = new WheelJoystickVisualizer(f, f2, f4, bitmap, f6, graphicsSceneConfigurer);
        wheelJoystickEventAdapter.addListener(wheelJoystickVisualizer, wheelToPointerAdapter);
        TouchArea touchArea = new TouchArea(f - f4, f2 - f4, f + f4, f2 + f4, wheelJoystickEventAdapter);
        return new SimpleTouchScreenControl(new TouchArea[]{touchArea}, new TouchScreenControlVisualizer[]{wheelJoystickVisualizer});
    }
}
