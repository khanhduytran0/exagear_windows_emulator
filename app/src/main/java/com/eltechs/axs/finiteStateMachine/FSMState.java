package com.eltechs.axs.finiteStateMachine;

public interface FSMState {
    void notifyBecomeActive();

    void notifyBecomeInactive();
}
