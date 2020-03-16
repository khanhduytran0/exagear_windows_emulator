package com.eltechs.axs;

import android.view.View;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;

public interface TouchScreenControlsFactory {
    TouchScreenControls create(View view, ViewOfXServer viewOfXServer);

    boolean hasVisibleControls();
}
