package com.eltechs.ed;

import java.util.HashMap;
import com.eltechs.ed.R;

public class AppRunGuide {
    public static String ID_CIV3 = "civ3";
    public static String ID_DIVINE_DIVINITY = "divine_divinity";
    public static String ID_FALLOUT = "fallout";
    public static String ID_FALLOUT2 = "fallout2";
    public static final HashMap<String, AppRunGuide> guidesMap = new HashMap<String, AppRunGuide>() {
        {
            put(AppRunGuide.ID_DIVINE_DIVINITY, new AppRunGuide(R.string.cont_run_guide_divine_divinity_header, R.string.cont_run_guide_divine_divinity_body));
            put(AppRunGuide.ID_FALLOUT, new AppRunGuide(R.string.cont_run_guide_fallout_header, R.string.cont_run_guide_fallout_body));
            put(AppRunGuide.ID_FALLOUT2, new AppRunGuide(R.string.cont_run_guide_fallout2_header, R.string.cont_run_guide_fallout2_body));
            put(AppRunGuide.ID_CIV3, new AppRunGuide(R.string.cont_run_guide_civ3_header, R.string.cont_run_guide_civ3_body));
        }
    };
    public int mBodyRes;
    public int mHeaderRes;

    private AppRunGuide(int i, int i2) {
        this.mHeaderRes = i;
        this.mBodyRes = i2;
    }
}
