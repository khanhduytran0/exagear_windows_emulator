package com.eltechs.axs.gamesControls;

import android.view.View;
import com.eltechs.axs.CommonApplicationConfigurationAccessor;
import com.eltechs.axs.TouchScreenControlsFactory;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.activities.menus.Quit;
import com.eltechs.axs.activities.menus.ShowKeyboard;
import com.eltechs.axs.activities.menus.ShowUsage;
import com.eltechs.axs.activities.menus.ToggleHorizontalStretch;
import com.eltechs.axs.configuration.TouchScreenControlsInputConfiguration;
import com.eltechs.axs.widgets.touchScreenControlsOverlay.TouchScreenControlsWidget;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import java.util.ArrayList;

public class BasicRolePlayingGamesUI implements XServerDisplayActivityInterfaceOverlay {
    private final TouchScreenControlsFactory controlsFactory;
    private TouchScreenControlsWidget widget;

    public BasicRolePlayingGamesUI(TouchScreenControlsFactory touchScreenControlsFactory) {
        this.controlsFactory = touchScreenControlsFactory;
    }

    public View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        this.widget = new TouchScreenControlsWidget(xServerDisplayActivity, viewOfXServer, this.controlsFactory, TouchScreenControlsInputConfiguration.DEFAULT);
        this.widget.setZOrderMediaOverlay(true);
        viewOfXServer.setHorizontalStretchEnabled(new CommonApplicationConfigurationAccessor().isHorizontalStretchEnabled());
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ShowKeyboard());
        arrayList.add(new ToggleHorizontalStretch());
        arrayList.add(new ShowUsage());
        arrayList.add(new Quit());
        xServerDisplayActivity.addDefaultPopupMenu(arrayList);
        return this.widget;
    }

    public void detach() {
        this.widget.detach();
        this.widget = null;
    }
}
