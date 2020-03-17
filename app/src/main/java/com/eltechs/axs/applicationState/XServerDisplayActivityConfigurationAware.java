package com.eltechs.axs.applicationState;

import com.eltechs.axs.activities.XServerDisplayActivityConfiguration;
import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;

public interface XServerDisplayActivityConfigurationAware {
    XServerDisplayActivityConfiguration getXServerDisplayActivityConfiguration();

    XServerDisplayActivityInterfaceOverlay getXServerDisplayActivityInterfaceOverlay();

    void setXServerDisplayActivityConfiguration(XServerDisplayActivityConfiguration xServerDisplayActivityConfiguration);

    void setXServerDisplayActivityInterfaceOverlay(XServerDisplayActivityInterfaceOverlay xServerDisplayActivityInterfaceOverlay);
}
