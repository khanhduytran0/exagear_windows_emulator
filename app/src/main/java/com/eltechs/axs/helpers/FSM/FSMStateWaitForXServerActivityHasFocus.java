package com.eltechs.axs.helpers.FSM;

import com.eltechs.axs.activities.XServerDisplayActivity;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.finiteStateMachine.AbstractFSMState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.InfiniteTimer;

public class FSMStateWaitForXServerActivityHasFocus extends AbstractFSMState {
    public static final FSMEvent SUCCESS = new FSMEvent() {
    };
    /* access modifiers changed from: private */
    public final ApplicationStateBase as;
    private final int timeoutMs;
    private InfiniteTimer timer;

    public FSMStateWaitForXServerActivityHasFocus(int i, ApplicationStateBase applicationStateBase) {
        this.timeoutMs = i;
        this.as = applicationStateBase;
    }

    public void notifyBecomeActive() {
        this.timer = new InfiniteTimer((long) this.timeoutMs) {
            public void onTick(long j) {
                if (FSMStateWaitForXServerActivityHasFocus.this.getMachine().isActiveState(FSMStateWaitForXServerActivityHasFocus.this) && FSMStateWaitForXServerActivityHasFocus.this.as.getCurrentActivity() != null && FSMStateWaitForXServerActivityHasFocus.this.as.getCurrentActivity().getClass() == XServerDisplayActivity.class) {
                    FSMStateWaitForXServerActivityHasFocus.this.sendEvent(FSMStateWaitForXServerActivityHasFocus.SUCCESS);
                }
            }
        };
        this.timer.start();
    }

    public void notifyBecomeInactive() {
        this.timer.cancel();
        this.timer = null;
    }
}
