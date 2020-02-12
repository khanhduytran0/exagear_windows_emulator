package com.eltechs.ed.startupActions;

import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;

public class SendGAEvent<StateClass> extends AbstractStartupAction<StateClass> {
    final String action;
    final String category;
    final String label;
    final Long value;

    public SendGAEvent(String str, String str2, String str3, Long l) {
        this.category = str;
        this.action = str2;
        this.label = str3;
        this.value = l;
    }

    public void execute() {
        sendDone();
    }
}
