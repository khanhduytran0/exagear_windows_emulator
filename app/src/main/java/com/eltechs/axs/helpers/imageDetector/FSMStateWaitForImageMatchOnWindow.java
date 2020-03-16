package com.eltechs.axs.helpers.imageDetector;

import com.eltechs.axs.finiteStateMachine.AbstractFSMState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.xserver.ViewFacade;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowContentModificationListener;

public class FSMStateWaitForImageMatchOnWindow extends AbstractFSMState implements WindowContentModificationListener {
    public static FSMEvent IMAGE_MATCHED = new FSMEvent() {
    };
    final ImageDetector comparer1;
    final ImageDetector comparer2;
    final ViewFacade viewFacade;
    final int windowHeight;
    final int windowWidth;

    public void frontBufferReplaced(Window window) {
    }

    public FSMStateWaitForImageMatchOnWindow(int i, int i2, Rectangle rectangle, Rectangle rectangle2, byte[] bArr, byte[] bArr2, ViewFacade viewFacade2) {
        this.windowWidth = i;
        this.windowHeight = i2;
        this.viewFacade = viewFacade2;
        this.comparer1 = new ImageDetector(bArr, rectangle);
        this.comparer2 = new ImageDetector(bArr2, rectangle2);
    }

    public void notifyBecomeActive() {
        System.out.printf("WAiting for match\n", new Object[0]);
        this.viewFacade.addWindowContentModificationListner(this);
    }

    public void notifyBecomeInactive() {
        this.viewFacade.removeWindowContentModificationListner(this);
    }

    public void contentChanged(Window window, int i, int i2, int i3, int i4) {
        if (this.windowWidth != window.getBoundingRectangle().width || this.windowHeight != window.getBoundingRectangle().height) {
            return;
        }
        if (this.comparer1.isSamplePresentInDrawable(window.getActiveBackingStore()) || this.comparer2.isSamplePresentInDrawable(window.getActiveBackingStore())) {
            sendEventIfActive(IMAGE_MATCHED);
        }
    }
}
