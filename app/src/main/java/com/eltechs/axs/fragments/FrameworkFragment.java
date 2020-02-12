package com.eltechs.axs.fragments;

import android.support.v4.app.Fragment;
import com.eltechs.axs.Globals;
import com.eltechs.axs.applicationState.ApplicationStateBase;

public class FrameworkFragment<StateClass extends ApplicationStateBase> extends Fragment {
    /* access modifiers changed from: protected */
    public final StateClass getApplicationState() {
        return (StateClass) (ApplicationStateBase) Globals.getApplicationState();
    }
}
