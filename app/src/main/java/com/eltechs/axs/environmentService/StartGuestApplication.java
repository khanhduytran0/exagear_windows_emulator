package com.eltechs.axs.environmentService;

import com.eltechs.axs.activities.StartupActivity;
import com.eltechs.axs.applicationState.EnvironmentAware;
import com.eltechs.axs.applicationState.UBTLaunchConfigurationAware;
import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;
import com.eltechs.axs.environmentService.components.GuestApplicationsTrackerComponent;
import com.eltechs.axs.guestApplicationsTracker.GuestApplicationsLifecycleAdapter;
import com.eltechs.axs.guestApplicationsTracker.impl.Translator;

public class StartGuestApplication<StateClass extends UBTLaunchConfigurationAware & EnvironmentAware> extends AbstractStartupAction<StateClass> {
    /* access modifiers changed from: private */
    public final boolean restartAXSAfterShutdown;
    private final boolean terminateAXSOnGuestExit;

    public StartGuestApplication(boolean z) {
        this(z, false);
    }

    public StartGuestApplication(boolean z, boolean z2) {
        this.terminateAXSOnGuestExit = z;
        this.restartAXSAfterShutdown = z2;
    }

    public void execute() {
        UBTLaunchConfigurationAware uBTLaunchConfigurationAware = (UBTLaunchConfigurationAware) getApplicationState();
        final GuestApplicationsTrackerComponent guestApplicationsTrackerComponent = (GuestApplicationsTrackerComponent) ((EnvironmentAware) uBTLaunchConfigurationAware).getEnvironment().getComponent(GuestApplicationsTrackerComponent.class);
        guestApplicationsTrackerComponent.startGuestApplication(uBTLaunchConfigurationAware.getUBTLaunchConfiguration());
        if (this.terminateAXSOnGuestExit) {
            guestApplicationsTrackerComponent.addListener(new GuestApplicationsLifecycleAdapter() {
                public void translatorExited(Translator translator) {
                    if (!guestApplicationsTrackerComponent.haveGuestApplications()) {
                        StartupActivity.shutdownAXSApplication(StartGuestApplication.this.restartAXSAfterShutdown);
                    }
                }
            });
        }
        sendDone();
    }
}
