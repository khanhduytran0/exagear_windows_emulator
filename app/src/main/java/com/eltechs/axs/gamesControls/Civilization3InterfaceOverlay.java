package com.eltechs.axs.gamesControls;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import com.eltechs.axs.CommonApplicationConfigurationAccessor;
import com.eltechs.axs.GestureStateMachine.GestureMouseMode;
import com.eltechs.axs.GestureStateMachine.GestureMouseMode.MouseModeChangeListener;
import com.eltechs.axs.GestureStateMachine.GestureMouseMode.MouseModeState;
import com.eltechs.axs.KeyCodesX;
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
import com.eltechs.axs.widgets.helpers.ButtonHelpers;
import com.eltechs.axs.widgets.touchScreenControlsOverlay.TouchScreenControlsWidget;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.KeyButNames;
import com.eltechs.axs.xserver.KeyboardListener;
import com.eltechs.axs.xserver.KeyboardModifiersListener;
import com.eltechs.axs.xserver.ViewFacade;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.Arrays;

public class Civilization3InterfaceOverlay implements XServerDisplayActivityInterfaceOverlay, XServerDisplayActivityUiOverlaySidePanels {
    private static final float buttonSzNormalDisplayInches = 0.4f;
    private static final float buttonSzSmallDisplayInches = 0.3f;
    private static final float displaySizeThresholdInches = 3.0f;
    private final TouchScreenControlsFactory controlsFactory = new Civ3TouchScreenControlsFactory(this.mouseMode);
    private boolean isLeftToolbarVisible = true;
    private View leftToolbar;
    private final GestureMouseMode mouseMode = new GestureMouseMode(MouseModeState.MOUSE_MODE_LEFT);
    private TouchScreenControlsWidget tscWidget;

    public View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        this.tscWidget = new TouchScreenControlsWidget(xServerDisplayActivity, viewOfXServer, this.controlsFactory, TouchScreenControlsInputConfiguration.DEFAULT);
        this.tscWidget.setZOrderMediaOverlay(true);
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setOrientation(0);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout.addView(createLeftToolbar(xServerDisplayActivity, viewOfXServer));
        linearLayout.addView(this.tscWidget, new LayoutParams(0, -1, 1.0f));
        viewOfXServer.setHorizontalStretchEnabled(new CommonApplicationConfigurationAccessor().isHorizontalStretchEnabled());
        xServerDisplayActivity.addDefaultPopupMenu(Arrays.asList(new AbstractAction[]{new ShowKeyboard(), new ToggleHorizontalStretch(), new ToggleUiOverlaySidePanels(), new ShowUsage(), new Quit()}));
        return linearLayout;
    }

    public void detach() {
        this.tscWidget.detach();
        this.tscWidget = null;
        this.leftToolbar = null;
    }

    public boolean isSidePanelsVisible() {
        return this.isLeftToolbarVisible;
    }

    public void toggleSidePanelsVisibility() {
        this.isLeftToolbarVisible = !this.isLeftToolbarVisible;
        if (this.isLeftToolbarVisible) {
            this.leftToolbar.setVisibility(0);
        } else {
            this.leftToolbar.setVisibility(8);
        }
    }

    private View createLeftToolbar(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setLayoutParams(new LayoutParams(-2, -1, 0.0f));
        linearLayout.setOrientation(1);
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        int i = (int) ((isDisplaySmall(displayMetrics) ? buttonSzSmallDisplayInches : 0.4f) * ((float) displayMetrics.densityDpi));
        ViewFacade xServerFacade = viewOfXServer.getXServerFacade();
        linearLayout.addView(createMouseModeButton(xServerDisplayActivity, this.mouseMode, i));
        linearLayout.addView(createShiftButton(xServerDisplayActivity, xServerFacade, i));
        linearLayout.addView(createScrollViewWithButtons(xServerDisplayActivity, xServerFacade, i));
        if (!this.isLeftToolbarVisible) {
            linearLayout.setVisibility(8);
        }
        this.leftToolbar = linearLayout;
        return linearLayout;
    }

    private static boolean isDisplaySmall(DisplayMetrics displayMetrics) {
        return ((float) displayMetrics.widthPixels) / ((float) displayMetrics.densityDpi) < displaySizeThresholdInches;
    }

    private static Button createShiftButton(Activity activity, final ViewFacade viewFacade, int i) {
        final String string = activity.getResources().getString(R.string.civ3_shift_off);
        final String string2 = activity.getResources().getString(R.string.civ3_shift_on);
        final Button button = new Button(activity);
        button.setWidth(i);
        button.setMaxWidth(i);
        button.setMinWidth(i);
        button.setHeight(i);
        button.setMaxHeight(i);
        button.setMinHeight(i);
        button.setText(string);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                viewFacade.switchModifierState(KeyButNames.SHIFT, (byte) KeyCodesX.KEY_SHIFT_LEFT.getValue(), true);
            }
        });
        viewFacade.addKeyboardListener(new KeyboardListener() {
            public void keyPressed(byte b, int i, Mask<KeyButNames> mask) {
            }

            public void keyReleased(byte b, int i, Mask<KeyButNames> mask) {
                if (b != ((byte) KeyCodesX.KEY_SHIFT_LEFT.getValue()) && b != ((byte) KeyCodesX.KEY_SHIFT_RIGHT.getValue())) {
                    viewFacade.setModifierState(KeyButNames.SHIFT, false, (byte) KeyCodesX.KEY_SHIFT_LEFT.getValue(), true);
                }
            }
        });
        viewFacade.addKeyboardModifiersChangeListener(new KeyboardModifiersListener() {
            public void modifiersChanged(Mask<KeyButNames> mask) {
                if (mask.isSet(KeyButNames.SHIFT)) {
                    button.setText(string2);
                } else {
                    button.setText(string);
                }
            }
        });
        return button;
    }

    private static ScrollView createScrollViewWithButtons(Activity activity, ViewFacade viewFacade, int i) {
        ScrollView scrollView = new ScrollView(activity);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        linearLayout.setOrientation(1);
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_R, i, "R"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F, i, "F"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_I, i, "I"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_J, i, "J"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_M, i, "M"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_B, i, "B"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_P, i, "P"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_C, i, "C"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_S, i, "S"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_W, i, "W"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F1, i, "F1"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_DELETE, i, "DEL"));
        scrollView.addView(linearLayout);
        return scrollView;
    }

    private static ImageButton createMouseModeButton(Activity activity, final GestureMouseMode gestureMouseMode, int i) {
        final ImageButton createRegularImageButton = ButtonHelpers.createRegularImageButton(activity, i, i, R.drawable.mouse_left);
        createRegularImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (gestureMouseMode.getState().equals(MouseModeState.MOUSE_MODE_LEFT)) {
                    gestureMouseMode.setState(MouseModeState.MOUSE_MODE_RIGHT);
                } else {
                    gestureMouseMode.setState(MouseModeState.MOUSE_MODE_LEFT);
                }
            }
        });
        gestureMouseMode.addListener(new MouseModeChangeListener() {
            public void mouseModeChanged(GestureMouseMode gestureMouseMode, MouseModeState mouseModeState) {
                if (mouseModeState == MouseModeState.MOUSE_MODE_LEFT) {
                    createRegularImageButton.setImageResource(R.drawable.mouse_left);
                } else {
                    createRegularImageButton.setImageResource(R.drawable.mouse_right);
                }
            }
        });
        return createRegularImageButton;
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
