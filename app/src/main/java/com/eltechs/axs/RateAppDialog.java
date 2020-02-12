package com.eltechs.axs;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog.Builder;
import com.eltechs.axs.helpers.DateHelper;
import java.util.Calendar;

public class RateAppDialog {
    private static final int MIN_GUEST_LAUNCHES = 10;
    private static final int SHOW_INTERVAL_DAYS = 2;

    public static void show(final Context context) {
        final AppConfig instance = AppConfig.getInstance(context);
        Builder builder = new Builder(context);
        builder.setTitle((CharSequence) "Rate this app");
        builder.setMessage((CharSequence) "If you enjoy this app, please take a moment to rate it. It won't take more than a minute. Thank you for your support!");
        builder.setPositiveButton((CharSequence) "Rate now", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
				/*
                String packageName = context.getPackageName();
                StringBuilder sb = new StringBuilder();
                sb.append("market://details?id=");
                sb.append(packageName);
                try {
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                } catch (ActivityNotFoundException unused) {
                    Context context = context;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("http://play.google.com/store/apps/details?id=");
                    sb2.append(packageName);
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb2.toString())));
                }
				*/
                instance.setRateAppDontShowAgain(true);
            }
        });
        builder.setNeutralButton((CharSequence) "No, thanks", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                instance.setRateAppDontShowAgain(true);
            }
        });
        builder.setNegativeButton((CharSequence) "Remind later", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        instance.setRateAppLastShowTime(Calendar.getInstance().getTime());
        builder.show();
    }

    public static void checkCondAndShow(Context context) {
        AppConfig instance = AppConfig.getInstance(context);
        if (!instance.getRateAppDontShowAgain() && instance.getGuestLaunchesCount() >= 10 && DateHelper.getDiffDays(Calendar.getInstance().getTime(), instance.getRateAppLastShowTime()) >= 2) {
            show(context);
        }
    }
}
