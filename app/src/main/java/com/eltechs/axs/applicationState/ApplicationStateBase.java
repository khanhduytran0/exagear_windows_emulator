package com.eltechs.axs.applicationState;

import com.eltechs.axs.applicationState.ApplicationStateBase;

public interface ApplicationStateBase<StateClass extends ApplicationStateBase<StateClass>> extends StartupActionsCollectionAware<StateClass>, EnvironmentAware, CurrentActivityAware, DroidApplicationContextAware {
}
