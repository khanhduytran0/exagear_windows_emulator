package com.eltechs.axs.configuration.startup;

import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import com.eltechs.axs.Globals;
import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
// import com.eltechs.axs.applicationState.PurchasableComponentsCollectionAware;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class DetectedExecutableFile<StateClass> {
    private String controlsId;
    private DialogFragment controlsInfoDialog;
    private final EnvironmentCustomisationParameters defaultEnvironmentCustomisationParameters;
    private final XServerDisplayActivityInterfaceOverlay defaultUiOverlay;
    private final String description;
    private final EnvironmentCustomisationParameters environmentCustomisationParameters;
    private final String fileName;
    private final List<StartupAction<StateClass>> furtherStartupActions;
    private final Bitmap icon;
    private final File parentDir;
	
    public DetectedExecutableFile(File file, String str, Bitmap bitmap, String str2, Object purchasableComponent, XServerDisplayActivityInterfaceOverlay xServerDisplayActivityInterfaceOverlay, EnvironmentCustomisationParameters environmentCustomisationParameters2, List<StartupAction<StateClass>> list) {
        this.parentDir = file;
        this.fileName = str;
        this.icon = bitmap;
        this.description = str2;
        this.defaultUiOverlay = xServerDisplayActivityInterfaceOverlay;
        this.environmentCustomisationParameters = environmentCustomisationParameters2;
        this.defaultEnvironmentCustomisationParameters = new EnvironmentCustomisationParameters();
        this.defaultEnvironmentCustomisationParameters.copyFrom(environmentCustomisationParameters2);
        this.furtherStartupActions = list;
        this.controlsInfoDialog = null;
    }

    public DetectedExecutableFile(EnvironmentCustomisationParameters environmentCustomisationParameters2, String str, DialogFragment dialogFragment) {
        this(new File("dummyParentDir"), "dummyFileName", null, null, null, null, environmentCustomisationParameters2, Collections.EMPTY_LIST);
        this.controlsId = str;
        this.controlsInfoDialog = dialogFragment;
    }

    public File getParentDir() {
        return this.parentDir;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Bitmap getIcon() {
        return this.icon;
    }

    public String getDescription() {
        return this.description;
    }

    public List<StartupAction<StateClass>> getFurtherStartupActions() {
        return this.furtherStartupActions;
    }

    public String getControlsId() {
        return this.controlsId;
    }

    public DialogFragment getControlsInfoDialog() {
        return this.controlsInfoDialog;
    }

    public EnvironmentCustomisationParameters getDefaultEnvironmentCustomisationParameters() {
        return this.defaultEnvironmentCustomisationParameters;
    }

    public EnvironmentCustomisationParameters getEnvironmentCustomisationParameters() {
        return this.environmentCustomisationParameters;
    }

    public XServerDisplayActivityInterfaceOverlay getDefaultUiOverlay() {
        return this.defaultUiOverlay;
    }
}
