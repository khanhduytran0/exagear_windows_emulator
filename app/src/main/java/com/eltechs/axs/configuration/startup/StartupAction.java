package com.eltechs.axs.configuration.startup;

public interface StartupAction<StateClass> {
    void attach(StartupActionsCollection<StateClass> startupActionsCollection);

    void execute();

    StartupActionInfo getInfo();
}
