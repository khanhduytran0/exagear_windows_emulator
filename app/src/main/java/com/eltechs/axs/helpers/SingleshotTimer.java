package com.eltechs.axs.helpers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SingleshotTimer {
    private final Timer impl = new Timer();
    /* access modifiers changed from: private */
    public final ArrayList<TimerTaskImpl> scheduledActions = new ArrayList<>();

    private class TimerTaskImpl extends TimerTask {
        private final Runnable action;
        private boolean cancelled;

        TimerTaskImpl(Runnable runnable) {
            this.action = runnable;
        }

        public void run() {
            synchronized (SingleshotTimer.this) {
                if (!this.cancelled) {
                    this.action.run();
                    SingleshotTimer.this.scheduledActions.remove(this);
                }
            }
        }

        public boolean cancel() {
            boolean cancel;
            synchronized (SingleshotTimer.this) {
                SingleshotTimer.this.scheduledActions.remove(this);
                this.cancelled = true;
                cancel = super.cancel();
            }
            return cancel;
        }
    }

    public synchronized TimerTask schedule(int i, Runnable runnable) {
        TimerTaskImpl timerTaskImpl;
        timerTaskImpl = new TimerTaskImpl(runnable);
        this.scheduledActions.add(timerTaskImpl);
        this.impl.schedule(timerTaskImpl, (long) i);
        return timerTaskImpl;
    }

    public synchronized void cancel() {
        while (!this.scheduledActions.isEmpty()) {
            ((TimerTaskImpl) this.scheduledActions.get(this.scheduledActions.size() - 1)).cancel();
        }
    }
}
