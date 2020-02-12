package com.eltechs.axs.gamesControls;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import com.eltechs.axs.CommonApplicationConfigurationAccessor;
import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.PointerEventReporter;
import com.eltechs.ed.R;
import com.eltechs.axs.TouchScreenControlsFactory;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.activities.XServerDisplayActivityUiOverlaySidePanels;
import com.eltechs.axs.activities.menus.Quit;
import com.eltechs.axs.activities.menus.ShowKeyboard;
import com.eltechs.axs.activities.menus.ShowUsage;
import com.eltechs.axs.activities.menus.ToggleHorizontalStretch;
import com.eltechs.axs.activities.menus.ToggleUiOverlaySidePanels;
import com.eltechs.axs.configuration.TouchScreenControlsInputConfiguration;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.widgets.actions.AbstractAction;
import com.eltechs.axs.widgets.touchScreenControlsOverlay.TouchScreenControlsWidget;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.ViewFacade;
import java.util.Arrays;

public class FalloutInterfaceOverlay implements XServerDisplayActivityInterfaceOverlay, XServerDisplayActivityUiOverlaySidePanels {
    public static final float buttonSizeInches = 0.4f;
    private static final float buttonSzNormalDisplayInches = 0.4f;
    private static final float buttonSzSmallDisplayInches = 0.3f;
    private static final float displaySizeThresholdInches = 5.0f;
    private final int buttonWidthPixelsFixup = 30;
    private final TouchScreenControlsFactory controlsFactory = new FalloutTouchScreenControlsFactory();
    private boolean isToolbarsVisible = true;
    private View leftToolbar;
    private View rightToolbar;
    private TouchScreenControlsWidget tscWidget;

    private static boolean isDisplaySmall(DisplayMetrics displayMetrics) {
        return ((float) displayMetrics.widthPixels) / ((float) displayMetrics.densityDpi) < displaySizeThresholdInches;
    }

