package com.eltechs.axs;

import android.content.Context;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.applicationState.ApplicationStateObject;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.applicationState.*;

public abstract class Globals {
    private static ApplicationStateObject applicationState;

    private Globals() {
    }

    public static <T extends ApplicationStateBase> void setApplicationState(ApplicationStateObject<T> applicationStateObject) {
        Assert.state(applicationState == null, "Application state object already created.");
        applicationState = applicationStateObject;
    }

    public static <T> T getApplicationState() {
        if (applicationState != null) {
            return (T) applicationState.getState();
        }
        return null;
    }

    public static void clearState() {
        if (applicationState != null) {
            applicationState.clear();
            applicationState = null;
        }
    }

    public static Context getAppContext() {
        return ((ApplicationStateBase) getApplicationState()).getAndroidApplicationContext();
    }
}
