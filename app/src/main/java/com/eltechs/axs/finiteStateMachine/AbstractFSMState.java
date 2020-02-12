package com.eltechs.axs.finiteStateMachine;

import com.eltechs.axs.helpers.Assert;

public abstract class AbstractFSMState implements FSMState {
    private FiniteStateMachine machine;

    public final void attach(FiniteStateMachine finiteStateMachine) {
        Assert.state(this.machine == null, "Already attached to FSM!");
        this.machine = finiteStateMachine;
    }

    /* access modifiers changed from: protected */
    public final void sendEvent(FSMEvent fSMEvent) {
        this.machine.sendEvent(this, fSMEvent);
    }

    /* access modifiers changed from: protected */
    public final void sendEventIfActive(FSMEvent fSMEvent) {
        synchronized (this.machine) {
            if (this.machine.isActiveState(this)) {
                sendEvent(fSMEvent);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final FiniteStateMachine getMachine() {
        return this.machine;
    }
}
