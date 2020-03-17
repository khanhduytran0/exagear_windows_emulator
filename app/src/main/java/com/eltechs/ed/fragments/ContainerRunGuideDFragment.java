package com.eltechs.ed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eltechs.ed.AppRunGuide;
import com.eltechs.ed.R;
import com.eltechs.ed.guestContainers.GuestContainer;
import com.eltechs.ed.guestContainers.GuestContainersManager;

public class ContainerRunGuideDFragment extends DialogFragment {
    public static final String ARG_CONT_ID = "CONT_ID";
    public static final String ARG_IS_SHOW_ONLY = "IS_SHOW_ONLY";
    /* access modifiers changed from: private */
    public GuestContainer mContainer;
    private AppRunGuide mGuide;
    /* access modifiers changed from: private */
    public boolean mIsShowOnly;
    /* access modifiers changed from: private */
    public OnContRunGuideResListener mListener;

    public interface OnContRunGuideResListener {
        void onContRunGuideRes(boolean z);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContainer = GuestContainersManager.getInstance(getContext()).getContainerById(Long.valueOf(getArguments().getLong("CONT_ID")));
        this.mGuide = (AppRunGuide) AppRunGuide.guidesMap.get(this.mContainer.mConfig.getRunGuide());
        this.mIsShowOnly = getArguments().getBoolean(ARG_IS_SHOW_ONLY);
        if (!this.mIsShowOnly) {
            try {
                this.mListener = (OnContRunGuideResListener) context;
            } catch (ClassCastException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append(context.toString());
                sb.append(" must implement OnContRunGuideResListener");
                throw new ClassCastException(sb.toString());
            }
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.container_run_guide, viewGroup, false);
        if (this.mIsShowOnly) {
            linearLayout.findViewById(R.id.btn_dontshow).setVisibility(8);
        }
        return linearLayout;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((TextView) getView().findViewById(R.id.header)).setText(getResources().getString(this.mGuide.mHeaderRes));
        ((TextView) getView().findViewById(R.id.body)).setText(Html.fromHtml(getResources().getString(this.mGuide.mBodyRes)));
        ((Button) getView().findViewById(R.id.btn_ok)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ContainerRunGuideDFragment.this.dismiss();
                if (!ContainerRunGuideDFragment.this.mIsShowOnly) {
                    ContainerRunGuideDFragment.this.mListener.onContRunGuideRes(true);
                }
            }
        });
        ((Button) getView().findViewById(R.id.btn_dontshow)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ContainerRunGuideDFragment.this.mContainer.mConfig.setRunGuideShown(true);
                ContainerRunGuideDFragment.this.dismiss();
                if (!ContainerRunGuideDFragment.this.mIsShowOnly) {
                    ContainerRunGuideDFragment.this.mListener.onContRunGuideRes(false);
                }
            }
        });
    }

    public static DialogFragment createDialog(GuestContainer guestContainer, boolean z) {
        ContainerRunGuideDFragment containerRunGuideDFragment = new ContainerRunGuideDFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("CONT_ID", guestContainer.mId.longValue());
        bundle.putBoolean(ARG_IS_SHOW_ONLY, z);
        containerRunGuideDFragment.setArguments(bundle);
        return containerRunGuideDFragment;
    }
}
