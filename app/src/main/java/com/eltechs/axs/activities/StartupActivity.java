package com.eltechs.axs.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.eltechs.axs.AppConfig;
import com.eltechs.axs.Globals;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.applicationState.ApplicationStateObject;
import com.eltechs.axs.configuration.startup.StartupAction;
import com.eltechs.axs.configuration.startup.StartupActionInfo;
import com.eltechs.axs.configuration.startup.StartupActionsCollection;
import com.eltechs.axs.configuration.startup.actions.StartupActionCompletionListener;
import com.eltechs.axs.configuration.startup.actions.StartupStepInfoListener;
import com.eltechs.axs.configuration.startup.actions.UserInteractionRequestListener;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.UiThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;

public abstract class StartupActivity<StateClass extends ApplicationStateBase<StateClass>> extends FrameworkActivity<StateClass> implements StartupActionCompletionListener, UserInteractionRequestListener, StartupStepInfoListener {
    public static final int REQUEST_CODE_GET_PERMISSIONS = 10002;
    public static final int REQUEST_CODE_GET_USER_INPUT = 10001;
    private static final String RESTART_AFTER_SHUTDOWN_FLAG_VALUE = "restart";
    public static final int RESULT_CODE_GOT_USER_INPUT = 2;
    private static final String SHUTDOWN_REQUEST_FLAG = "just die already";
    public static final String STATE_PROGRESS_DESCR = "PROGRESS_DESCR";
    public static final String STATE_PROGRESS_FILENAME = "PROGRESS_FILENAME";
    public static final String TAG = "StartupActivity";
    private static final long progressUpdateInterval = 200;
    private static boolean shutdownInProgress;
    private boolean isUpdateProgress = false;
    private Class<? extends FrameworkActivity> mainActivityClass = XServerDisplayActivity.class;
    private String progressDescription = null;
    private String progressFileName = null;

    public void finish() {
    }

    /* access modifiers changed from: protected */
    public abstract void initialiseStartupActions();

    protected StartupActivity(Class<StateClass> cls) {
        if (Globals.getApplicationState() == null) {
            Globals.setApplicationState(new ApplicationStateObject(cls));
		}
    }

    /* access modifiers changed from: protected */
    public void setMainActivityClass(Class<? extends FrameworkActivity> cls) {
        this.mainActivityClass = cls;
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(STATE_PROGRESS_DESCR, this.progressDescription);
        bundle.putString(STATE_PROGRESS_FILENAME, this.progressFileName);
        super.onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.progressDescription = bundle.getString(STATE_PROGRESS_DESCR);
        this.progressFileName = bundle.getString(STATE_PROGRESS_FILENAME);
    }

