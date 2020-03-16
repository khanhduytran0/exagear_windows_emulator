package com.eltechs.ed.startupActions;

import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;
import com.eltechs.axs.helpers.SafeFileHelpers;
import com.eltechs.axs.helpers.ZipInstallerAssets;
import com.eltechs.axs.helpers.ZipInstallerAssets.InstallCallback;
import com.eltechs.ed.guestContainers.GuestContainersManager;
import java.io.File;
import java.io.IOException;

public class InstallRecipesFromAssets<StateClass extends ExagearImageAware> extends AbstractStartupAction<StateClass> {
    public void execute() {
        final File file = new File(((ExagearImageAware) getApplicationState()).getExagearImage().getPath(), GuestContainersManager.RECIPES_GUEST_DIR);
        try {
            SafeFileHelpers.removeDirectory(file);
            ZipInstallerAssets.installIfNecessary(getAppContext(), new InstallCallback() {
                public void installationFinished(String str) {
					try {
						Runtime.getRuntime().exec("chmod -R 700 " + file.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					InstallRecipesFromAssets.this.sendDone();
                }

                public void installationFailed(String str) {
                    InstallRecipesFromAssets.this.sendError(str);
                }
            }, file, "recipe.zip");
        } catch (IOException e) {
            sendError(e.toString());
        }
    }
}
