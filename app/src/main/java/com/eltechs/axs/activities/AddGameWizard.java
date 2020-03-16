package com.eltechs.axs.activities;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.applicationState.UserApplicationsDirectoryNameAware;

public class AddGameWizard<StateClass extends ApplicationStateBase<StateClass> & UserApplicationsDirectoryNameAware> extends FrameworkActivity<StateClass> {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.add_game_wizard);
        resizeRootViewToStandardDialogueSize();
        Integer num = (Integer) getExtraParameter();
        if (num == null) {
            num = Integer.valueOf(R.string.agw_basic_instruction);
        }
        TextView textView = (TextView) findViewById(R.id.agw_text_view);
        textView.setText(Html.fromHtml(getResources().getString(num.intValue())));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
