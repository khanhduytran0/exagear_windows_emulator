package com.eltechs.axs.configuration.startup;

import android.content.Context;
import android.util.Log;
import com.eltechs.axs.activities.BufferedListenerInvoker;
import com.eltechs.axs.activities.FrameworkActivity;
import com.eltechs.axs.activities.StartupActivity;
import com.eltechs.axs.configuration.startup.actions.StartupActionCompletionListener;
import com.eltechs.axs.configuration.startup.actions.StartupStepInfoListener;
import com.eltechs.axs.configuration.startup.actions.UserInteractionRequestListener;
import com.eltechs.axs.container.annotations.Autowired;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.UiThread;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StartupActionsCollection<StateClass> {
    private static final String LOG_TAG = "StartupActions";
    /* access modifiers changed from: private */
    public final BufferedListenerInvoker<StartupActionCompletionListener> actionCompletionReporter = new BufferedListenerInvoker<>(StartupActionCompletionListener.class);
    /* access modifiers changed from: private */
    public final Deque<StartupAction> actions = new ArrayDeque<StartupAction>();
    private final Context applicationContext;
    private final Executor asyncStartupActionsExecutor = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public ActionState currentActionState = ActionState.NOT_YET_STARTED;
    private final BufferedListenerInvoker<StartupStepInfoListener> infoUpdater = new BufferedListenerInvoker<>(StartupStepInfoListener.class);
    private boolean isFinished;
    /* access modifiers changed from: private */
    public final BufferedListenerInvoker<UserInteractionRequestListener> userInteractionRequester = new BufferedListenerInvoker<>(UserInteractionRequestListener.class);

    private enum ActionState {
        NOT_YET_STARTED,
        RUNNING,
        AWAITING_RESPONSE
    }

    public StartupActionsCollection(Context context) {
        this.applicationContext = context;
    }

    @Autowired
    private void setStartupActivity(StartupActivity<?> startupActivity) {
        UiThread.check();
        this.actionCompletionReporter.setRealListener(startupActivity);
        this.userInteractionRequester.setRealListener(startupActivity);
        this.infoUpdater.setRealListener(startupActivity);
    }

    public void addAction(StartupAction<StateClass> startupAction) {
        UiThread.check();
        startupAction.attach(this);
        this.actions.addLast(startupAction);
    }

    public void addActions(List<StartupAction<StateClass>> list) {
        for (StartupAction addAction : list) {
            addAction(addAction);
        }
    }

    public boolean runAction() {
        UiThread.check();
        if (this.actions.isEmpty()) {
            logDebug("runAction() found no more startup actions\n", new Object[0]);
            this.isFinished = true;
            return false;
        } else if (this.currentActionState != ActionState.NOT_YET_STARTED) {
            Assert.state(false, String.format("runAction() called with the current action in invalid state %s.", new Object[]{this.currentActionState}));
            return false;
        } else {
            this.currentActionState = ActionState.RUNNING;
            final StartupAction currentAction = getCurrentAction();
            setStepInfo(currentAction.getInfo());
            if (!isAsyncAction(currentAction)) {
                UiThread.post(new Runnable() {
                    public void run() {
                        currentAction.execute();
                    }
                });
            } else {
                this.asyncStartupActionsExecutor.execute(new Runnable() {
                    public void run() {
                        currentAction.execute();
                    }
                });
            }
            return true;
        }
    }

    public void actionDone(final StartupAction startupAction) {
        UiThread.post(new Runnable() {
            public void run() {
                boolean z = true;
                StartupActionsCollection.this.logDebug("actionDone(%s)\n", startupAction);
                Assert.state(StartupActionsCollection.this.currentActionState == ActionState.RUNNING, String.format("actionDone() called with the current action in invalid state %s.", new Object[]{StartupActionsCollection.this.currentActionState}));
                if (startupAction != StartupActionsCollection.this.getCurrentAction()) {
                    z = false;
                }
                Assert.state(z, "An invalid action has reported the completion of itself.");
                StartupActionsCollection.this.actions.removeFirst();
                StartupActionsCollection.this.currentActionState = ActionState.NOT_YET_STARTED;
                ((StartupActionCompletionListener) StartupActionsCollection.this.actionCompletionReporter.getProxy()).actionDone(startupAction);
            }
        });
    }

    public void actionFailed(final StartupAction startupAction, final String str) {
        UiThread.post(new Runnable() {
            public void run() {
                boolean z = false;
                StartupActionsCollection.this.logDebug("actionFailed(%s, '%s')\n", startupAction, str);
                Assert.state(StartupActionsCollection.this.currentActionState == ActionState.RUNNING, String.format("actionFailed() called with the current action in invalid state %s.", new Object[]{StartupActionsCollection.this.currentActionState}));
                if (startupAction == StartupActionsCollection.this.getCurrentAction()) {
                    z = true;
                }
                Assert.state(z, "An invalid action has reported a failure in itself.");
                StartupActionsCollection.this.actions.removeFirst();
                StartupActionsCollection.this.currentActionState = ActionState.NOT_YET_STARTED;
                ((StartupActionCompletionListener) StartupActionsCollection.this.actionCompletionReporter.getProxy()).actionFailed(startupAction, str);
            }
        });
    }

    public void userInteractionFinished(Serializable serializable) {
        UiThread.check();
        logDebug("userInteractionFinished()\n", new Object[0]);
        Assert.state(this.currentActionState == ActionState.AWAITING_RESPONSE, "userInteractionFinished() called but the current action expects no user input.");
        this.currentActionState = ActionState.RUNNING;
        Assert.state(getCurrentAction() instanceof InteractiveStartupAction, "Only interactive startup actions can receive nontrivial user responses.");
        ((InteractiveStartupAction) getCurrentAction()).userInteractionFinished(serializable);
    }

    public void userInteractionCanceled() {
        UiThread.check();
        boolean z = false;
        logDebug("userInteractionCanceled()\n", new Object[0]);
        if (this.currentActionState == ActionState.AWAITING_RESPONSE) {
            z = true;
        }
        Assert.state(z, "userInteractionCanceled() called but the current action expects no user input.");
        this.currentActionState = ActionState.RUNNING;
        Assert.state(getCurrentAction() instanceof InteractiveStartupAction, "Only interactive startup actions can receive nontrivial user responses.");
        ((InteractiveStartupAction) getCurrentAction()).userInteractionCanceled();
    }

    public Context getAndroidApplicationContext() {
        return this.applicationContext;
    }

    public void requestUserInput(final Class<? extends FrameworkActivity> cls, final Serializable serializable) {
        UiThread.post(new Runnable() {
            public void run() {
                Assert.state(StartupActionsCollection.this.currentActionState == ActionState.RUNNING, String.format("User input has been requested in state %s; can do it only in RUNNING state.", new Object[]{StartupActionsCollection.this.currentActionState}));
                Assert.state(StartupActionsCollection.this.getCurrentAction() instanceof InteractiveStartupAction, "Only interactive startup actions can request user input.");
                StartupActionsCollection.this.currentActionState = ActionState.AWAITING_RESPONSE;
                ((UserInteractionRequestListener) StartupActionsCollection.this.userInteractionRequester.getProxy()).requestUserInput(cls, serializable);
            }
        });
    }

    public void requestUserInput() {
        UiThread.post(new Runnable() {
            public void run() {
                Assert.state(StartupActionsCollection.this.currentActionState == ActionState.RUNNING, String.format("User input has been requested in state %s; can do it only in RUNNING state.", new Object[]{StartupActionsCollection.this.currentActionState}));
                Assert.state(StartupActionsCollection.this.getCurrentAction() instanceof InteractiveStartupAction, "Only interactive startup actions can request user input.");
                StartupActionsCollection.this.currentActionState = ActionState.AWAITING_RESPONSE;
            }
        });
    }

    public boolean isWaitingForUserInput() {
        UiThread.check();
        return this.currentActionState == ActionState.AWAITING_RESPONSE;
    }

    public boolean isFinished() {
        UiThread.check();
        return this.isFinished;
    }

    /* access modifiers changed from: private */
    public StartupAction getCurrentAction() {
        UiThread.check();
        return (StartupAction) this.actions.getFirst();
    }

    private void setStepInfo(StartupActionInfo startupActionInfo) {
        ((StartupStepInfoListener) this.infoUpdater.getProxy()).setStepInfo(startupActionInfo);
    }

    private boolean isAsyncAction(StartupAction startupAction) {
        return startupAction instanceof AsyncStartupAction;
    }

    /* access modifiers changed from: private */
    public void logDebug(String str, Object... objArr) {
        Log.d(LOG_TAG, String.format(str, objArr));
    }
}
