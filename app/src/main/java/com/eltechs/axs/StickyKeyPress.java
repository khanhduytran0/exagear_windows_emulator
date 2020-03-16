package com.eltechs.axs;

import com.eltechs.axs.helpers.InfiniteTimer;
import com.eltechs.axs.xserver.ViewFacade;

public class StickyKeyPress {
    /* access modifiers changed from: private */
    public final ViewFacade facade;
    private final int intervalMs;
    /* access modifiers changed from: private */
    public final byte keyCode;
    private InfiniteTimer timer;

    public StickyKeyPress(int i, byte b, ViewFacade viewFacade) {
        this.intervalMs = i;
        this.keyCode = b;
        this.facade = viewFacade;
    }

    public void start() {
        if (this.timer != null) {
            cancel();
        }
        this.timer = new InfiniteTimer((long) this.intervalMs) {
            public void onTick(long j) {
                StickyKeyPress.this.facade.injectKeyType(StickyKeyPress.this.keyCode);
            }
        };
        this.timer.start();
    }

    public void cancel() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }
}
