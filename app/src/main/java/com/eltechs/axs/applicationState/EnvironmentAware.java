package com.eltechs.axs.applicationState;

import com.eltechs.axs.configuration.XServerViewConfiguration;
import com.eltechs.axs.environmentService.AXSEnvironment;
import com.eltechs.axs.environmentService.AXSEnvironmentService;

public interface EnvironmentAware {
    AXSEnvironment getEnvironment();

    AXSEnvironmentService getEnvironmentServiceInstance();

    XServerViewConfiguration getXServerViewConfiguration();

    void setEnvironment(AXSEnvironment aXSEnvironment);

    void setEnvironmentServiceInstance(AXSEnvironmentService aXSEnvironmentService);

    void setXServerViewConfiguration(XServerViewConfiguration xServerViewConfiguration);
}
