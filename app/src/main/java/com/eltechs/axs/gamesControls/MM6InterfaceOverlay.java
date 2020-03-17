package com.eltechs.axs.gamesControls;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.eltechs.axs.StickyKeyPress;
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
import com.eltechs.axs.xserver.KeyboardModifiersListener;
import com.eltechs.axs.xserver.ViewFacade;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.Arrays;

public class MM6InterfaceOverlay implements XServerDisplayActivityInterfaceOverlay, XServerDisplayActivityUiOverlaySidePanels {
    public static final int KEY_PRESS_INTERVAL_MS = 200;
    private static final float buttonSzNormalDisplayInches = 0.4f;
    private static final float buttonSzSmallDisplayInches = 0.3f;
    private static final float displaySizeThresholdInches = 5.0f;
    private final int buttonWidthPixelsFixup = 30;
    private final TouchScreenControlsFactory controlsFactory = new MM6TouchScreenControlsFactory(this.mouseMode);
    private boolean isToolbarsVisible = true;
    private View leftToolbar;
    private final GestureMouseMode mouseMode = new GestureMouseMode(MouseModeState.MOUSE_MODE_RIGHT);
    private View rightToolbar;
    private TouchScreenControlsWidget tscWidget;

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

    private static boolean isDisplaySmall(DisplayMetrics displayMetrics) {
        return ((float) displayMetrics.widthPixels) / ((float) displayMetrics.densityDpi) < displaySizeThresholdInches;
    }

    private static Button createRunButton(Activity activity, final ViewFacade viewFacade, int i) {
        final String string = activity.getResources().getString(R.string.mm_run_off);
        final String string2 = activity.getResources().getString(R.string.mm_run_on);
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

    private View createLeftToolbar(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setLayoutParams(new LayoutParams(-2, -1, 0.0f));
        linearLayout.setOrientation(1);
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        int i = (int) ((isDisplaySmall(displayMetrics) ? buttonSzSmallDisplayInches : 0.4f) * ((float) displayMetrics.densityDpi));
        ViewFacade xServerFacade = viewOfXServer.getXServerFacade();
        linearLayout.addView(createRunButton(xServerDisplayActivity, xServerFacade, i));
        linearLayout.addView(createLeftScrollViewWithButtons(xServerDisplayActivity, xServerFacade, i));
        if (!this.isToolbarsVisible) {
            linearLayout.setVisibility(8);
        }
        this.leftToolbar = linearLayout;
        return linearLayout;
    }

    private View createRightToolbar(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
        LinearLayout linearLayout = new LinearLayout(xServerDisplayActivity);
        linearLayout.setLayoutParams(new LayoutParams(-2, -1, 0.0f));
        linearLayout.setOrientation(1);
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        int i = (int) ((isDisplaySmall(displayMetrics) ? buttonSzSmallDisplayInches : 0.4f) * ((float) displayMetrics.densityDpi));
        ViewFacade xServerFacade = viewOfXServer.getXServerFacade();
        linearLayout.addView(createMouseModeButton(xServerDisplayActivity, this.mouseMode, i, (isDisplaySmall(displayMetrics) ? 30 : 0) + i));
        linearLayout.addView(createRightScrollViewWithButtons(xServerDisplayActivity, xServerFacade, i));
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
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_A, i, "A"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_S, i, "S"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_X, i, "X"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_RETURN, i, "Entr"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_SPACE, i, "Spc"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F11, i, "F11"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_F3, i, "F3"));
        scrollView.addView(linearLayout);
        return scrollView;
    }

    private static ScrollView createLeftScrollViewWithButtons(Activity activity, ViewFacade viewFacade, int i) {
        ScrollView scrollView = new ScrollView(activity);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        linearLayout.setOrientation(1);
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_NEXT, i, "PgD"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_END, i, "End"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_DELETE, i, "Del"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_PRIOR, i, "PgU"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_HOME, i, "Hm"));
        linearLayout.addView(createLetterButton(activity, viewFacade, KeyCodesX.KEY_INSERT, i, "Ins"));
        scrollView.addView(linearLayout);
        return scrollView;
    }

    private static ImageButton createMouseModeButton(Activity activity, final GestureMouseMode gestureMouseMode, int i, int i2) {
        final ImageButton createRegularImageButton = ButtonHelpers.createRegularImageButton(activity, i, i2, R.drawable.mouse_right);
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

    private static void setHandlerToButton(Button button, KeyCodesX keyCodesX, ViewFacade viewFacade) {
        final StickyKeyPress stickyKeyPress = new StickyKeyPress(200, (byte) keyCodesX.getValue(), viewFacade);
        button.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                    case 5:
                        stickyKeyPress.start();
                        break;
                    case 1:
                    case 3:
                    case 4:
                    case 6:
                        stickyKeyPress.cancel();
                        break;
                    default:
                        return false;
                }
                return true;
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
