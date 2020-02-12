package com.eltechs.axs.helpers.imageDetector;

import com.eltechs.axs.finiteStateMachine.AbstractFSMState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.PlacedDrawable;
import com.eltechs.axs.xserver.ViewFacade;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowContentModificationListener;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import java.util.Iterator;

public class FSMStateWaitForImageUnmatchOnWindow extends AbstractFSMState implements WindowContentModificationListener, WindowLifecycleListener {
    public static FSMEvent IMAGE_UNMATCHED = new FSMEvent() {
    };
    public static FSMEvent TARGET_NOT_FOUND = new FSMEvent() {
    };
    final ImageDetector comparer1;
    final ImageDetector comparer2;
    Drawable target = null;
    final ViewFacade viewFacade;

    public void frontBufferReplaced(Window window) {
    }

    public void windowCreated(Window window) {
    }

    public void windowDestroyed(Window window) {
    }

    public void windowMapped(Window window) {
    }

    public void windowReparented(Window window, Window window2) {
    }

    public void windowZOrderChange(Window window) {
    }

    public FSMStateWaitForImageUnmatchOnWindow(int i, int i2, Rectangle rectangle, Rectangle rectangle2, byte[] bArr, byte[] bArr2, ViewFacade viewFacade2) {
        this.viewFacade = viewFacade2;
        this.comparer1 = new ImageDetector(bArr, rectangle);
        this.comparer2 = new ImageDetector(bArr2, rectangle2);
    }

    public void notifyBecomeActive() {
        boolean z = false;
        System.out.printf("WAiting for unmatch\n", new Object[0]);
        if (this.target == null) {
            z = true;
        }
        Assert.state(z);
        Iterator it = this.viewFacade.listNonRootWindowDrawables().iterator();
        while (it.hasNext()) {
            PlacedDrawable placedDrawable = (PlacedDrawable) it.next();
            if (checkMatch(placedDrawable.getDrawable())) {
                this.target = placedDrawable.getDrawable();
                this.viewFacade.addWindowContentModificationListner(this);
                this.viewFacade.addWindowLifecycleListener(this);
                return;
            }
        }
        sendEvent(TARGET_NOT_FOUND);
    }

    /* access modifiers changed from: 0000 */
    public boolean checkMatch(Drawable drawable) {
        return this.comparer1.isSamplePresentInDrawable(drawable) || this.comparer2.isSamplePresentInDrawable(drawable);
    }

    public void notifyBecomeInactive() {
        if (this.target != null) {
            this.viewFacade.removeWindowContentModificationListner(this);
            this.viewFacade.removeWindowLifecycleListener(this);
            this.target = null;
        }
    }

    public void contentChanged(Window window, int i, int i2, int i3, int i4) {
        Assert.state(this.target != null);
        if (window.getActiveBackingStore() == this.target && !checkMatch(this.target)) {
            System.out.printf("image unmatched\n", new Object[0]);
            sendEventIfActive(IMAGE_UNMATCHED);
        }
    }

    public void windowUnmapped(Window window) {
        Assert.state(this.target != null);
        if (window.getActiveBackingStore() == this.target) {
            System.out.printf("image unmatched (d gone)\n", new Object[0]);
            sendEventIfActive(IMAGE_UNMATCHED);
        }
    }
}
