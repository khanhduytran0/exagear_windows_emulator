package com.eltechs.axs.applicationState;

import com.eltechs.axs.configuration.startup.StartupActionsCollection;

public interface StartupActionsCollectionAware<StateClass> {
    StartupActionsCollection<StateClass> getStartupActionsCollection();

    void setStartupActionsCollection(StartupActionsCollection<StateClass> startupActionsCollection);
}
