package com.eltechs.axs.finiteStateMachine;

import com.eltechs.axs.helpers.Assert;
import java.util.ArrayList;
import java.util.Iterator;

public class FiniteStateMachine {
    private ArrayList<AbstractFSMState> allStates = new ArrayList<>();
    private AbstractFSMState currentState;
    private AbstractFSMState defaultState;
    private FSMListenersList listeners = new FSMListenersList();
    private ArrayList<FSMTransitionTableEntry> transitionTable = new ArrayList<>();

    private static class FSMTransitionTableEntry {
        public final FSMEvent event;
        public final AbstractFSMState postState;
        public final AbstractFSMState preState;

        public FSMTransitionTableEntry(AbstractFSMState abstractFSMState, FSMEvent fSMEvent, AbstractFSMState abstractFSMState2) {
            this.preState = abstractFSMState;
            this.event = fSMEvent;
            this.postState = abstractFSMState2;
        }
    }

    public void setStatesList(AbstractFSMState... abstractFSMStateArr) {
        for (AbstractFSMState abstractFSMState : abstractFSMStateArr) {
            abstractFSMState.attach(this);
            this.allStates.add(abstractFSMState);
        }
    }

    public void setInitialState(AbstractFSMState abstractFSMState) {
        Assert.state(this.allStates.contains(abstractFSMState));
        this.currentState = abstractFSMState;
    }

    public void setDefaultState(AbstractFSMState abstractFSMState) {
        this.defaultState = abstractFSMState;
    }

    public void configurationCompleted() {
        boolean z = false;
        Assert.state(this.currentState != null, "Initial state not set");
        if (this.defaultState != null) {
            z = true;
        }
        Assert.state(z, "Default state not set");
        Assert.state(!this.transitionTable.isEmpty(), "Transitional table is not initialized");
        Assert.state(!this.allStates.isEmpty(), "States are not set");
        this.currentState.notifyBecomeActive();
    }

    public void addTransition(AbstractFSMState abstractFSMState, FSMEvent fSMEvent, AbstractFSMState abstractFSMState2) {
        Assert.state(this.allStates.contains(abstractFSMState), "Transition from unknown state");
        Assert.state(this.allStates.contains(abstractFSMState2), "Transition to unknown state");
        this.transitionTable.add(new FSMTransitionTableEntry(abstractFSMState, fSMEvent, abstractFSMState2));
    }

    public boolean isActiveState(AbstractFSMState abstractFSMState) {
        boolean z;
        synchronized (this) {
            z = this.currentState == abstractFSMState;
        }
        return z;
    }

    private void changeState(AbstractFSMState abstractFSMState) {
        this.currentState.notifyBecomeInactive();
        this.listeners.sendLeftState(this.currentState);
        this.currentState = abstractFSMState;
        this.currentState.notifyBecomeActive();
        this.listeners.sendEnteredState(this.currentState);
    }

    private AbstractFSMState getNextStateByCurrentStateAndEvent(AbstractFSMState abstractFSMState, FSMEvent fSMEvent) {
        Iterator it = this.transitionTable.iterator();
        while (it.hasNext()) {
            FSMTransitionTableEntry fSMTransitionTableEntry = (FSMTransitionTableEntry) it.next();
            if (fSMTransitionTableEntry.preState == abstractFSMState && fSMTransitionTableEntry.event == fSMEvent) {
                return fSMTransitionTableEntry.postState;
            }
        }
        return this.defaultState;
    }

    public void sendEvent(AbstractFSMState abstractFSMState, FSMEvent fSMEvent) {
        synchronized (this) {
            Assert.state(abstractFSMState == this.currentState);
            changeState(getNextStateByCurrentStateAndEvent(this.currentState, fSMEvent));
        }
    }

    public void addListener(FSMListener fSMListener) {
        synchronized (this) {
            this.listeners.addListener(fSMListener);
        }
    }

    public void removeListener(FSMListener fSMListener) {
        synchronized (this) {
            this.listeners.removeListener(fSMListener);
        }
    }
}
