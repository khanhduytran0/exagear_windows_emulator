package com.eltechs.axs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;
// import com.eltechs.axs.applicationState.PurchasableComponentsCollectionAware;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
// import com.eltechs.axs.payments.PurchasableComponent;

public class SelectCustomizationPackageActivity<StateClass extends ApplicationStateBase<StateClass> /* & PurchasableComponentsCollectionAware */ & SelectedExecutableFileAware<StateClass>> extends FrameworkActivity<StateClass> {
    private static final int REQUEST_CODE_CP_DRAWABLE = 10001;

    private class MyAdapter extends BaseAdapter {
        private final View[] myViews;

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public int getItemViewType(int i) {
            return -1;
        }

        private MyAdapter() {
            this.myViews = new View[getCount()];
        }

        public int getCount() {
            return 0; // ((PurchasableComponentsCollectionAware) SelectCustomizationPackageActivity.this.getApplicationState()).getPurchasableComponentsCollection().getPurchasableComponentsCount();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (this.myViews[i] != null) {
                return this.myViews[i];
            }
            // final PurchasableComponent purchasableComponent = ((PurchasableComponentsCollectionAware) SelectCustomizationPackageActivity.this.getApplicationState()).getPurchasableComponentsCollection().getPurchasableComponent(i);
            LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) SelectCustomizationPackageActivity.this.getSystemService("layout_inflater")).inflate(R.layout.select_customization_package_elem, viewGroup, false);
            Button button = (Button) linearLayout.findViewById(R.id.select_cp_button_elem);
            button.setText(String.format(SelectCustomizationPackageActivity.this.getString(R.string.like_textelem), "Cracked"));
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    // ((SelectedExecutableFileAware) SelectCustomizationPackageActivity.this.getApplicationState()).getSelectedExecutableFile().setUserSelectedCustomizationPackage(purchasableComponent);
                    SelectCustomizationPackageActivity.this.setResult(-1);
                    SelectCustomizationPackageActivity.this.finish();
                }
            });
            ((Button) linearLayout.findViewById(R.id.select_cp_button_info)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    // SelectCustomizationPackageActivity.this.startActivityForResult(10001, UsageActivity.class, Integer.valueOf(purchasableComponent.getInfoResId()));
                }
            });
            this.myViews[i] = linearLayout;
            return linearLayout;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.select_customization_package);
        resizeRootViewToStandardDialogueSize();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        ((ListView) findViewById(R.id.list_of_all_customization_packages)).setAdapter(new MyAdapter());
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
        sb.append(")");
        logDebug(sb.toString());
        if (i != 10001) {
            super.onActivityResult(i, i2, intent);
        }
    }
}
