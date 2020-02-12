package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.activities.FrameworkActivity;
import com.eltechs.axs.configuration.startup.InteractiveStartupAction;
import java.io.Serializable;

public abstract class InteractiveStartupActionBase<StateClass, UserResponseType extends Serializable> extends AbstractStartupAction<StateClass> implements InteractiveStartupAction<StateClass, UserResponseType> {
    /* access modifiers changed from: protected */
    public final void requestUserInput(Class<? extends FrameworkActivity> cls) {
        getStartupActions().requestUserInput(cls, null);
    }

    /* access modifiers changed from: protected */
    public final void requestUserInput(Class<? extends FrameworkActivity> cls, Serializable serializable) {
        getStartupActions().requestUserInput(cls, serializable);
    }

    /* access modifiers changed from: protected */
    public final void requestUserInput() {
        getStartupActions().requestUserInput();
    }
}
