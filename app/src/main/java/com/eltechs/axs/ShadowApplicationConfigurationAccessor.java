package com.eltechs.axs;

import android.content.SharedPreferences.Editor;

public class ShadowApplicationConfigurationAccessor extends ApplicationConfigurationAccessor {
    private static final String SHADOW_PARAM_USAGE_PREFIX = "axs_shadow_usage_";
    private final String keyName;

    public ShadowApplicationConfigurationAccessor(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(SHADOW_PARAM_USAGE_PREFIX);
        sb.append(str);
        this.keyName = sb.toString();
    }

    public boolean isUsageShown() {
        return this.prefs.getBoolean(this.keyName, false);
    }

    public void setUsageShown(boolean z) {
        Editor edit = this.prefs.edit();
        edit.putBoolean(this.keyName, z);
        edit.commit();
    }
}
