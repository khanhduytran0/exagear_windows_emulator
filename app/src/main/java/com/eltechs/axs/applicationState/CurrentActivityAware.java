package com.eltechs.axs.applicationState;

import com.eltechs.axs.activities.FrameworkActivity;

public interface CurrentActivityAware {
    FrameworkActivity getCurrentActivity();

    void setCurrentActivity(FrameworkActivity frameworkActivity);
}
