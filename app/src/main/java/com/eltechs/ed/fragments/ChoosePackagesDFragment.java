package com.eltechs.ed.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import com.eltechs.ed.ContainerPackage;
import com.eltechs.ed.R;
import java.util.ArrayList;
import java.util.List;

public class ChoosePackagesDFragment extends DialogFragment {
    OnPackagesSelectedListener mListener;
    List<ContainerPackage> mSelectedItems;

    public interface OnPackagesSelectedListener {
        void onPackagesSelected(List<ContainerPackage> list);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnPackagesSelectedListener) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement OnPackagesSelectedListener");
            throw new ClassCastException(sb.toString());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        this.mSelectedItems = new ArrayList<ContainerPackage>();
        Builder builder = new Builder(getContext());
        builder.setTitle((CharSequence) "Select packages").setAdapter(new ArrayAdapter(getContext(), R.layout.multichoice_list_item, ContainerPackage.LIST), null).setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChoosePackagesDFragment.this.mListener.onPackagesSelected(ChoosePackagesDFragment.this.mSelectedItems);
                ChoosePackagesDFragment.this.dismiss();
            }
        }).setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChoosePackagesDFragment.this.dismiss();
            }
        });
        AlertDialog create = builder.create();
        create.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                ((AlertDialog) ChoosePackagesDFragment.this.getDialog()).getButton(-1).setEnabled(!ChoosePackagesDFragment.this.mSelectedItems.isEmpty());
            }
        });
        create.getListView().setChoiceMode(2);
        create.getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ContainerPackage containerPackage = (ContainerPackage) adapterView.getItemAtPosition(i);
                if (((CheckedTextView) view).isChecked()) {
                    ChoosePackagesDFragment.this.mSelectedItems.add(containerPackage);
                } else {
                    ChoosePackagesDFragment.this.mSelectedItems.remove(containerPackage);
                }
                ((AlertDialog) ChoosePackagesDFragment.this.getDialog()).getButton(-1).setEnabled(!ChoosePackagesDFragment.this.mSelectedItems.isEmpty());
            }
        });
        return create;
    }
}
