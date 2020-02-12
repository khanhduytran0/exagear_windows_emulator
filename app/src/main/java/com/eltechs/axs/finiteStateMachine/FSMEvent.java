package com.eltechs.axs.finiteStateMachine;

public class FSMEvent {
    private final String name;

    public FSMEvent(String str) {
        this.name = str;
    }

    public FSMEvent() {
        this.name = FSMEvent.class.toString();
    }

    public String toString() {
        return this.name;
    }
}
