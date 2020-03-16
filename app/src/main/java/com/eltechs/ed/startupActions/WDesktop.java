package com.eltechs.ed.startupActions;

import com.eltechs.axs.activities.StartupActivity;
import com.eltechs.axs.configuration.startup.actions.InteractiveStartupActionBase;
import com.eltechs.ed.activities.EDMainActivity;

public class WDesktop<StateClass> extends InteractiveStartupActionBase<StateClass, WDesktop.UserRequestedAction> {

    public enum UserRequestedAction {
        GO_FURTHER,
        RESTART_ME
    }

    public void execute() {
        requestUserInput(EDMainActivity.class);
    }

    public void userInteractionFinished(UserRequestedAction userRequestedAction) {
        switch (userRequestedAction) {
            case GO_FURTHER:
                sendDone();
                return;
            case RESTART_ME:
                getStartupActions().addAction(new InitGuestContainersManager());
                getStartupActions().addAction(new WDesktop());
                sendDone();
                return;
            default:
                return;
        }
    }

    public void userInteractionCanceled() {
        StartupActivity.shutdownAXSApplication();
    }
}
