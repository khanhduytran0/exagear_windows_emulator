package com.eltechs.axs.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.Builder;
import com.eltechs.ed.R;
import com.eltechs.axs.activities.NotificationClickActivity;
import com.eltechs.axs.activities.SwitchToAxsFromSystemTrayActivity;

public class NotificationHelper {
    private static void displayNotification(Context context, String str, String str2, String str3, Intent intent) {
        Intent intent2 = new Intent(context, NotificationClickActivity.class);
        intent2.putExtra("INTENT", intent);
        intent2.putExtra("NOTIFICATION_NAME", str3);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent2, 1073741824);
        String string = context.getString(R.string.default_notification_channel_id);
        Builder defaults = new Builder(context, string).setSmallIcon(R.drawable.tray).setContentTitle(str).setContentText(str2).setAutoCancel(true).setContentIntent(activity).setPriority(0).setDefaults(-1);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
		/*
        if (VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel(string, "ExaGear", 3));
        }
		*/
        notificationManager.notify(0, defaults.build());
    }

    public static void displaySimpleNotification(Context context, String str, String str2, String str3) {
        displayNotification(context, str, str2, str3, new Intent(context, SwitchToAxsFromSystemTrayActivity.class));
    }

    public static void displayOpenUrlNotification(Context context, String str, String str2, String str3, String str4) {
        displayNotification(context, str, str2, str3, new Intent("android.intent.action.VIEW", Uri.parse(str4)));
    }
}
