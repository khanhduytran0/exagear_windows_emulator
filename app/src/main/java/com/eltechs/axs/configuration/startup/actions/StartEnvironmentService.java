package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.applicationState.EnvironmentAware;
import com.eltechs.axs.environmentService.AXSEnvironment.StartupCallback;
import com.eltechs.axs.environmentService.TrayConfiguration;

public class StartEnvironmentService<StateClass extends EnvironmentAware> extends AbstractStartupAction<StateClass> {
    private final TrayConfiguration trayConfiguration;

    public StartEnvironmentService(TrayConfiguration trayConfiguration2) {
        this.trayConfiguration = trayConfiguration2;
    }

    public void execute() {
        EnvironmentAware environmentAware = (EnvironmentAware) getApplicationState();
        environmentAware.getEnvironment().startEnvironmentService(new StartupCallback() {
            public void serviceStarted() {
                StartEnvironmentService.this.sendDone();
            }

            public void serviceFailed(Throwable th) {
                StartEnvironmentService.this.sendError("Failed to start the environment emulation service.", th);
            }
        }, this.trayConfiguration);
    }
}
