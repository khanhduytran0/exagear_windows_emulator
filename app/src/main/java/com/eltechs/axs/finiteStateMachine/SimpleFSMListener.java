package com.eltechs.axs.finiteStateMachine;

public abstract class SimpleFSMListener implements FSMListener {
    public void leftState(FSMState fSMState) {
    }

    /* access modifiers changed from: protected */
    public abstract void stateSwitched(FSMState fSMState);

    public void enteredState(FSMState fSMState) {
        stateSwitched(fSMState);
    }
}
