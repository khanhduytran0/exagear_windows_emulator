package com.eltechs.axs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.eltechs.axs.AppConfig;
// import com.eltechs.axs.firebase.FAHelper;

public class NotificationClickActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String stringExtra = getIntent().getStringExtra("NOTIFICATION_NAME");
        StringBuilder sb = new StringBuilder();
        sb.append("Click on notification ");
        sb.append(stringExtra);
        Log.i("Notification", sb.toString());
        // FAHelper.logNotificationClickEvent(this, stringExtra);
        AppConfig.getInstance(this).setRunAfterNotification(true);
        AppConfig.getInstance(this).setNotificationName(stringExtra);
        Intent intent = (Intent) getIntent().getParcelableExtra("INTENT");
        intent.putExtra("RUN_AFTER_NOTIFICATION", true);
        intent.putExtra("NOTIFICATION_NAME", stringExtra);
        finish();
        startActivity(intent);
    }
}
