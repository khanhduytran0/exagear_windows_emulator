package com.eltechs.axs.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.eltechs.axs.AppConfig;
import com.eltechs.axs.Globals;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.DateHelper;
import java.io.Serializable;
import java.util.Calendar;

public class FrameworkActivity<StateClass extends ApplicationStateBase> extends AxsActivity {
    private static final String EXTRA_PARAMETER_NAME = "ExtraParameter";
    private static final int GA_KEEPALIVE_DELAY = 30000;
    /* access modifiers changed from: private */
    public CountDownTimer gaKeepaliveTimer = null;

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (this.gaKeepaliveTimer == null) {
            CountDownTimer r1 = new CountDownTimer(DateHelper.MSEC_IN_DAY, 30000) {
                public void onTick(long j) {
                    if (FrameworkActivity.this.isActivityResumed()) {
                        AppConfig.getInstance(FrameworkActivity.this.getApplicationContext()).setLastSessionTime(Calendar.getInstance().getTime());
                    }
                }

                public void onFinish() {
                    FrameworkActivity.this.gaKeepaliveTimer.start();
                }
            };
            this.gaKeepaliveTimer = r1;
            this.gaKeepaliveTimer.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getApplicationState().setCurrentActivity(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        ApplicationStateBase applicationState = getApplicationState();
        if (applicationState != null) {
            applicationState.setCurrentActivity(null);
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        
        if (this.gaKeepaliveTimer != null) {
            this.gaKeepaliveTimer.cancel();
            this.gaKeepaliveTimer = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: protected */
    public final StateClass getApplicationState() {
        return (StateClass) (ApplicationStateBase) Globals.getApplicationState();
    }

    protected static <T extends Serializable> void writeExtraParameter(Intent intent, T t) {
        intent.putExtra(EXTRA_PARAMETER_NAME, t);
    }

    /* access modifiers changed from: protected */
    public final <T extends Serializable> T getExtraParameter() {
        return (T) getIntent().getSerializableExtra(EXTRA_PARAMETER_NAME);
    }

    public static Intent createIntent(FrameworkActivity frameworkActivity, Class<? extends FrameworkActivity> cls, Serializable serializable) {
        Intent intent = new Intent(frameworkActivity, cls);
        if (serializable != null) {
            writeExtraParameter(intent, serializable);
        }
        return intent;
    }

    /* access modifiers changed from: protected */
    public void startActivity(Class<? extends FrameworkActivity> cls) {
        startActivity(createIntent(this, cls, null));
    }

    /* access modifiers changed from: protected */
    public void startActivity(Class<? extends FrameworkActivity> cls, Serializable serializable) {
        startActivity(createIntent(this, cls, serializable));
    }

    /* access modifiers changed from: protected */
    public void startActivityForResult(int i, Class<? extends FrameworkActivity> cls) {
        startActivityForResult(i, cls, null);
    }

    /* access modifiers changed from: protected */
    public void startActivityForResult(int i, Class<? extends FrameworkActivity> cls, Serializable serializable) {
        startActivityForResult(createIntent(this, cls, serializable), i);
    }

    /* access modifiers changed from: protected */
    public void resizeRootViewToStandardDialogueSize() {
        resizeViewToFractionOfScreenSize(getWindow().getDecorView().findViewById(16908290), 80, 80);
    }

    /* access modifiers changed from: protected */
    public void resizeViewToFractionOfScreenSize(View view, int i, int i2) {
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        LayoutParams layoutParams = view.getLayoutParams();
        int i3 = (displayMetrics.widthPixels * i) / 100;
        int i4 = (displayMetrics.heightPixels * i2) / 100;
        layoutParams.width = i3;
        layoutParams.height = i4;
    }

    /* access modifiers changed from: protected */
    public void signalUserInteractionFinished() {
        signalUserInteractionFinished(null);
    }

    /* access modifiers changed from: protected */
    public <T extends Serializable> void signalUserInteractionFinished(T t) {
        setResultEx(2, t);
        finish();
    }
}
