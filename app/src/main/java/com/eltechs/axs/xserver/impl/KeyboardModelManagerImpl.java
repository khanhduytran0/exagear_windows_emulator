package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.KeyboardModel;
import com.eltechs.axs.xserver.KeyboardModelManager;

public class KeyboardModelManagerImpl implements KeyboardModelManager {
    private KeyboardModel keyboardModel;

    public KeyboardModelManagerImpl(KeyboardModel keyboardModel2) {
        this.keyboardModel = keyboardModel2;
    }

    public KeyboardModel getKeyboardModel() {
        return this.keyboardModel;
    }

    public void setKeyboardModel(KeyboardModel keyboardModel2) {
        Assert.notImplementedYet();
    }
}
