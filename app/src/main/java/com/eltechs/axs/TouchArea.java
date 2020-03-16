package com.eltechs.axs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TouchArea {
    private final List<Finger> activeFingers = new ArrayList();
    private final TouchEventAdapter adapter;
    private final float bottomX;
    private final float bottomY;
    private final List<Finger> immutableActiveFingers = Collections.unmodifiableList(this.activeFingers);
    private final FingerAction lastFingerAction;
    private final float topX;
    private final float topY;

    public class FingerAction {
        private FingerActionType action = FingerActionType.NONE;
        private Finger finger = null;

        public FingerAction() {
        }

        public FingerActionType getAction() {
            return this.action;
        }

        public Finger getFinger() {
            return this.finger;
        }

        public void set(Finger finger2, FingerActionType fingerActionType) {
            this.finger = finger2;
            this.action = fingerActionType;
        }
    }

    public enum FingerActionType {
        NONE,
        TOUCH,
        MOVE_IN,
        MOVE,
        MOVE_OUT,
        RELEASE
    }

    public TouchArea(float f, float f2, float f3, float f4, TouchEventAdapter touchEventAdapter) {
        this.topX = f;
        this.topY = f2;
        this.bottomX = f3;
        this.bottomY = f4;
        this.adapter = touchEventAdapter;
        this.lastFingerAction = new FingerAction();
    }

    private void removeFinger(Finger finger) {
        for (Finger notifyFingersCountChanged : this.activeFingers) {
            notifyFingersCountChanged.notifyFingersCountChanged();
        }
        this.activeFingers.remove(finger);
    }

    private void addFinger(Finger finger) {
        this.activeFingers.add(finger);
        for (Finger notifyFingersCountChanged : this.activeFingers) {
            notifyFingersCountChanged.notifyFingersCountChanged();
        }
    }

    public void handleFingerDown(Finger finger) {
        if (isInside(finger)) {
            addFinger(finger);
            this.lastFingerAction.set(finger, FingerActionType.TOUCH);
            this.adapter.notifyTouched(finger, this.immutableActiveFingers);
        }
    }

    public void handleFingerUp(Finger finger) {
        if (this.activeFingers.contains(finger)) {
            removeFinger(finger);
            this.lastFingerAction.set(finger, FingerActionType.RELEASE);
            this.adapter.notifyReleased(finger, this.immutableActiveFingers);
        }
    }

    public void handleFingerMove(Finger finger) {
        if (isInside(finger)) {
            if (this.activeFingers.contains(finger)) {
                this.lastFingerAction.set(finger, FingerActionType.MOVE);
                this.adapter.notifyMoved(finger, this.immutableActiveFingers);
                return;
            }
            addFinger(finger);
            this.lastFingerAction.set(finger, FingerActionType.MOVE_IN);
            this.adapter.notifyMovedIn(finger, this.immutableActiveFingers);
        } else if (this.activeFingers.contains(finger)) {
            removeFinger(finger);
            this.lastFingerAction.set(finger, FingerActionType.MOVE_OUT);
            this.adapter.notifyMovedOut(finger, this.immutableActiveFingers);
        }
    }

    private boolean isInside(Finger finger) {
        float x = finger.getX();
        float y = finger.getY();
        return x > this.topX && x < this.bottomX && y > this.topY && y < this.bottomY;
    }

    public List<Finger> getFingers() {
        return this.immutableActiveFingers;
    }

    public FingerAction getLastFingerAction() {
        return this.lastFingerAction;
    }
}