    public View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        this.tscWidget = new TouchScreenControlsWidget(xServerDisplayActivity, viewOfXServer, this.controlsFactory, TouchScreenControlsInputConfiguration.DEFAULT);
        this.tscWidget.setZOrderMediaOverlay(true);
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setOrientation(0);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout.addView(createLeftToolbar(xServerDisplayActivity, viewOfXServer));
        linearLayout.addView(this.tscWidget, new LayoutParams(0, -1, 1.0f));
        linearLayout.addView(createRightToolbar(xServerDisplayActivity, viewOfXServer));
        viewOfXServer.setHorizontalStretchEnabled(new CommonApplicationConfigurationAccessor().isHorizontalStretchEnabled());
        xServerDisplayActivity.addDefaultPopupMenu(Arrays.asList(new AbstractAction[]{new ShowKeyboard(), new ToggleHorizontalStretch(), new ToggleUiOverlaySidePanels(), new ShowUsage(), new Quit()}));
        return linearLayout;
    }

    public void detach() {
        this.tscWidget.detach();
        this.tscWidget = null;
        this.leftToolbar = null;
        this.rightToolbar = null;
    }

    public boolean isSidePanelsVisible() {
        return this.isToolbarsVisible;
    }

    public void toggleSidePanelsVisibility() {
        this.isToolbarsVisible = !this.isToolbarsVisible;
        if (this.isToolbarsVisible) {
            this.leftToolbar.setVisibility(0);
            this.rightToolbar.setVisibility(0);
            return;
        }
        this.leftToolbar.setVisibility(8);
        this.rightToolbar.setVisibility(8);
    }

    private static Button createSyncButton(Activity activity, final ViewOfXServer viewOfXServer, int i) {
        String string = activity.getResources().getString(R.string.fal_sync);
        Button button = new Button(activity);
        button.setWidth(i);
        button.setMaxWidth(i);
        button.setMinWidth(i);
        button.setHeight(i);
        button.setMaxHeight(i);
        button.setMinHeight(i);
        button.setText(string);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewFacade xServerFacade = viewOfXServer.getXServerFacade();
                PointerEventReporter pointerEventReporter = new PointerEventReporter(viewOfXServer);
                pointerEventReporter.pointerMove(0.0f, 0.0f);
                try {
                    Thread.sleep(40, 0);
                } catch (InterruptedException unused) {
                }
                pointerEventReporter.pointerMove((float) xServerFacade.getScreenInfo().widthInPixels, 0.0f);
                try {
                    Thread.sleep(40, 0);
                } catch (InterruptedException unused2) {
                }
                pointerEventReporter.pointerMove((float) (xServerFacade.getScreenInfo().widthInPixels + 1000), (float) (xServerFacade.getScreenInfo().heightInPixels + 1000));
                try {
                    Thread.sleep(40, 0);
                } catch (InterruptedException unused3) {
                }
                pointerEventReporter.pointerMove(0.0f, (float) xServerFacade.getScreenInfo().heightInPixels);
                try {
                    Thread.sleep(40, 0);
                } catch (InterruptedException unused4) {
                }
                pointerEventReporter.pointerMove(50.0f, 50.0f);
            }
        });
        return button;
    }

    private View createLeftToolbar(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setLayoutParams(new LayoutParams(-2, -1, 0.0f));
        linearLayout.setOrientation(1);
        linearLayout.addView(createLeftScrollViewWithButtons(xServerDisplayActivity, viewOfXServer.getXServerFacade(), (int) (0.4f * ((float) AndroidHelpers.getDisplayMetrics().densityDpi))));
        if (!this.isToolbarsVisible) {
            linearLayout.setVisibility(8);
        }
        this.leftToolbar = linearLayout;
        return linearLayout;
    }

    private static ScrollView createLeftScrollViewWithButtons(Activity activity, ViewFacade viewFacade, int i) {
        ScrollView scrollView = new ScrollView(activity);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        linearLayout.setOrientation(1);
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_M, i, "M"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_C, i, "C"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_I, i, "I"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_P, i, "P"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_Z, i, "Z"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_O, i, "O"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_B, i, "B"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_N, i, "N"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_S, i, "S"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F1, i, "F1"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F2, i, "F2"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F3, i, "F3"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F4, i, "F4"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F5, i, "F5"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F6, i, "F6"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F7, i, "F7"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F10, i, "F10"));
        scrollView.addView(linearLayout);
        return scrollView;
    }

    private View createRightToolbar(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setLayoutParams(new LayoutParams(-2, -1, 0.0f));
        linearLayout.setOrientation(1);
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        int i = (int) ((isDisplaySmall(displayMetrics) ? buttonSzSmallDisplayInches : 0.4f) * ((float) displayMetrics.densityDpi));
        linearLayout.addView(createSyncButton(xServerDisplayActivity, viewOfXServer, (isDisplaySmall(displayMetrics) ? 30 : 0) + i));
        linearLayout.addView(createRightScrollViewWithButtons(xServerDisplayActivity, viewOfXServer.getXServerFacade(), i));
        if (!this.isToolbarsVisible) {
            linearLayout.setVisibility(8);
        }
        this.rightToolbar = linearLayout;
        return linearLayout;
    }

    private static ScrollView createRightScrollViewWithButtons(Activity activity, ViewFacade viewFacade, int i) {
        ScrollView scrollView = new ScrollView(activity);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        linearLayout.setOrientation(1);
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_RETURN, i, "Entr"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_SPACE, i, "Spc"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_1, i, "1"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_2, i, "2"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_3, i, "3"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_4, i, "4"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_5, i, "5"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_6, i, "6"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_7, i, "7"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_8, i, "8"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_TAB, i, "Tab"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_HOME, i, "Hom"));
        scrollView.addView(linearLayout);
        return scrollView;
    }

    private static void setHandlerToButton(Button button, final KeyCodesX keyCodesX, final ViewFacade viewFacade) {
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                viewFacade.injectKeyType((byte) keyCodesX.getValue());
            }
        });
    }

    private static Button createLetterButton(Activity activity, ViewFacade viewFacade, KeyCodesX keyCodesX, int i, String str) {
        Button button = new Button(activity);
        button.setWidth(i);
        button.setMinWidth(i);
        button.setMaxWidth(i);
        button.setHeight(i);
        button.setMinHeight(i);
        button.setMaxHeight(i);
        button.setText(str);
        setHandlerToButton(button, keyCodesX, viewFacade);
        return button;
    }
}
