package com.eltechs.axs.applicationState;

import com.eltechs.axs.configuration.MemsplitConfiguration;

public interface MemsplitConfigurationAware {
    MemsplitConfiguration getMemsplitConfiguration();

    void setMemsplitConfiguration(MemsplitConfiguration memsplitConfiguration);
}
