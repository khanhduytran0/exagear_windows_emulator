package com.eltechs.ed.controls.uiOverlays;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog.Builder;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.activities.StartupActivity;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.configuration.TouchScreenControlsInputConfiguration;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.widgets.touchScreenControlsOverlay.TouchScreenControlsWidget;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.ViewFacade;
import com.eltechs.ed.R;
import com.eltechs.ed.controls.Controls;
import com.eltechs.ed.controls.touchControls.AbstractTCF;

public class DefaultUIOverlay implements XServerDisplayActivityInterfaceOverlay {
    private static final float displaySizeThresholdHeightInches = 3.0f;
    private static final float displaySizeThresholdWidthInches = 5.0f;
    private static final float sideToolbarWidthNormalDisplayInches = 0.4f;
    private static final float sideToolbarWidthSmallDisplayInches = 0.35f;
    private static final float toolbarHeightNormalDisplayInches = 0.27f;
    private static final float toolbarHeightSmallDisplayInches = 0.23f;
    /* access modifiers changed from: private */
    public final Controls mControls;
    private final AbstractTCF mControlsFactory;
    protected XServerDisplayActivity mHostActivity;
    protected int mLeftTbWidth;
    protected LinearLayout mLeftToolbar;
    protected int mRightTbWidth;
    protected LinearLayout mRightToolbar;
    protected View mToolbar;
    private TouchScreenControlsWidget mTscWidget;
    protected ViewOfXServer mViewOfXServer;
    protected ViewFacade mXServerFacade;

    enum KeyButtonHandlerType {
        CLICK,
        PRESS_RELEASE,
        CUSTOM
    }

    protected class ScrollArea {
        LinearLayout mLinearLayout;
        ScrollView mScrollView;

        ScrollArea(ScrollView scrollView, LinearLayout linearLayout) {
            this.mScrollView = scrollView;
            this.mLinearLayout = linearLayout;
        }
    }

    public DefaultUIOverlay(Controls controls, AbstractTCF abstractTCF) {
        this.mControls = controls;
        this.mControlsFactory = abstractTCF;
        this.mControlsFactory.setUIOverlay(this);
    }

