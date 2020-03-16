package com.eltechs.axs.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.eltechs.ed.R;

public class RateMeActivity extends FrameworkActivity {
    private static final int DAYS_FOR_DIALOG = 7;
    private static final int LAUNCHES_FOR_DIALOG = 5;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setResult(2);
        SharedPreferences sharedPreferences = getSharedPreferences("exadroid_rate", 0);
        Editor edit = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("rate_clicked", false)) {
            finish();
            return;
        }
        long j = sharedPreferences.getLong("next_dialog_date", 0);
        if (j == 0) {
            edit.putLong("next_dialog_date", System.currentTimeMillis());
        }
        int i = sharedPreferences.getInt("launch_count", 0);
        edit.putInt("launch_count", i + 1);
        edit.commit();
        if (i < 5 || j > System.currentTimeMillis()) {
            finish();
            return;
        }
        setContentView(R.layout.rate_me_activity);
        ((TextView) findViewById(R.id.rate_custom_title)).setText(getString(getApplicationInfo().labelRes));
    }

    public void onRateNowClicked(View view) {
        Editor edit = getSharedPreferences("exadroid_rate", 0).edit();
        edit.putBoolean("rate_clicked", true);
        edit.commit();
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(getPackageName());
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
        finish();
    }

    public void onRateLaterClicked(View view) {
        Editor edit = getSharedPreferences("exadroid_rate", 0).edit();
        edit.putLong("next_dialog_date", System.currentTimeMillis() + 604800000);
        edit.commit();
        finish();
    }
}
