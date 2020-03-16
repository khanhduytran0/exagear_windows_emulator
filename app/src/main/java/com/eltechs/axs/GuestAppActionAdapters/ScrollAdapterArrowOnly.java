package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;
import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.KeyEventReporter;
import java.util.EnumMap;
import java.util.Map;

public class ScrollAdapterArrowOnly implements SyncScrollAdapter {
    private static final Map<DirectionX, KeyCodesX> directionToKeyCodeX = new EnumMap(DirectionX.class);
    private static final Map<DirectionY, KeyCodesX> directionToKeyCodeY = new EnumMap(DirectionY.class);
    private final KeyEventReporter ker;

    public void notifyStart() {
    }

    public void notifyStop() {
    }

    static {
        directionToKeyCodeX.put(DirectionX.LEFT, KeyCodesX.KEY_LEFT);
        directionToKeyCodeX.put(DirectionX.NONE, KeyCodesX.KEY_NONE);
        directionToKeyCodeX.put(DirectionX.RIGHT, KeyCodesX.KEY_RIGHT);
        directionToKeyCodeY.put(DirectionY.UP, KeyCodesX.KEY_UP);
        directionToKeyCodeY.put(DirectionY.NONE, KeyCodesX.KEY_NONE);
        directionToKeyCodeY.put(DirectionY.DOWN, KeyCodesX.KEY_DOWN);
    }

    public ScrollAdapterArrowOnly(KeyEventReporter keyEventReporter) {
        this.ker = keyEventReporter;
    }

    private void scrollImpl(KeyCodesX keyCodesX, int i) {
        if (keyCodesX != KeyCodesX.KEY_NONE) {
            while (true) {
                int i2 = i - 1;
                if (i > 0) {
                    this.ker.reportKeyPressReleaseNoDelay(keyCodesX);
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    public void scroll(DirectionX directionX, DirectionY directionY, int i) {
        scrollImpl((KeyCodesX) directionToKeyCodeX.get(directionX), i);
        scrollImpl((KeyCodesX) directionToKeyCodeY.get(directionY), i);
    }
}
