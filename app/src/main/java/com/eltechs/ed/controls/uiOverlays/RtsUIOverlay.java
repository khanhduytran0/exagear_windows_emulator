package com.eltechs.ed.controls.uiOverlays;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.helpers.UiThread;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.ed.controls.Controls;
import com.eltechs.ed.controls.touchControls.AbstractTCF;

public class RtsUIOverlay extends DefaultUIOverlay {
    /* access modifiers changed from: private */
    public Button mCtrlButton;
    /* access modifiers changed from: private */
    public boolean mIsCtrlPressed;

    public RtsUIOverlay(Controls controls, AbstractTCF abstractTCF) {
        super(controls, abstractTCF);
    }

    public View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        View attach = super.attach(xServerDisplayActivity, viewOfXServer);
        this.mLeftToolbar.addView(createSideTbButton(this.mLeftTbWidth, "A", KeyButtonHandlerType.PRESS_RELEASE, KeyCodesX.KEY_A));
        this.mLeftToolbar.addView(createSideTbButton(this.mLeftTbWidth, "S", KeyButtonHandlerType.PRESS_RELEASE, KeyCodesX.KEY_S));
        this.mLeftToolbar.addView(createSideTbButton(this.mLeftTbWidth, "H", KeyButtonHandlerType.PRESS_RELEASE, KeyCodesX.KEY_H));
        this.mLeftToolbar.addView(createSideTbButton(this.mLeftTbWidth, "P", KeyButtonHandlerType.PRESS_RELEASE, KeyCodesX.KEY_P));
        this.mLeftToolbar.addView(createSideTbButton(this.mLeftTbWidth, "SHIFT", KeyButtonHandlerType.PRESS_RELEASE, KeyCodesX.KEY_SHIFT_LEFT));
        this.mCtrlButton = createSideTbButton(this.mRightTbWidth, "CTRL", KeyButtonHandlerType.CUSTOM);
        this.mCtrlButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RtsUIOverlay.this.mIsCtrlPressed = !RtsUIOverlay.this.mIsCtrlPressed;
                RtsUIOverlay.this.setButtonStyleByState((Button) view, RtsUIOverlay.this.mIsCtrlPressed);
            }
        });
        this.mRightToolbar.addView(this.mCtrlButton);
        ScrollArea createToolbarScrollArea = createToolbarScrollArea();
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_1, "1"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_2, "2"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_3, "3"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_4, "4"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_5, "5"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_6, "6"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_7, "7"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_8, "8"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_9, "9"));
        createToolbarScrollArea.mLinearLayout.addView(createNumButton(KeyCodesX.KEY_0, "0"));
        this.mRightToolbar.addView(createToolbarScrollArea.mScrollView);
        this.mLeftToolbar.setVisibility(0);
        this.mRightToolbar.setVisibility(0);
        setToolbarSideToolbarsButtonVisibility(true);
        return attach;
    }

    private Button createNumButton(final KeyCodesX keyCodesX, String str) {
        Button createSideTbButton = createSideTbButton(this.mRightTbWidth, str, KeyButtonHandlerType.CUSTOM);
        createSideTbButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (RtsUIOverlay.this.mIsCtrlPressed) {
                    RtsUIOverlay.this.mXServerFacade.injectKeyPress((byte) KeyCodesX.KEY_CONTROL_LEFT.getValue());
                    RtsUIOverlay.this.mXServerFacade.injectKeyType((byte) keyCodesX.getValue());
                    RtsUIOverlay.this.mXServerFacade.injectKeyRelease((byte) KeyCodesX.KEY_CONTROL_LEFT.getValue());
                    RtsUIOverlay.this.mIsCtrlPressed = false;
                    RtsUIOverlay.this.setButtonStyleByState(RtsUIOverlay.this.mCtrlButton, RtsUIOverlay.this.mIsCtrlPressed);
                } else {
                    RtsUIOverlay.this.mXServerFacade.injectKeyType((byte) keyCodesX.getValue());
                }
                final Button button = (Button) view;
                RtsUIOverlay.this.setButtonStyleByState(button, true);
                UiThread.postDelayed(25, new Runnable() {
                    public void run() {
                        RtsUIOverlay.this.setButtonStyleByState(button, false);
                    }
                });
            }
        });
        return createSideTbButton;
    }
}
