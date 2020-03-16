package com.eltechs.axs.helpers.FSM;

import com.eltechs.axs.finiteStateMachine.AbstractFSMState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.OneShotTimer;

public class FSMStateWaitForTimeout extends AbstractFSMState {
    public static final FSMEvent TIMEOUT = new FSMEvent() {
    };
    private final int timeoutMs;
    private OneShotTimer timer;

    public FSMStateWaitForTimeout(int i) {
        this.timeoutMs = i;
    }

    public void notifyBecomeActive() {
        this.timer = new OneShotTimer((long) this.timeoutMs) {
            public void onFinish() {
                FSMStateWaitForTimeout.this.sendEvent(FSMStateWaitForTimeout.TIMEOUT);
            }
        };
        this.timer.start();
    }

    public void notifyBecomeInactive() {
        this.timer.cancel();
        this.timer = null;
    }
}
