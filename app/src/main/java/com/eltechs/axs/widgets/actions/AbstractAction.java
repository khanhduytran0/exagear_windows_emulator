package com.eltechs.axs.widgets.actions;

import com.eltechs.axs.Globals;
import com.eltechs.axs.activities.FrameworkActivity;
import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.helpers.Assert;

public abstract class AbstractAction implements Action {
    protected static final boolean CHECKABLE = true;
    protected static final boolean NOT_CHECKABLE = false;
    private final boolean checkable;
    private boolean enabled;
    private String name;

    protected AbstractAction(String str) {
        this(str, false);
    }

    protected AbstractAction(String str, boolean z) {
        this.name = str;
        this.enabled = true;
        this.checkable = z;
    }

    public String getName() {
        return this.name;
    }

    /* access modifiers changed from: protected */
    public void setName(String str) {
        this.name = str;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    /* access modifiers changed from: protected */
    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public final boolean isCheckable() {
        return this.checkable;
    }

    public boolean isChecked() {
        Assert.state(!this.checkable, "Checkable Actions must implement isChecked().");
        return false;
    }

    /* access modifiers changed from: protected */
    public final XServerDisplayActivity getCurrentXServerDisplayActivity() {
        FrameworkActivity currentActivity = ((ApplicationStateBase) Globals.getApplicationState()).getCurrentActivity();
        Assert.state(currentActivity instanceof XServerDisplayActivity, String.format("A menu was requested by %s which is not a XServerDisplayActivity.", new Object[]{currentActivity}));
        return (XServerDisplayActivity) currentActivity;
    }
}
