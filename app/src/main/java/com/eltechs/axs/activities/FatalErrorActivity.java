package com.eltechs.axs.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.eltechs.axs.Globals;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;

public class FatalErrorActivity extends FrameworkActivity<ApplicationStateBase<?>> {
    public static void showFatalError(String str) {
        Context context;
        ApplicationStateBase applicationStateBase = (ApplicationStateBase) Globals.getApplicationState();
        // int i = 0;
        boolean z = applicationStateBase.getCurrentActivity() != null;
        if (z) {
            context = applicationStateBase.getCurrentActivity();
        } else {
            context = applicationStateBase.getAndroidApplicationContext();
        }
        Intent intent = new Intent(context, FatalErrorActivity.class);
        if (!z) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        
        writeExtraParameter(intent, str);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fatal_error);
        TextView textView = (TextView) findViewById(R.id.fe_text_view);
        textView.setText(Html.fromHtml((String) getExtraParameter()));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void finish() {
        super.finish();
        StartupActivity.shutdownAXSApplication();
    }
}
