package com.eltechs.axs.applicationState;

import com.eltechs.axs.configuration.UBTLaunchConfiguration;

public interface UBTLaunchConfigurationAware {
    UBTLaunchConfiguration getUBTLaunchConfiguration();

    void setUBTLaunchConfiguration(UBTLaunchConfiguration uBTLaunchConfiguration);
}
