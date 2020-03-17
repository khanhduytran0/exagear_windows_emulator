package com.eltechs.axs.applicationState;

import android.content.Context;

public interface DroidApplicationContextAware {
    Context getAndroidApplicationContext();

    void setAndroidApplicationContext(Context context);
}
