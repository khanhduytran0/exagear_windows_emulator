package com.eltechs.axs.finiteStateMachine.generalStates;

import com.eltechs.axs.finiteStateMachine.AbstractFSMState;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.FileHelpers;
import java.io.File;
import java.io.IOException;

public class FSMStateCopyFileIfExists extends AbstractFSMState {
    public static final FSMEvent COMPLETED = new FSMEvent() {
    };
    public static final FSMEvent FAILED = new FSMEvent() {
    };
    private final File from;
    private final File to;

    public void notifyBecomeInactive() {
    }

    public FSMStateCopyFileIfExists(File file, File file2) {
        this.from = file;
        this.to = file2;
    }

    public void notifyBecomeActive() {
        if (this.from.exists()) {
            try {
                FileHelpers.copyFile(this.from, this.to);
                sendEvent(COMPLETED);
            } catch (IOException unused) {
                sendEvent(FAILED);
            }
        } else {
            sendEvent(COMPLETED);
        }
    }
}
