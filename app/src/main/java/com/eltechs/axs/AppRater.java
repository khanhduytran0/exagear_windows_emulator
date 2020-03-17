package com.eltechs.axs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import com.eltechs.ed.R;

public class AppRater {
    private static final int DAYS_FOR_DIALOG = 7;
    private static final int LAUNCHES_FOR_DIALOG = 5;
    private RateDialog rateDialog;

    private class RateDialog extends AlertDialog {
        final Context context;

        public RateDialog(Context context2) {
            super(context2);
            this.context = context2;
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            TableLayout tableLayout = (TableLayout) LayoutInflater.from(this.context).inflate(R.layout.rate_dialog, null);
            setContentView(tableLayout);
            ((Button) tableLayout.findViewById(R.id.rate_now)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AppRater.this.startRateActivity(RateDialog.this.context);
                    AppRater.this.dismissDialog(RateDialog.this.context);
                }
            });
            ((Button) tableLayout.findViewById(R.id.remind_me_later)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AppRater.this.delayShow(RateDialog.this.context);
                    AppRater.this.dismissDialog(RateDialog.this.context);
                }
            });
        }
    }

    public boolean maybeShowDialog(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("exadroid_rate", 0);
        Editor edit = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("rate_clicked", false)) {
            return false;
        }
        long j = sharedPreferences.getLong("next_dialog_date", 0);
        if (j == 0) {
            edit.putLong("next_dialog_date", System.currentTimeMillis());
        }
        int i = sharedPreferences.getInt("launch_count", 0);
        edit.putInt("launch_count", i + 1);
        edit.commit();
        if (i < 5 || j > System.currentTimeMillis()) {
            return false;
        }
        showRateDialog(context);
        return true;
    }

    private void showRateDialog(Context context) {
        this.rateDialog = new RateDialog(context);
        this.rateDialog.show();
    }

    public void dismissDialog(Context context) {
        if (this.rateDialog != null) {
            Editor edit = context.getSharedPreferences("exadroid_rate", 0).edit();
            edit.putInt("launch_count", 0);
            edit.commit();
            this.rateDialog.dismiss();
            this.rateDialog = null;
        }
    }

    public void startRateActivity(Context context) {
        Editor edit = context.getSharedPreferences("exadroid_rate", 0).edit();
        edit.putBoolean("rate_clicked", true);
        edit.commit();
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(context.getPackageName());
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
    }

    public void delayShow(Context context) {
        Editor edit = context.getSharedPreferences("exadroid_rate", 0).edit();
        edit.putLong("next_dialog_date", System.currentTimeMillis() + 604800000);
        edit.commit();
    }
}
