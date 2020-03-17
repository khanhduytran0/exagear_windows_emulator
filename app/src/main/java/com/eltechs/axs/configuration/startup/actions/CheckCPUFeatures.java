package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.Globals;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.helpers.EnvironmentInfoHelpers;

public class CheckCPUFeatures extends AbstractStartupAction<ApplicationStateBase<?>> {
    private final RequiredCPUFeatures requiredCPUFeatures;

    public static class RequiredCPUFeatures {
        final boolean isNeonRequired;

        public RequiredCPUFeatures(boolean z) {
            this.isNeonRequired = z;
        }
    }

    public CheckCPUFeatures(RequiredCPUFeatures requiredCPUFeatures2) {
        this.requiredCPUFeatures = requiredCPUFeatures2;
    }

    public void execute() {
        if (!EnvironmentInfoHelpers.canRunUbtOnCpu(false)) {
            sendError("Sorry, your CPU is not supported.");
        } else if (!this.requiredCPUFeatures.isNeonRequired || EnvironmentInfoHelpers.canRunUbtOnCpu(true)) {
            sendDone();
        } else {
            sendError("No NEON support.");
        }
    }
}
