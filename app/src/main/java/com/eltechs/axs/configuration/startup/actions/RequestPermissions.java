package com.eltechs.axs.configuration.startup.actions;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class RequestPermissions<StateClass> extends SimpleInteractiveStartupActionBase<StateClass> {
    final int requestCode;
    final Activity thisActivity;

    public RequestPermissions(Activity activity, int i) {
        this.thisActivity = activity;
        this.requestCode = i;
    }

    public void execute() {
        if (ContextCompat.checkSelfPermission(this.thisActivity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestUserInput();
            ActivityCompat.requestPermissions(this.thisActivity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, this.requestCode);
            return;
        }
        sendDone();
    }

    public void userInteractionFinished() {
        sendDone();
    }

    public void userInteractionCanceled() {
        sendDone();
    }
}
