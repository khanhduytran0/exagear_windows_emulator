package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;
import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.KeyEventReporter;
import java.util.EnumMap;
import java.util.Map;

public class ScrollAdapterControlArrow implements SyncScrollAdapter {
    private static final Map<DirectionX, Map<DirectionY, KeyCodesX>> directionToKeyCode = new EnumMap(DirectionX.class);
    private final KeyEventReporter ker;

    public void notifyStart() {
    }

    public void notifyStop() {
    }

    static {
        EnumMap enumMap = new EnumMap(DirectionY.class);
        enumMap.put(DirectionY.UP, KeyCodesX.KEY_HOME);
        enumMap.put(DirectionY.NONE, KeyCodesX.KEY_LEFT);
        enumMap.put(DirectionY.DOWN, KeyCodesX.KEY_END);
        directionToKeyCode.put(DirectionX.LEFT, enumMap);
        EnumMap enumMap2 = new EnumMap(DirectionY.class);
        enumMap2.put(DirectionY.UP, KeyCodesX.KEY_UP);
        enumMap2.put(DirectionY.NONE, KeyCodesX.KEY_NONE);
        enumMap2.put(DirectionY.DOWN, KeyCodesX.KEY_DOWN);
        directionToKeyCode.put(DirectionX.NONE, enumMap2);
        EnumMap enumMap3 = new EnumMap(DirectionY.class);
        enumMap3.put(DirectionY.UP, KeyCodesX.KEY_PRIOR);
        enumMap3.put(DirectionY.NONE, KeyCodesX.KEY_RIGHT);
        enumMap3.put(DirectionY.DOWN, KeyCodesX.KEY_NEXT);
        directionToKeyCode.put(DirectionX.RIGHT, enumMap3);
    }

    public ScrollAdapterControlArrow(KeyEventReporter keyEventReporter) {
        this.ker = keyEventReporter;
    }

    private void pressCtrl() {
        this.ker.reportKeysPress(KeyCodesX.KEY_CONTROL_RIGHT);
    }

    private void releaseCtrl() {
        this.ker.reportKeysRelease(KeyCodesX.KEY_CONTROL_RIGHT);
    }

    private void scrollImpl(KeyCodesX keyCodesX, int i) {
        if (keyCodesX != KeyCodesX.KEY_NONE) {
            pressCtrl();
            while (true) {
                int i2 = i - 1;
                if (i > 0) {
                    this.ker.reportKeys(keyCodesX);
                    i = i2;
                } else {
                    releaseCtrl();
                    return;
                }
            }
        }
    }

    public void scroll(DirectionX directionX, DirectionY directionY, int i) {
        scrollImpl((KeyCodesX) ((Map) directionToKeyCode.get(directionX)).get(directionY), i);
    }
}
