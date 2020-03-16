package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.ExagearImageConfiguration.ExagearImageConfigurationHelpers;
import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.environmentService.components.NativeLibsConfiguration;
import com.eltechs.axs.helpers.StringHelpers;
import java.io.File;
import java.io.IOException;

public class PrepareGuestImage<StateClass extends ExagearImageAware> extends AbstractStartupAction<StateClass> {
    private final String homeDir;
    private final File hostDirInUserArea;

    public PrepareGuestImage(String str, File file) {
        this.homeDir = str;
        this.hostDirInUserArea = file;
    }

    public void execute() {
        try {
            ExagearImageConfigurationHelpers exagearImageConfigurationHelpers = new ExagearImageConfigurationHelpers(((ExagearImageAware) getApplicationState()).getExagearImage());
            NativeLibsConfiguration nativeLibsConfiguration = new NativeLibsConfiguration(getAppContext());
            exagearImageConfigurationHelpers.createEtcPasswd(new File(this.homeDir).getName(), this.homeDir);
            exagearImageConfigurationHelpers.createVpathsList(StringHelpers.appendTrailingSlash(this.hostDirInUserArea.getAbsolutePath()), "/proc/", "/dev/");
            exagearImageConfigurationHelpers.recreateX11SocketDir();
            exagearImageConfigurationHelpers.recreateSoundSocketDir();
            exagearImageConfigurationHelpers.prepareWineForCurrentMemoryConfiguration(nativeLibsConfiguration);
            sendDone();
        } catch (IOException e) {
            sendError("Failed to prepare the unpacked exagear image for the game being started.", e);
        }
    }
}
