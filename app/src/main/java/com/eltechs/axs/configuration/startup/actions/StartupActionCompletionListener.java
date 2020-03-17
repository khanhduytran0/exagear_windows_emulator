package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.configuration.startup.StartupAction;

public interface StartupActionCompletionListener {
    void actionDone(StartupAction startupAction);

    void actionFailed(StartupAction startupAction, String str);
}
