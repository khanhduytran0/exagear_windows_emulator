package com.eltechs.axs.widgets.touchScreenControlsOverlay;

import android.support.v4.view.InputDeviceCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import com.eltechs.axs.Finger;
import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.KeyEventReporter;
import com.eltechs.axs.Keyboard;
import com.eltechs.axs.Mouse;
import com.eltechs.axs.PointerEventReporter;
import com.eltechs.axs.TouchScreenControls;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.configuration.TouchScreenControlsInputConfiguration;
import com.eltechs.axs.configuration.TouchScreenControlsInputConfiguration.BackKeyAction;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.xserver.ViewFacade;

public class TouchScreenControlsInputWidget extends View {
    private final int MAX_FINGERS = 10;
    /* access modifiers changed from: private */
    public final TouchScreenControlsInputConfiguration configuration;
    /* access modifiers changed from: private */
    public final Keyboard keyboard;
    private final Mouse mouse;
    private TouchScreenControls touchScreenControls;
    private final Finger[] userFingers = new Finger[10];
    /* access modifiers changed from: private */
    public final ViewFacade xServerFacade;

    public TouchScreenControlsInputWidget(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer, TouchScreenControlsInputConfiguration touchScreenControlsInputConfiguration) {
        super(xServerDisplayActivity);
        this.xServerFacade = viewOfXServer.getXServerFacade();
        this.mouse = new Mouse(new PointerEventReporter(viewOfXServer));
        this.keyboard = new Keyboard(new KeyEventReporter(this.xServerFacade));
        this.configuration = touchScreenControlsInputConfiguration;
        setFocusable(true);
        setFocusableInTouchMode(true);
        installKeyListener();
    }

    public void setTouchScreenControls(TouchScreenControls touchScreenControls2) {
        this.touchScreenControls = touchScreenControls2;
    }

    private void installKeyListener() {
        setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getSource() == 8194) {
                    if (i == 4) {
                        if (keyEvent.getAction() == 0) {
                            TouchScreenControlsInputWidget.this.xServerFacade.injectPointerButtonPress(3);
                        } else if (keyEvent.getAction() == 1) {
                            TouchScreenControlsInputWidget.this.xServerFacade.injectPointerButtonRelease(3);
                        }
                    }
                    return true;
                } else if (i == 82 && keyEvent.getAction() == 1) {
                    TouchScreenControlsInputWidget.this.getHost().showPopupMenu();
                    return true;
                } else if (i == 4 && TouchScreenControlsInputWidget.this.configuration.backKeyAction == BackKeyAction.SHOW_POPUP_MENU) {
                    if (keyEvent.getAction() == 1) {
                        TouchScreenControlsInputWidget.this.getHost().showPopupMenu();
                    }
                    return true;
                } else if (i == 23) {
                    if (keyEvent.getAction() == 0) {
                        TouchScreenControlsInputWidget.this.xServerFacade.injectPointerButtonPress(3);
                    } else if (keyEvent.getAction() == 1) {
                        TouchScreenControlsInputWidget.this.xServerFacade.injectPointerButtonRelease(3);
                    }
                    return true;
                } else if (i == 102 || i == 104) {
                    if (keyEvent.getAction() == 0) {
                        TouchScreenControlsInputWidget.this.xServerFacade.injectKeyPress((byte) KeyCodesX.KEY_SHIFT_LEFT.getValue());
                    } else if (keyEvent.getAction() == 1) {
                        TouchScreenControlsInputWidget.this.xServerFacade.injectKeyRelease((byte) KeyCodesX.KEY_SHIFT_LEFT.getValue());
                    }
                    return true;
                } else if (i == 103 || i == 105) {
                    if (keyEvent.getAction() == 0) {
                        TouchScreenControlsInputWidget.this.xServerFacade.injectKeyPress((byte) KeyCodesX.KEY_CONTROL_LEFT.getValue());
                    } else if (keyEvent.getAction() == 1) {
                        TouchScreenControlsInputWidget.this.xServerFacade.injectKeyRelease((byte) KeyCodesX.KEY_CONTROL_LEFT.getValue());
                    }
                    return true;
                } else {
                    if (i != 0) {
                        if (keyEvent.getAction() == 0) {
                            return TouchScreenControlsInputWidget.this.keyboard.handleKeyDown(i, keyEvent);
                        }
                        if (keyEvent.getAction() == 1) {
                            return TouchScreenControlsInputWidget.this.keyboard.handleKeyUp(i, keyEvent);
                        }
                    } else if (keyEvent.getAction() == 2) {
                        return TouchScreenControlsInputWidget.this.keyboard.handleUnicodeKeyType(keyEvent);
                    }
                    return false;
                }
            }
        });
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        int source = motionEvent.getSource() & InputDeviceCompat.SOURCE_TOUCHSCREEN;
        boolean z = false;
        boolean z2 = (motionEvent.getSource() & InputDeviceCompat.SOURCE_STYLUS) == 16386;
        if ((motionEvent.getSource() & 8194) == 8194) {
            z = true;
        }
        if (z2 || z) {
            return this.mouse.handleMouseEvent(motionEvent);
        }
        return super.onGenericMotionEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        boolean z2 = (motionEvent.getSource() & InputDeviceCompat.SOURCE_TOUCHSCREEN) == 4098;
        boolean z3 = (motionEvent.getSource() & InputDeviceCompat.SOURCE_STYLUS) == 16386;
        if ((motionEvent.getSource() & 8194) == 8194) {
            z = true;
        }
        if (z2 || z3) {
            return handleTouchEvent(motionEvent);
        }
        if (z) {
            return this.mouse.handleMouseEvent(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }

    private boolean handleTouchEvent(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(actionIndex);
        int actionMasked = motionEvent.getActionMasked();
        if (pointerId >= 10) {
            return true;
        }
        int i = 0;
        switch (actionMasked) {
            case 0:
            case 5:
                this.userFingers[pointerId] = new Finger(motionEvent.getX(actionIndex), motionEvent.getY(actionIndex));
                this.touchScreenControls.handleFingerDown(this.userFingers[pointerId]);
                break;
            case 1:
            case 6:
                if (this.userFingers[pointerId] != null) {
                    this.userFingers[pointerId].release(motionEvent.getX(actionIndex), motionEvent.getY(actionIndex));
                    this.touchScreenControls.handleFingerUp(this.userFingers[pointerId]);
                    this.userFingers[pointerId] = null;
                    break;
                }
                break;
            case 2:
                while (i < 10) {
                    if (this.userFingers[i] != null) {
                        int findPointerIndex = motionEvent.findPointerIndex(i);
                        if (findPointerIndex >= 0) {
                            this.userFingers[i].update(motionEvent.getX(findPointerIndex), motionEvent.getY(findPointerIndex));
                            this.touchScreenControls.handleFingerMove(this.userFingers[i]);
                        } else {
                            this.touchScreenControls.handleFingerUp(this.userFingers[i]);
                            this.userFingers[i] = null;
                        }
                    }
                    i++;
                }
                break;
            case 3:
                while (i < 10) {
                    if (this.userFingers[i] != null) {
                        this.touchScreenControls.handleFingerUp(this.userFingers[i]);
                        this.userFingers[i] = null;
                    }
                    i++;
                }
                break;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public XServerDisplayActivity getHost() {
        return (XServerDisplayActivity) getContext();
    }
}
