package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;

public interface AsyncScrollAdapter {
    void notifyStart();

    void notifyStop();

    void setScrolling(DirectionX directionX, DirectionY directionY);
}
