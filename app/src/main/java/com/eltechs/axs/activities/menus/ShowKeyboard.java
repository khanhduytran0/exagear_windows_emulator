package com.eltechs.axs.activities.menus;

import com.eltechs.ed.R;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.widgets.actions.AbstractAction;

public class ShowKeyboard extends AbstractAction {
    public ShowKeyboard() {
        super(AndroidHelpers.getString(R.string.show_keyboard));
    }

    public void run() {
        AndroidHelpers.toggleSoftInput();
    }
}
