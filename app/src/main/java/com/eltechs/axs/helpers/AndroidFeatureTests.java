package com.eltechs.axs.helpers;

import android.os.Build.VERSION;

public abstract class AndroidFeatureTests {

    public enum ApiLevel {
        ANDROID_4_4(19),
        ANDROID_4_0(14),
        ANDROID_3_0(11),
        ANDROID_2_3_3(10);
        
        /* access modifiers changed from: private */
        public final int numericLevel;

        private ApiLevel(int i) {
            this.numericLevel = i;
        }
    }

    private AndroidFeatureTests() {
    }

    public static boolean haveAndroidApi(ApiLevel apiLevel) {
        return VERSION.SDK_INT >= apiLevel.numericLevel;
    }
}
