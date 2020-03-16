package com.eltechs.axs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SingleTouchEventAdapter implements TouchEventAdapter {
    private Finger finger = null;
    private final Collection<PointerEventListener> pointerEventListeners = new ArrayList();

    public void addListener(PointerEventListener... pointerEventListenerArr) {
        for (PointerEventListener add : pointerEventListenerArr) {
            this.pointerEventListeners.add(add);
        }
    }

    public void removeListener(PointerEventListener... pointerEventListenerArr) {
        for (PointerEventListener remove : pointerEventListenerArr) {
            this.pointerEventListeners.remove(remove);
        }
    }

    public void notifyTouched(Finger finger2, List<Finger> list) {
        if (this.finger == null) {
            this.finger = finger2;
            for (PointerEventListener pointerEntered : this.pointerEventListeners) {
                pointerEntered.pointerEntered(finger2.getX(), finger2.getY());
            }
        }
    }

    public void notifyReleased(Finger finger2, List<Finger> list) {
        if (this.finger == finger2) {
            for (PointerEventListener pointerExited : this.pointerEventListeners) {
                pointerExited.pointerExited(finger2.getX(), finger2.getY());
            }
            this.finger = null;
        }
    }

    public void notifyMoved(Finger finger2, List<Finger> list) {
        if (this.finger == finger2) {
            for (PointerEventListener pointerMove : this.pointerEventListeners) {
                pointerMove.pointerMove(finger2.getX(), finger2.getY());
            }
        }
    }

    public void notifyMovedIn(Finger finger2, List<Finger> list) {
        if (this.finger == null) {
            this.finger = finger2;
            for (PointerEventListener pointerEntered : this.pointerEventListeners) {
                pointerEntered.pointerEntered(finger2.getX(), finger2.getY());
            }
        }
    }

    public void notifyMovedOut(Finger finger2, List<Finger> list) {
        if (this.finger == finger2) {
            for (PointerEventListener pointerExited : this.pointerEventListeners) {
                pointerExited.pointerExited(finger2.getX(), finger2.getY());
            }
            this.finger = null;
        }
    }
}
