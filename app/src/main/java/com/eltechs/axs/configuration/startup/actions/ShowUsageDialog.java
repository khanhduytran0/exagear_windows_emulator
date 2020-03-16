package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.Globals;
import com.eltechs.axs.ShadowApplicationConfigurationAccessor;
import com.eltechs.axs.activities.UsageActivity;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
// import com.eltechs.axs.payments.PurchasableComponent;

public class ShowUsageDialog<StateClass> extends SimpleInteractiveStartupActionBase<StateClass> {
    public void execute() {
		/*
        PurchasableComponent effectiveCustomizationPackage = ((SelectedExecutableFileAware) Globals.getApplicationState()).getSelectedExecutableFile().getEffectiveCustomizationPackage();
        ShadowApplicationConfigurationAccessor shadowApplicationConfigurationAccessor = new ShadowApplicationConfigurationAccessor(effectiveCustomizationPackage.getName());
        if (!shadowApplicationConfigurationAccessor.isUsageShown()) {
            requestUserInput(UsageActivity.class, Integer.valueOf(effectiveCustomizationPackage.getInfoResId()));
            shadowApplicationConfigurationAccessor.setUsageShown(true);
            return;
        }
		*/
        sendDone();
    }

    public void userInteractionFinished() {
        sendDone();
    }

    public void userInteractionCanceled() {
        sendDone();
    }
}
