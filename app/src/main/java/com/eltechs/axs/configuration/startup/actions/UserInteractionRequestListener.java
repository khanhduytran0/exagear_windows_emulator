package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.activities.FrameworkActivity;
import java.io.Serializable;

public interface UserInteractionRequestListener {
    void requestUserInput(Class<? extends FrameworkActivity> cls, Serializable serializable);
}
