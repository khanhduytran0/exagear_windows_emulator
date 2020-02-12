package com.eltechs.axs.configuration.startup.actions;

import android.content.Context;
import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.helpers.ZipInstallerAssets;
import com.eltechs.axs.helpers.ZipInstallerAssets.InstallCallback;

public class UnpackAssetZip<StateClass extends ExagearImageAware> extends AbstractStartupAction<StateClass> {
    private final Context applicationContext;
    private final String assetFileName;

    public UnpackAssetZip(Context context, String str) {
        this.applicationContext = context;
        this.assetFileName = str;
    }

    public void execute() {
        ZipInstallerAssets.installIfNecessary(this.applicationContext, new InstallCallback() {
            public void installationFinished(String str) {
                UnpackAssetZip.this.sendDone();
            }

            public void installationFailed(String str) {
                UnpackAssetZip.this.sendError(str);
            }
        }, ((ExagearImageAware) getApplicationState()).getExagearImage().getPath(), this.assetFileName);
    }
}