    public View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        this.mHostActivity = xServerDisplayActivity;
        this.mViewOfXServer = viewOfXServer;
        this.mXServerFacade = viewOfXServer.getXServerFacade();
        this.mTscWidget = new TouchScreenControlsWidget(xServerDisplayActivity, viewOfXServer, this.mControlsFactory, TouchScreenControlsInputConfiguration.DEFAULT);
        this.mTscWidget.setZOrderMediaOverlay(true);
        FrameLayout frameLayout = new FrameLayout(xServerDisplayActivity);
        frameLayout.setLayoutParams(new LayoutParams(-1, -1));
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setOrientation(0);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout.addView(createLeftToolbar());
        linearLayout.addView(this.mTscWidget, new LinearLayout.LayoutParams(0, -1, 1.0f));
        linearLayout.addView(createRightToolbar());
        frameLayout.addView(linearLayout);
        frameLayout.addView(createToolbar());
        viewOfXServer.setHorizontalStretchEnabled(false);
        return frameLayout;
    }

    private View createToolbar() {
        ConstraintLayout constraintLayout = (ConstraintLayout) this.mHostActivity.getLayoutInflater().inflate(R.layout.default_ui_overlay_toolbar, null);
        final ImageButton imageButton = (ImageButton) constraintLayout.findViewById(R.id.button_fullscreen);
        imageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                boolean z = !DefaultUIOverlay.this.mViewOfXServer.isHorizontalStretchEnabled();
                DefaultUIOverlay.this.mViewOfXServer.setHorizontalStretchEnabled(z);
                imageButton.setImageResource(z ? R.drawable.ic_fullscreen_exit_24dp : R.drawable.ic_fullscreen_24dp);
            }
        });
        ((ImageButton) constraintLayout.findViewById(R.id.button_sidetoolbars)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DefaultUIOverlay.this.toggleRightToolbar();
                DefaultUIOverlay.this.toggleLeftToolbar();
            }
        });
        ((ImageButton) constraintLayout.findViewById(R.id.button_kbd)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AndroidHelpers.toggleSoftInput();
            }
        });
        ((ImageButton) constraintLayout.findViewById(R.id.button_help)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DefaultUIOverlay.this.mControls.createInfoDialog().show(DefaultUIOverlay.this.mHostActivity.getSupportFragmentManager(), "CONTROLS_INFO");
            }
        });
        ((ImageButton) constraintLayout.findViewById(R.id.button_close)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(DefaultUIOverlay.this.mHostActivity);
                builder.setTitle((CharSequence) "Confirm Exit");
                builder.setIcon((int) R.drawable.ic_warning_24dp);
                builder.setMessage((CharSequence) "Are you sure you want to exit?");
                builder.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StartupActivity.shutdownAXSApplication(true);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        constraintLayout.setMaxHeight((int) ((isDisplaySmallHeight(displayMetrics) ? toolbarHeightSmallDisplayInches : toolbarHeightNormalDisplayInches) * displayMetrics.ydpi));
        this.mToolbar = constraintLayout;
        return constraintLayout;
    }

    private LinearLayout createSideToolbar() {
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        int i = (int) ((isDisplaySmallWidth(displayMetrics) ? sideToolbarWidthSmallDisplayInches : 0.4f) * displayMetrics.xdpi);
        LinearLayout linearLayout = new LinearLayout(this.mHostActivity);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(i, -1, 0.0f));
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-10066330);
        linearLayout.setVisibility(8);
        return linearLayout;
    }

    private View createLeftToolbar() {
        this.mLeftToolbar = createSideToolbar();
        this.mLeftTbWidth = this.mLeftToolbar.getLayoutParams().width;
        return this.mLeftToolbar;
    }

    private View createRightToolbar() {
        this.mRightToolbar = createSideToolbar();
        this.mRightTbWidth = this.mRightToolbar.getLayoutParams().width;
        return this.mRightToolbar;
    }

    /* access modifiers changed from: protected */
    public ScrollArea createToolbarScrollArea() {
        ScrollView scrollView = new ScrollView(this.mHostActivity);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0f));
        LinearLayout linearLayout = new LinearLayout(this.mHostActivity);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        scrollView.addView(linearLayout);
        return new ScrollArea(scrollView, linearLayout);
    }

    /* access modifiers changed from: protected */
    public void setButtonStyleByState(Button button, boolean z) {
        if (z) {
            button.setBackgroundResource(R.drawable.side_tb_button_pressed);
            button.setTextColor(-2236963);
            return;
        }
        button.setBackgroundResource(R.drawable.side_tb_button_normal);
        button.setTextColor(-14540254);
    }

    /* access modifiers changed from: protected */
    public Button createSideTbButton(int i, String str, KeyButtonHandlerType keyButtonHandlerType) {
        return createSideTbButton(i, str, keyButtonHandlerType, KeyCodesX.KEY_NONE);
    }

    /* access modifiers changed from: protected */
    public Button createSideTbButton(int i, String str, KeyButtonHandlerType keyButtonHandlerType, final KeyCodesX keyCodesX) {
        Button button = new Button(this.mHostActivity);
        button.setLayoutParams(new LinearLayout.LayoutParams(i, i));
        button.setText(str);
        setButtonStyleByState(button, false);
        switch (keyButtonHandlerType) {
            case CLICK:
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        DefaultUIOverlay.this.mXServerFacade.injectKeyType((byte) keyCodesX.getValue());
                    }
                });
                break;
            case PRESS_RELEASE:
                button.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        int action = motionEvent.getAction();
                        if (action != 3) {
                            switch (action) {
                                case 0:
                                    DefaultUIOverlay.this.setButtonStyleByState((Button) view, true);
                                    DefaultUIOverlay.this.mXServerFacade.injectKeyPress((byte) keyCodesX.getValue());
                                    break;
                                case 1:
                                    break;
                            }
                        }
                        DefaultUIOverlay.this.setButtonStyleByState((Button) view, false);
                        DefaultUIOverlay.this.mXServerFacade.injectKeyRelease((byte) keyCodesX.getValue());
                        return false;
                    }
                });
                break;
        }
        return button;
    }

    /* access modifiers changed from: protected */
    public void setToolbarSideToolbarsButtonVisibility(boolean z) {
        ((ImageButton) this.mToolbar.findViewById(R.id.button_sidetoolbars)).setVisibility(z ? 0 : 8);
    }

    public void toggleToolbar() {
        this.mToolbar.setVisibility(this.mToolbar.getVisibility() != 0 ? 0 : 8);
    }

    public void toggleLeftToolbar() {
        this.mLeftToolbar.setVisibility(this.mLeftToolbar.getVisibility() != 0 ? 0 : 8);
    }

    public void toggleRightToolbar() {
        this.mRightToolbar.setVisibility(this.mRightToolbar.getVisibility() != 0 ? 0 : 8);
    }

    private static boolean isDisplaySmallHeight(DisplayMetrics displayMetrics) {
        return ((float) displayMetrics.heightPixels) / displayMetrics.ydpi < displaySizeThresholdHeightInches;
    }

    private static boolean isDisplaySmallWidth(DisplayMetrics displayMetrics) {
        return ((float) displayMetrics.widthPixels) / displayMetrics.xdpi < displaySizeThresholdWidthInches;
    }

    public void detach() {
        this.mTscWidget.detach();
        this.mTscWidget = null;
        this.mToolbar = null;
        this.mLeftToolbar = null;
        this.mRightToolbar = null;
    }
}
