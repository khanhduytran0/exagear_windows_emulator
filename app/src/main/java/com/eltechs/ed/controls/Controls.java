package com.eltechs.ed.controls;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.ed.fragments.ControlsInfoDFragment;
import java.util.Arrays;
import java.util.List;

public abstract class Controls {
    public abstract XServerDisplayActivityInterfaceOverlay create();

    public abstract String getId();

    public abstract List<ControlsInfoElem> getInfoElems();

    public int getInfoImage() {
        return 0;
    }

    public abstract String getName();

    public DialogFragment createInfoDialog() {
        ControlsInfoDFragment controlsInfoDFragment = new ControlsInfoDFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ControlsInfoDFragment.ARG_CONTROLS_ID, getId());
        controlsInfoDFragment.setArguments(bundle);
        return controlsInfoDFragment;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId());
        sb.append(" (");
        sb.append(getName());
        sb.append(")");
        return sb.toString();
    }

    public static Controls find(String str) {
        for (Controls controls : getList()) {
            if (controls.getId().equals(str)) {
                return controls;
            }
        }
        return null;
    }

    public static Controls getDefault() {
        return new DefaultControls();
    }

    public static List<Controls> getList() {
        return Arrays.asList(new Controls[]{new DefaultControls(), new RtsControls(), new HoMM3Controls(), new Disciples2Controls(), new Panzer2Controls(), new Civ3Controls(), new ArcanumControls(), new FalloutControls(), new JA2Controls(), new MMControls()});
    }
}
