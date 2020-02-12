package com.eltechs.axs;

import java.util.List;

public interface TouchEventAdapter {
    void notifyMoved(Finger finger, List<Finger> list);

    void notifyMovedIn(Finger finger, List<Finger> list);

    void notifyMovedOut(Finger finger, List<Finger> list);

    void notifyReleased(Finger finger, List<Finger> list);

    void notifyTouched(Finger finger, List<Finger> list);
}
