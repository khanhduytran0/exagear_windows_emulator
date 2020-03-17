package com.eltechs.axs.activities.menus;

import com.eltechs.ed.R;
import com.eltechs.axs.activities.StartupActivity;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.widgets.actions.AbstractAction;

public class Quit extends AbstractAction {
    public Quit() {
        super(AndroidHelpers.getString(R.string.stop_Xserver));
    }

    public void run() {
        StartupActivity.shutdownAXSApplication(true);
    }
}
