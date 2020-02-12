package com.eltechs.ed.startupActions;

import com.eltechs.axs.configuration.startup.AsyncStartupAction;
import com.eltechs.axs.configuration.startup.StartupActionInfo;
import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;
import com.eltechs.axs.helpers.UiThread;
import com.eltechs.ed.guestContainers.GuestContainersManager;

public class InitGuestContainersManager<StateClass> extends AbstractStartupAction<StateClass> implements AsyncStartupAction<StateClass> {
    public StartupActionInfo getInfo() {
        return new StartupActionInfo("Preparing containers...", null);
    }

    public void execute() {
        GuestContainersManager.getInstance(getAppContext());
        UiThread.post(new Runnable() {
            public void run() {
                InitGuestContainersManager.this.sendDone();
            }
        });
    }
}
