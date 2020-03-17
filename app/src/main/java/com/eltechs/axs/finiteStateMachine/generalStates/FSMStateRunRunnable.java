package com.eltechs.axs.finiteStateMachine.generalStates;

import com.eltechs.axs.finiteStateMachine.AbstractFSMState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;

public class FSMStateRunRunnable extends AbstractFSMState {
    public static FSMEvent COMPLETED = new FSMEvent() {
    };
    private final Runnable runnable;

    public void notifyBecomeInactive() {
    }

    public FSMStateRunRunnable(Runnable runnable2) {
        this.runnable = runnable2;
    }

    public void notifyBecomeActive() {
        this.runnable.run();
        sendEvent(COMPLETED);
    }
}
