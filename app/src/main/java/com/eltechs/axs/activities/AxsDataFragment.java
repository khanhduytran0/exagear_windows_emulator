package com.eltechs.axs.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import java.util.HashMap;

public class AxsDataFragment extends Fragment {
    HashMap<String, DialogInfo> tag2dialogInfo = new HashMap<>();

    static class DialogInfo {
        Dialog dialog;
        boolean isShown;

        DialogInfo() {
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }
}
