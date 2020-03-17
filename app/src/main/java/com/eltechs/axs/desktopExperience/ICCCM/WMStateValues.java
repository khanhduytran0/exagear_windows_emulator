package com.eltechs.axs.desktopExperience.ICCCM;

public enum WMStateValues {
    WITHDRAWN(0),
    NORMAL(1),
    ICONIC(3);
    
    private final int id;

    private WMStateValues(int i) {
        this.id = i;
    }

    public int value() {
        return this.id;
    }
}
