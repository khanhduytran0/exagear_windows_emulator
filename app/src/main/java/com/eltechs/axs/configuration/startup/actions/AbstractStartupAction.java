package com.eltechs.axs.configuration.startup.actions;

import android.content.Context;
import com.eltechs.axs.Globals;
import com.eltechs.axs.configuration.startup.StartupAction;
import com.eltechs.axs.configuration.startup.StartupActionInfo;
import com.eltechs.axs.configuration.startup.StartupActionsCollection;
import com.eltechs.axs.helpers.Assert;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang3.StringEscapeUtils;

public abstract class AbstractStartupAction<StateClass> implements StartupAction<StateClass> {
    private volatile StartupActionsCollection<StateClass> startupActions;

    public final void attach(StartupActionsCollection<StateClass> startupActionsCollection) {
        Assert.state(this.startupActions == null, "Already registered within a startup actions collection.");
        this.startupActions = startupActionsCollection;
    }

    public StartupActionInfo getInfo() {
        return new StartupActionInfo("");
    }

    /* access modifiers changed from: protected */
    public StartupActionsCollection<StateClass> getStartupActions() {
        return this.startupActions;
    }

    /* access modifiers changed from: protected */
    public StateClass getApplicationState() {
        return Globals.getApplicationState();
    }

    /* access modifiers changed from: protected */
    public Context getAppContext() {
        return this.startupActions.getAndroidApplicationContext();
    }

    /* access modifiers changed from: protected */
    public final String getString(int i) {
        return getAppContext().getString(i);
    }

    /* access modifiers changed from: protected */
    public final void sendDone() {
        this.startupActions.actionDone(this);
    }

    /* access modifiers changed from: protected */
    public final void sendError(String str) {
        sendErrorHtml(String.format("<html><body>%s</body></html>", new Object[]{StringEscapeUtils.escapeHtml4(str)}));
    }

    /* access modifiers changed from: protected */
    public final void sendError(String str, Throwable th) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        sendErrorHtml(String.format("<html><body>%s<br><br><pre>%s</pre></body></html>", new Object[]{StringEscapeUtils.escapeHtml4(str), StringEscapeUtils.escapeHtml4(stringWriter.toString())}));
    }

    /* access modifiers changed from: protected */
    public final void sendErrorHtml(String str) {
        this.startupActions.actionFailed(this, str);
    }

    public String toString() {
        return String.format("[Startup action: %s]", new Object[]{getClass().getSimpleName()});
    }
}
