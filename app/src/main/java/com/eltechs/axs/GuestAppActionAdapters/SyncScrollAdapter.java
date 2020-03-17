package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;

public interface SyncScrollAdapter {
    void notifyStart();

    void notifyStop();

    void scroll(DirectionX directionX, DirectionY directionY, int i);
}
