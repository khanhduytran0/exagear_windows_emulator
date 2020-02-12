package com.eltechs.axs.GuestAppActionAdapters;

public interface MouseMoveAdapter {
    public static final MouseMoveAdapter dummy = new MouseMoveAdapter() {
        public void moveTo(float f, float f2) {
        }

        public void prepareMoving(float f, float f2) {
        }
    };

    void moveTo(float f, float f2);

    void prepareMoving(float f, float f2);
}
