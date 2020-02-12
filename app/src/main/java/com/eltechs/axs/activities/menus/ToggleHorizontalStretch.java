package com.eltechs.axs.activities.menus;

import com.eltechs.axs.CommonApplicationConfigurationAccessor;
import com.eltechs.ed.R;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.widgets.actions.AbstractAction;

public class ToggleHorizontalStretch extends AbstractAction {
    public ToggleHorizontalStretch() {
        super(null);
    }

    public String getName() {
        if (getCurrentXServerDisplayActivity().isHorizontalStretchEnabled()) {
            return AndroidHelpers.getString(R.string.show_normal);
        }
        return AndroidHelpers.getString(R.string.show_stretched);
    }

    public void run() {
        XServerDisplayActivity currentXServerDisplayActivity = getCurrentXServerDisplayActivity();
        CommonApplicationConfigurationAccessor commonApplicationConfigurationAccessor = new CommonApplicationConfigurationAccessor();
        boolean z = !currentXServerDisplayActivity.isHorizontalStretchEnabled();
        currentXServerDisplayActivity.setHorizontalStretchEnabled(z);
        commonApplicationConfigurationAccessor.setHorizontalStretchEnabled(z);
    }
}
