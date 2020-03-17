package com.eltechs.axs.finiteStateMachine.generalStates;

import com.eltechs.axs.finiteStateMachine.AbstractFSMState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import java.io.File;

public class FSMStateCheckFileExists extends AbstractFSMState {
    public static final FSMEvent DOESNT_EXISTS = new FSMEvent() {
    };
    public static final FSMEvent EXISTS = new FSMEvent() {
    };
    private final File file;

    public void notifyBecomeInactive() {
    }

    public FSMStateCheckFileExists(File file2) {
        this.file = file2;
    }

    public void notifyBecomeActive() {
        sendEvent(this.file.exists() ? EXISTS : DOESNT_EXISTS);
    }
}
