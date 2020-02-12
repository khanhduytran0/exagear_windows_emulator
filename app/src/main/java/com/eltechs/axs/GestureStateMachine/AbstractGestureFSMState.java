package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.finiteStateMachine.AbstractFSMState;

public abstract class AbstractGestureFSMState extends AbstractFSMState {
    private final GestureContext context;

    public AbstractGestureFSMState(GestureContext gestureContext) {
        this.context = gestureContext;
    }

    /* access modifiers changed from: protected */
    public GestureContext getContext() {
        return this.context;
    }
}