    /* access modifiers changed from: protected */
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!maybeShutdown(getIntent())) {
            if (!isTaskRoot()) {
                super.finish();
                return;
            }
            setupUI();
            ApplicationStateBase applicationState = getApplicationState();
            String str = null;
            if (applicationState.getStartupActionsCollection() == null) {
                applicationState.setAndroidApplicationContext(getApplicationContext());
                applicationState.setStartupActionsCollection(new StartupActionsCollection(getApplicationContext()));
                AppConfig instance = AppConfig.getInstance(this);
                if (instance.getFirstRunTime().getTime() == 0) {
                    instance.setFirstRunTime(Calendar.getInstance().getTime());
                }
                boolean booleanExtra = getIntent().getBooleanExtra("RUN_AFTER_NOTIFICATION", false);
                instance.setRunAfterNotification(booleanExtra);
                if (booleanExtra) {
                    str = getIntent().getStringExtra("NOTIFICATION_NAME");
                }
                instance.setNotificationName(str);
                initialiseStartupActions();
                moveToNextAction();
            } else if (applicationState.getStartupActionsCollection().isFinished()) {
                startupFinished(null);
            }
        }
    }

    private void resetProgressToDefault() {
        ((ProgressBar) findViewById(R.id.startupProgressBar)).setIndeterminate(true);
        ((TextView) findViewById(R.id.sa_step_description)).setText(this.progressDescription);
    }

    private void updateProgressPost() {
        UiThread.postDelayed(progressUpdateInterval, new Runnable() {
            public void run() {
                StartupActivity.this.updateProgress();
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateProgress() {
        int i;
        if (this.progressFileName == null) {
            resetProgressToDefault();
            this.isUpdateProgress = false;
        }
        if (this.isUpdateProgress) {
            File file = new File(this.progressFileName);
            if (!file.exists()) {
                updateProgressPost();
                return;
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readLine = bufferedReader.readLine();
                String readLine2 = bufferedReader.readLine();
                bufferedReader.close();
                try {
                    i = Math.min(Integer.parseInt(readLine), 100);
                } catch (Exception unused) {
                    i = -1;
                }
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.startupProgressBar);
                if (i == -1) {
                    progressBar.setIndeterminate(true);
                } else {
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(i);
                }
                TextView textView = (TextView) findViewById(R.id.sa_step_description);
                if (readLine2 == null || readLine2 == "") {
                    textView.setText(null);
                } else {
                    textView.setText(readLine2);
                }
            } catch (IOException unused2) {
            }
            updateProgressPost();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.isUpdateProgress = false;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.isUpdateProgress = true;
        updateProgressPost();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        maybeShutdown(intent);
    }

    private boolean maybeShutdown(Intent intent) {
        String stringExtra = intent.getStringExtra(SHUTDOWN_REQUEST_FLAG);
        if (stringExtra == null) {
            return false;
        }
        logInfo("This is shutdown.");
        super.finish();
        Globals.clearState();
        shutdownInProgress = false;
        if (stringExtra.equals(RESTART_AFTER_SHUTDOWN_FLAG_VALUE)) {
            logInfo("Will restart after shutdown.");
			
			Intent intent2 = new Intent(this, AndroidHelpers.getAppLaunchActivityClass(this));
			intent2.setFlags(270565376);
			startActivity(intent2);
        }
        return true;
    }

    private void setupUI() {
        setContentView(R.layout.startup);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
		Assert.isTrue(i == 10001, String.format("Received a response to an unknown request %d; can only issue REQUEST_CODE_GET_USER_INPUT.", new Object[]{Integer.valueOf(i)}));
		Assert.isTrue(i2 == 0 || i2 == 2, String.format("Received an invalid resultCode %d.", new Object[]{Integer.valueOf(i2)}));
		if (i2 == 2) {
			getStartupActions().userInteractionFinished(getResultEx(intent));
		} else {
			getStartupActions().userInteractionCanceled();
		}
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 10002) {
            getStartupActions().userInteractionFinished(null);
        }
    }

    /* access modifiers changed from: protected */
    public StartupActionsCollection getStartupActions() {
        return getApplicationState().getStartupActionsCollection();
    }

    public void requestUserInput(Class<? extends FrameworkActivity> cls, Serializable serializable) {
        startActivityForResult(createIntent(this, cls, serializable), REQUEST_CODE_GET_USER_INPUT);
    }

    public void actionDone(StartupAction startupAction) {
        logDebug("actionDone(%s)", startupAction);
        moveToNextAction();
    }

    public void actionFailed(StartupAction startupAction, String str) {
        logDebug("actionFailed(%s, '%s')", startupAction, str);
        FatalErrorActivity.showFatalError(str);
    }

    public void setStepInfo(StartupActionInfo startupActionInfo) {
        this.progressDescription = startupActionInfo.getStepDescription();
        this.progressFileName = startupActionInfo.getProgressFilename();
        if (this.progressFileName != null) {
            File file = new File(this.progressFileName);
            if (file.exists()) {
                file.delete();
            }
        }
        this.isUpdateProgress = true;
        updateProgress();
    }

    public static void shutdownAXSApplication() {
        shutdownAXSApplication(false);
    }

    public static void shutdownAXSApplication(boolean z) {
        Context context;
        if (!shutdownInProgress) {
            ApplicationStateBase applicationStateBase = (ApplicationStateBase) Globals.getApplicationState();
            boolean z2 = applicationStateBase.getCurrentActivity() != null;
            if (z2) {
                context = applicationStateBase.getCurrentActivity();
            } else {
                context = applicationStateBase.getAndroidApplicationContext();
            }
            Intent intent = new Intent(context, AndroidHelpers.getAppLaunchActivityClass(context));
            intent.setFlags(1149239296);
            if (!z2) {
                intent.addFlags(268435456);
            }
            intent.putExtra(SHUTDOWN_REQUEST_FLAG, z ? RESTART_AFTER_SHUTDOWN_FLAG_VALUE : "");
            context.startActivity(intent);
            shutdownInProgress = true;
        }
    }

    private void moveToNextAction() {
        setStepInfo(new StartupActionInfo(""));
        if (!getStartupActions().runAction()) {
            startupFinished(null);
        }
    }

    /* access modifiers changed from: protected */
    public void startupFinished(Class<?> cls) {
        Intent intent = new Intent(this, this.mainActivityClass);
        intent.setFlags(65536);
        intent.putExtra("facadeclass", cls);
        startActivity(intent);
    }
}
