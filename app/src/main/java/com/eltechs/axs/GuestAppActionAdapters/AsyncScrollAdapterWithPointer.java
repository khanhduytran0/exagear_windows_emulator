package com.eltechs.axs.GuestAppActionAdapters;

import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionX;
import com.eltechs.axs.GuestAppActionAdapters.ScrollDirections.DirectionY;
import com.eltechs.axs.geom.Point;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.xserver.ViewFacade;

public class AsyncScrollAdapterWithPointer implements AsyncScrollAdapter {
    private final ViewFacade facade;
    private final Rectangle rect;
    private int savedPosX = -1;
    private int savedPosY = -1;
    private boolean scrollingX = false;
    private boolean scrollingY = false;

    public void notifyStart() {
    }

    public void notifyStop() {
    }

    public AsyncScrollAdapterWithPointer(ViewFacade viewFacade, Rectangle rectangle) {
        this.facade = viewFacade;
        this.rect = rectangle;
    }

    public void setScrolling(DirectionX directionX, DirectionY directionY) {
        Point pointerLocation = this.facade.getPointerLocation();
        int i = pointerLocation.x;
        int i2 = pointerLocation.y;
        if (!this.scrollingX) {
            this.savedPosX = pointerLocation.x;
            this.scrollingX = true;
        }
        switch (directionX) {
            case LEFT:
                i = 0;
                break;
            case RIGHT:
                i = this.rect.width;
                break;
            case NONE:
                if (this.scrollingX) {
                    i = this.savedPosX;
                }
                this.scrollingX = false;
                break;
        }
        if (!this.scrollingY) {
            this.savedPosY = pointerLocation.y;
            this.scrollingY = true;
        }
        switch (directionY) {
            case UP:
                i2 = 0;
                break;
            case DOWN:
                i2 = this.rect.height;
                break;
            case NONE:
                if (this.scrollingY) {
                    i2 = this.savedPosY;
                }
                this.scrollingY = false;
                break;
        }
        this.facade.injectPointerMove(i, i2);
    }
}
