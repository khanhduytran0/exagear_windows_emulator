package com.eltechs.axs.activities;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.activities.AxsDataFragment.DialogInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AxsActivity extends AppCompatActivity {
    private static final String DATA_FRAGMENT_TAG = "data";
    private static final String RESULT_VALUE_NAME = "AxsActivityResult";
    private static final Map<Class<? extends AxsActivity>, Map<String, BufferedListenerInvoker<?>>> bufferedListenerInvokers = new HashMap();
    private String TAG;
    private final List<ActivityResultHandler> activityResultHandlers;
    private AxsDataFragment dataFragment;
    private int firstFreeRequestCode;
    private boolean logEnabled;
    private boolean resumed;
    private boolean wasRecreated;
    private boolean wasRecreatedInited;

    public interface ActivityResultHandler {
        boolean handleActivityResult(int i, int i2, Intent intent);
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateRetainedDialog(String str) {
        return null;
    }

    protected AxsActivity() {
        this.TAG = "AxsActivity";
        this.logEnabled = true;
        this.resumed = false;
        this.activityResultHandlers = new ArrayList<ActivityResultHandler>();
        this.firstFreeRequestCode = 7666;
        this.TAG = getClass().getSimpleName();
        try {
            String str = (String) getClass().getField("TAG").get(this);
            if (str != null) {
                this.TAG = str;
            }
        } catch (IllegalAccessException | NoSuchFieldException unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        logDebug("onCreate() called");
        super.onCreate(bundle);
        logDebug(getIntent().toString());
        if (bundle != null) {
            logDebug(bundle.toString());
        }
        logDebug(getPackageName());
        StringBuilder sb = new StringBuilder();
        sb.append("task id: ");
        sb.append(getTaskId());
        logDebug(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("is root activity: ");
        sb2.append(isTaskRoot());
        logDebug(sb2.toString());
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.dataFragment = (AxsDataFragment) supportFragmentManager.findFragmentByTag(DATA_FRAGMENT_TAG);
        if (this.dataFragment == null) {
            this.dataFragment = new AxsDataFragment();
            FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
            beginTransaction.add((Fragment) this.dataFragment, DATA_FRAGMENT_TAG);
            beginTransaction.commit();
            logDebug("first time activity creation");
            return;
        }
        this.wasRecreated = true;
        this.wasRecreatedInited = true;
        logDebug("activity recreation");
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        logDebug("onRestart() called");
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        logDebug("onStart() called");
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        logDebug("onResume() called");
        super.onResume();
        this.resumed = true;
        for (Entry entry : this.dataFragment.tag2dialogInfo.entrySet()) {
            String str = (String) entry.getKey();
            DialogInfo dialogInfo = (DialogInfo) entry.getValue();
            if (dialogInfo.dialog == null && dialogInfo.isShown) {
                showRetainedDialog(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        logDebug("onPause() called");
        super.onPause();
        this.resumed = false;
        for (BufferedListenerInvoker clearRealListener : getMyBufferedListenerInvokers().values()) {
            clearRealListener.clearRealListener();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        logDebug("onStop() called");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        logDebug("onDestroy() called");
        super.onDestroy();
        for (DialogInfo dialogInfo : this.dataFragment.tag2dialogInfo.values()) {
            if (dialogInfo.dialog != null) {
                dialogInfo.dialog.dismiss();
                dialogInfo.dialog = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("onActivityResult(");
        sb.append(i);
        sb.append(",");
        sb.append(i2);
        sb.append(",");
        sb.append(intent);
        sb.append(") in AxsActivity called");
        logDebug(sb.toString());
        for (ActivityResultHandler handleActivityResult : this.activityResultHandlers) {
            if (handleActivityResult.handleActivityResult(i, i2, intent)) {
                return;
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    public int registerActivityResultHandler(ActivityResultHandler activityResultHandler, int i) {
        int i2;
        if (i > 0) {
            i2 = this.firstFreeRequestCode;
            this.firstFreeRequestCode += i;
        } else {
            i2 = 0;
        }
        this.activityResultHandlers.add(activityResultHandler);
        return i2;
    }

    /* access modifiers changed from: protected */
    public final void enableLogging(boolean z) {
		// MOD: Add '!' to enable logging.
        this.logEnabled = !z;
    }

    /* access modifiers changed from: protected */
    public final void logDebug(String str) {
        if (this.logEnabled) {
            Log.d(this.TAG, str);
        }
    }

    /* access modifiers changed from: protected */
    public final void logDebug(String str, Object... objArr) {
        logDebug(String.format(str, objArr));
    }

    /* access modifiers changed from: protected */
    public final void logInfo(String str) {
        if (this.logEnabled) {
            Log.i(this.TAG, str);
        }
    }

    /* access modifiers changed from: protected */
    public final void logInfo(String str, Object... objArr) {
        logInfo(String.format(str, objArr));
    }

    /* access modifiers changed from: protected */
    public final void checkUiThread() {
        Assert.state(Thread.currentThread() == Looper.getMainLooper().getThread());
    }

    public final boolean isActivityResumed() {
        return this.resumed;
    }

    /* access modifiers changed from: protected */
    public final boolean wasRecreated() {
        Assert.state(this.wasRecreatedInited);
        return this.wasRecreated;
    }

    /* access modifiers changed from: protected */
    public final void alert(String str) {
        Builder builder = new Builder(this);
        builder.setMessage(str);
        builder.setNeutralButton("OK", null);
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Showing alert dialog: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        builder.create().show();
    }

    /* access modifiers changed from: protected */
    public final <T extends Serializable> void setResultEx(int i, T t) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_VALUE_NAME, t);
        setResult(i, intent);
    }

    public static <T extends Serializable> T getResultEx(Intent intent) {
        if (intent != null) {
            return (T) intent.getSerializableExtra(RESULT_VALUE_NAME);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public <ListenerType> ListenerType createBufferedListener(String str, ListenerType listenertype) {
        Map myBufferedListenerInvokers = getMyBufferedListenerInvokers();
        BufferedListenerInvoker bufferedListenerInvoker = (BufferedListenerInvoker) myBufferedListenerInvokers.get(str);
        if (bufferedListenerInvoker != null) {
            bufferedListenerInvoker.setRealListener(listenertype);
            return null;
        }
        BufferedListenerInvoker bufferedListenerInvoker2 = new BufferedListenerInvoker(inferListenerClass(listenertype));
        bufferedListenerInvoker2.setRealListener(listenertype);
        myBufferedListenerInvokers.put(str, bufferedListenerInvoker2);
        return (ListenerType) bufferedListenerInvoker2.getProxy();
    }

    private Map<String, BufferedListenerInvoker<?>> getMyBufferedListenerInvokers() {
        Map<String, BufferedListenerInvoker<?>> map = (Map) bufferedListenerInvokers.get(getClass());
        if (map != null) {
            return map;
        }
        HashMap hashMap = new HashMap();
        bufferedListenerInvokers.put(getClass(), hashMap);
        return hashMap;
    }

    private <ListenerType> Class<ListenerType> inferListenerClass(ListenerType listenertype) {
        Class<ListenerType>[] interfaces = (Class<ListenerType>[]) listenertype.getClass().getInterfaces();
        Assert.isTrue(interfaces.length == 1, String.format("The class %s is used as a listener and must implement exactly one interface.", new Object[]{listenertype.getClass()}));
        return interfaces[0];
    }

    /* access modifiers changed from: protected */
    public final void showRetainedDialog(String str) {
        DialogInfo dialogInfo = (DialogInfo) this.dataFragment.tag2dialogInfo.get(str);
        if (dialogInfo == null) {
            dialogInfo = new DialogInfo();
            this.dataFragment.tag2dialogInfo.put(str, dialogInfo);
        }
        if (dialogInfo.dialog == null) {
            dialogInfo.dialog = onCreateRetainedDialog(str);
        }
        if (!dialogInfo.isShown) {
            dialogInfo.dialog.show();
            dialogInfo.isShown = true;
        }
    }

    /* access modifiers changed from: protected */
    public final void hideRetainedDialog(String str) {
        DialogInfo dialogInfo = (DialogInfo) this.dataFragment.tag2dialogInfo.get(str);
        if (dialogInfo != null && dialogInfo.dialog != null) {
            dialogInfo.dialog.hide();
            dialogInfo.isShown = false;
        }
    }

    /* access modifiers changed from: protected */
    public final void dismissRetainedDialog(String str) {
        DialogInfo dialogInfo = (DialogInfo) this.dataFragment.tag2dialogInfo.get(str);
        if (dialogInfo != null && dialogInfo.dialog != null) {
            dialogInfo.dialog.dismiss();
            dialogInfo.dialog = null;
            dialogInfo.isShown = false;
        }
    }
}
