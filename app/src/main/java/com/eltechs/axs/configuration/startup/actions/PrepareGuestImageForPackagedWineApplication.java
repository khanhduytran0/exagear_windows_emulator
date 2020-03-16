package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.ExagearImageConfiguration.ExagearImageConfigurationHelpers;
import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.environmentService.components.NativeLibsConfiguration;
import java.io.IOException;

public class PrepareGuestImageForPackagedWineApplication<StateClass extends ExagearImageAware> extends AbstractStartupAction<StateClass> {
    public void execute() {
        try {
            new ExagearImageConfigurationHelpers(((ExagearImageAware) getApplicationState()).getExagearImage()).prepareWineForCurrentMemoryConfiguration(new NativeLibsConfiguration(getAppContext()));
            sendDone();
        } catch (IOException e) {
            sendError("Failed to prepare the unpacked exagear image for the game being started.", e);
        }
    }
}
