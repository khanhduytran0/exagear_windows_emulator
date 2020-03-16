package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.applicationState.XServerDisplayActivityConfigurationAware;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;
// import com.eltechs.axs.payments.PurchasableComponent;

public class SetUIOverlay<StateClass extends XServerDisplayActivityConfigurationAware & SelectedExecutableFileAware<StateClass>> extends AbstractStartupAction<StateClass> {
    public void execute() {
        DetectedExecutableFile selectedExecutableFile = ((SelectedExecutableFileAware) ((XServerDisplayActivityConfigurationAware) getApplicationState())).getSelectedExecutableFile();
		/*
        PurchasableComponent effectiveCustomizationPackage = selectedExecutableFile.getEffectiveCustomizationPackage();
        if (effectiveCustomizationPackage != selectedExecutableFile.getRecommendedCustomizationPackage()) {
            ((XServerDisplayActivityConfigurationAware) getApplicationState()).setXServerDisplayActivityInterfaceOverlay(effectiveCustomizationPackage.getUiOverlay());
        } else { */
            ((XServerDisplayActivityConfigurationAware) getApplicationState()).setXServerDisplayActivityInterfaceOverlay(selectedExecutableFile.getDefaultUiOverlay());
        // }
        sendDone();
    }
}
