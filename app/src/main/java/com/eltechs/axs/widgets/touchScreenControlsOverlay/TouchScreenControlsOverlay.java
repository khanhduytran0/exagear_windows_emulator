package com.eltechs.axs.widgets.touchScreenControlsOverlay;

import android.view.View;
import com.eltechs.axs.TouchScreenControlsFactory;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.configuration.TouchScreenControlsInputConfiguration;
import com.eltechs.axs.widgets.actions.Action;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import java.util.Collections;
import java.util.List;

public class TouchScreenControlsOverlay implements XServerDisplayActivityInterfaceOverlay {
    private final TouchScreenControlsInputConfiguration configuration;
    private final TouchScreenControlsFactory controlsFactory;
    private List<? extends Action> popupMenuItems = Collections.emptyList();
    private TouchScreenControlsWidget widget;

    public TouchScreenControlsOverlay(TouchScreenControlsFactory touchScreenControlsFactory, TouchScreenControlsInputConfiguration touchScreenControlsInputConfiguration) {
        this.controlsFactory = touchScreenControlsFactory;
        this.configuration = touchScreenControlsInputConfiguration;
    }

    public void setPopupMenuItems(List<? extends Action> list) {
        this.popupMenuItems = list;
    }

    public View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        this.widget = new TouchScreenControlsWidget(xServerDisplayActivity, viewOfXServer, this.controlsFactory, this.configuration);
        this.widget.setZOrderMediaOverlay(true);
        xServerDisplayActivity.addDefaultPopupMenu(this.popupMenuItems);
        return this.widget;
    }

    public void detach() {
        this.widget.detach();
        this.widget = null;
    }
}
