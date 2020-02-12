package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;
import com.eltechs.axs.PointerEventReporter;
import java.util.EnumMap;
import java.util.Map;

public class ScrollAdapterMouseWheel implements SyncScrollAdapter {
    private static final Map<DirectionY, Integer> directionToButtonCodeY = new EnumMap(DirectionY.class);
    private final PointerEventReporter per;

    public void notifyStart() {
    }

    public void notifyStop() {
    }

    static {
        directionToButtonCodeY.put(DirectionY.UP, Integer.valueOf(4));
        directionToButtonCodeY.put(DirectionY.NONE, Integer.valueOf(0));
        directionToButtonCodeY.put(DirectionY.DOWN, Integer.valueOf(5));
    }

    public ScrollAdapterMouseWheel(PointerEventReporter pointerEventReporter) {
        this.per = pointerEventReporter;
    }

    private void scrollImpl(int i, int i2) {
        if (i != 0) {
            while (true) {
                int i3 = i2 - 1;
                if (i2 > 0) {
                    this.per.buttonPressed(i);
                    this.per.buttonReleased(i);
                    i2 = i3;
                } else {
                    return;
                }
            }
        }
    }

    public void scroll(DirectionX directionX, DirectionY directionY, int i) {
        scrollImpl(((Integer) directionToButtonCodeY.get(directionY)).intValue(), i);
    }
}
