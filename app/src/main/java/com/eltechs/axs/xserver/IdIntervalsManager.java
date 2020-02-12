package com.eltechs.axs.xserver;

public interface IdIntervalsManager {
    void freeInterval(IdInterval idInterval);

    IdInterval getInterval();
}
