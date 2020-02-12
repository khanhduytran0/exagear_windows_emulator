package com.eltechs.axs.widgets.touchScreenControlsOverlay;

import android.widget.FrameLayout;
import com.eltechs.axs.TouchScreenControls;
import com.eltechs.axs.TouchScreenControlsFactory;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.configuration.TouchScreenControlsInputConfiguration;
import com.eltechs.axs.helpers.UiThread;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;

public class TouchScreenControlsWidget extends FrameLayout {
    private final TouchScreenControlsFactory controlsFactory;
    private final TouchScreenControlsDisplayWidget displayWidget;
    /* access modifiers changed from: private */
    public final XServerDisplayActivity host;
    private final TouchScreenControlsInputWidget inputWidget;
    private final ViewOfXServer target;

    public TouchScreenControlsWidget(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer, TouchScreenControlsFactory touchScreenControlsFactory, TouchScreenControlsInputConfiguration touchScreenControlsInputConfiguration) {
        super(xServerDisplayActivity);
        this.host = xServerDisplayActivity;
        this.target = viewOfXServer;
        this.controlsFactory = touchScreenControlsFactory;
        if (!touchScreenControlsFactory.hasVisibleControls()) {
            this.displayWidget = null;
        } else {
            this.displayWidget = new TouchScreenControlsDisplayWidget(xServerDisplayActivity);
            addView(this.displayWidget);
        }
        this.inputWidget = new TouchScreenControlsInputWidget(xServerDisplayActivity, viewOfXServer, touchScreenControlsInputConfiguration);
        addView(this.inputWidget);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (i != i3 && i2 != i4) {
            final int i5 = i;
            final int i6 = i2;
            final int i7 = i3;
            final int i8 = i4;
            Runnable r0 = new Runnable() {
                public void run() {
                    TouchScreenControlsWidget.this.host.placeViewOfXServer(i5, i6, i7 - i5, i8 - i6);
                }
            };
            UiThread.post(r0);
            TouchScreenControls create = this.controlsFactory.create(this, this.target);
            this.inputWidget.setTouchScreenControls(create);
            if (this.displayWidget != null) {
                this.displayWidget.setTouchScreenControls(create);
            }
        }
    }

    public void setZOrderMediaOverlay(boolean z) {
        if (this.displayWidget != null) {
            this.displayWidget.setZOrderMediaOverlay(z);
        }
    }

    public void detach() {
        if (this.displayWidget != null) {
            this.displayWidget.onPause();
        }
    }
}
