package com.eltechs.axs;

import com.eltechs.axs.xserver.ViewFacade;

public class ButtonEventReporter implements ButtonEventListener {
    private final KeyCodesX[] keyCodes;
    private final KeyEventReporter keyEventReporter;

    public ButtonEventReporter(ViewFacade viewFacade, KeyCodesX... keyCodesXArr) {
        this.keyCodes = keyCodesXArr;
        this.keyEventReporter = new KeyEventReporter(viewFacade);
    }

    public void pressed() {
        this.keyEventReporter.reportKeysPress(this.keyCodes);
    }

    public void released() {
        this.keyEventReporter.reportKeysRelease(this.keyCodes);
    }
}
