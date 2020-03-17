package com.eltechs.axs.configuration;

public class TouchScreenControlsInputConfiguration {
    public static TouchScreenControlsInputConfiguration DEFAULT = new TouchScreenControlsInputConfiguration(BackKeyAction.XKEYCODE);
    public final BackKeyAction backKeyAction;

    public enum BackKeyAction {
        XKEYCODE,
        SHOW_POPUP_MENU
    }

    public TouchScreenControlsInputConfiguration(BackKeyAction backKeyAction2) {
        this.backKeyAction = backKeyAction2;
    }
}
