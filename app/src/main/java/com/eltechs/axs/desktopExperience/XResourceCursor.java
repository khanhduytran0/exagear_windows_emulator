package com.eltechs.axs.desktopExperience;

import java.util.HashMap;
import java.util.Map;

public class XResourceCursor implements XResource {
    private final int size;
    private final String theme;
    private final boolean themeCore;

    public String getName() {
        return "Xcursor";
    }

    public XResourceCursor(int i, String str, boolean z) {
        this.size = i;
        this.theme = str;
        this.themeCore = z;
    }

    public Map<String, String> getKeyValPairs() {
        HashMap hashMap = new HashMap();
        hashMap.put("size", String.valueOf(this.size));
        hashMap.put("theme", this.theme);
        hashMap.put("theme_core", String.valueOf(true));
        return hashMap;
    }
}
