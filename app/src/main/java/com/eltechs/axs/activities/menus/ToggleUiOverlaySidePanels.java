package com.eltechs.axs.activities.menus;

import com.eltechs.axs.Globals;
import com.eltechs.ed.R;
import com.eltechs.axs.activities.XServerDisplayActivityUiOverlaySidePanels;
import com.eltechs.axs.applicationState.XServerDisplayActivityConfigurationAware;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.widgets.actions.AbstractAction;

public class ToggleUiOverlaySidePanels extends AbstractAction {
    public ToggleUiOverlaySidePanels() {
        super(null);
    }

    private XServerDisplayActivityUiOverlaySidePanels getUiOverlaySidePanels() {
        XServerDisplayActivityUiOverlaySidePanels xServerDisplayActivityUiOverlaySidePanels = (XServerDisplayActivityUiOverlaySidePanels) ((XServerDisplayActivityConfigurationAware) Globals.getApplicationState()).getXServerDisplayActivityInterfaceOverlay();
        Assert.notNull(xServerDisplayActivityUiOverlaySidePanels, "ToggleUiOverlaySidePanels should be used with UiOverlays with SidePanels");
        return xServerDisplayActivityUiOverlaySidePanels;
    }

    public String getName() {
        if (getUiOverlaySidePanels().isSidePanelsVisible()) {
            return AndroidHelpers.getString(R.string.hide_side_panels);
        }
        return AndroidHelpers.getString(R.string.show_side_panels);
    }

    public void run() {
        getUiOverlaySidePanels().toggleSidePanelsVisibility();
    }
}
