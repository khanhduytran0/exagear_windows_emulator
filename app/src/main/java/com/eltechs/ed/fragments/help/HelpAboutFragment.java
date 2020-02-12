package com.eltechs.ed.fragments.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eltechs.ed.BuildConfig;
import com.eltechs.ed.R;

public class HelpAboutFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.help_about, viewGroup, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((TextView) getView().findViewById(R.id.version)).setText(BuildConfig.VERSION_NAME);
    }
}
