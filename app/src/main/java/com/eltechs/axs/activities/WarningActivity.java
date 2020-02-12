package com.eltechs.axs.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;

public class WarningActivity extends FrameworkActivity<ApplicationStateBase<?>> {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.warning);
        ((TextView) findViewById(R.id.warn_text_view)).setText(Html.fromHtml((String) getExtraParameter()));
    }

    public void onOKClicked(View view) {
        finish();
    }
}
