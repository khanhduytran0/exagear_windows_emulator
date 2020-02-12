package com.eltechs.axs.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;

public class UsageActivity extends FrameworkActivity<ApplicationStateBase> {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setResult(2);
        Integer num = (Integer) getExtraParameter();
        setContentView(R.layout.tutorial_dialog);
        ((ImageView) findViewById(R.id.tutorial_pic)).setImageResource(num.intValue());
    }

    public void onOKClicked(View view) {
        finish();
    }
}
