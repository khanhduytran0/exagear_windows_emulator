package com.eltechs.ed.fragments.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.eltechs.ed.R;
import com.eltechs.ed.activities.EDHelpActivity;

public class HelpRootFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.help_root, viewGroup, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getView().findViewById(R.id.overview).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((EDHelpActivity) HelpRootFragment.this.getActivity()).setHelpFragment(new HelpOverviewFragment());
            }
        });
        getView().findViewById(R.id.controls).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((EDHelpActivity) HelpRootFragment.this.getActivity()).setHelpFragment(new HelpControlsFragment());
            }
        });
        getView().findViewById(R.id.about).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((EDHelpActivity) HelpRootFragment.this.getActivity()).setHelpFragment(new HelpAboutFragment());
            }
        });
    }
}
