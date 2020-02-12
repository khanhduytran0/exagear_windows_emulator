package com.eltechs.axs.activities;

import android.content.Intent;
import android.os.Bundle;
import com.eltechs.axs.helpers.AndroidHelpers;

public class SwitchToAxsFromSystemTrayActivity extends AxsActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Intent intent = new Intent(this, AndroidHelpers.getAppLaunchActivityClass(this));
        intent.putExtras(getIntent());
        intent.setFlags(268468224);
        finish();
        startActivity(intent);
    }
}
