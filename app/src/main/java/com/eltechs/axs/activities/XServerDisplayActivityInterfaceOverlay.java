package com.eltechs.axs.activities;

import android.view.View;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;

public interface XServerDisplayActivityInterfaceOverlay {
    View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer);

    void detach();
}
