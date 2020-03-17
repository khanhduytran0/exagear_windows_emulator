package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.geom.Point;
import com.eltechs.axs.xserver.ViewFacade;

public class Helpers {
    public static void adjustPointerPosition(ViewFacade viewFacade, int i) {
        Point pointerLocation = viewFacade.getPointerLocation();
        int i2 = pointerLocation.x;
        int i3 = pointerLocation.y;
        if (i2 < i) {
            i2 = i;
        } else if (i2 > viewFacade.getScreenInfo().widthInPixels - i) {
            i2 = viewFacade.getScreenInfo().widthInPixels - i;
        }
        if (i3 >= i) {
            i = i3 > viewFacade.getScreenInfo().heightInPixels - i ? viewFacade.getScreenInfo().heightInPixels - i : i3;
        }
        viewFacade.injectPointerMove(i2, i);
    }
}
