package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.activities.RateMeActivity;

public class ShowRateMeDialog<StateClass> extends SimpleInteractiveStartupActionBase<StateClass> {
    public void execute() {
        requestUserInput(RateMeActivity.class);
    }

    public void userInteractionFinished() {
        sendDone();
    }

    public void userInteractionCanceled() {
        sendDone();
    }
}
