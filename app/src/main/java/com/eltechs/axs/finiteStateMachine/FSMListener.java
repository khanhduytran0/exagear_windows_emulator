package com.eltechs.axs.finiteStateMachine;

public interface FSMListener {
    void enteredState(FSMState fSMState);

    void leftState(FSMState fSMState);
}
