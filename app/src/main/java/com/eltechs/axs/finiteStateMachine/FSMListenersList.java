package com.eltechs.axs.finiteStateMachine;

import java.util.ArrayList;
import java.util.List;

public class FSMListenersList {
    private final List<FSMListener> listeners = new ArrayList();

    public void addListener(FSMListener fSMListener) {
        this.listeners.add(fSMListener);
    }

    public void removeListener(FSMListener fSMListener) {
        this.listeners.remove(fSMListener);
    }

    public void sendLeftState(FSMState fSMState) {
        for (FSMListener leftState : this.listeners) {
            leftState.leftState(fSMState);
        }
    }

    public void sendEnteredState(FSMState fSMState) {
        for (FSMListener enteredState : this.listeners) {
            enteredState.enteredState(fSMState);
        }
    }
}
