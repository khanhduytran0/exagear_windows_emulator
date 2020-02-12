package com.eltechs.axs.GestureStateMachine;

public class PointerContext {
    private MoveMethod lastMoveMethod = MoveMethod.NOT_INIT;
    private long lastMoveTimestamp;

    public enum MoveMethod {
        TAP,
        AIM,
        NOT_INIT
    }

    public long getLastMoveTimestamp() {
        return this.lastMoveTimestamp;
    }

    public MoveMethod getLastMoveMethod() {
        return this.lastMoveMethod;
    }

    public void setLastMoveMethod(MoveMethod moveMethod) {
        this.lastMoveMethod = moveMethod;
        this.lastMoveTimestamp = System.currentTimeMillis();
    }
}
