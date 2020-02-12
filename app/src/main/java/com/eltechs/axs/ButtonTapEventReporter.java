package com.eltechs.axs;

import com.eltechs.axs.xserver.ViewFacade;

public class ButtonTapEventReporter implements ButtonTapEventListener {
    private final KeyCodesX[] keyCodes;
    private final KeyEventReporter keyEventReporter;

    public ButtonTapEventReporter(ViewFacade viewFacade, KeyCodesX... keyCodesXArr) {
        this.keyCodes = keyCodesXArr;
        this.keyEventReporter = new KeyEventReporter(viewFacade);
    }

    public void tapped() {
        KeyCodesX[] keyCodesXArr;
        for (KeyCodesX keyCodesX : this.keyCodes) {
            this.keyEventReporter.reportKeysPress(keyCodesX);
            this.keyEventReporter.reportKeysRelease(keyCodesX);
            try {
                Thread.sleep(50, 0);
            } catch (InterruptedException unused) {
            }
        }
    }
}
