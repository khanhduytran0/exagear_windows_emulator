package com.eltechs.axs;

import android.content.SharedPreferences.Editor;

public class CommonApplicationConfigurationAccessor extends ApplicationConfigurationAccessor {
    private static final String CONFIG_HORIZ_STRETCH = "axs_config_stretch";

    public boolean isHorizontalStretchEnabled() {
        return this.prefs.getBoolean(CONFIG_HORIZ_STRETCH, false);
    }

    public void setHorizontalStretchEnabled(boolean z) {
        Editor edit = this.prefs.edit();
        edit.putBoolean(CONFIG_HORIZ_STRETCH, z);
        edit.commit();
    }
}
