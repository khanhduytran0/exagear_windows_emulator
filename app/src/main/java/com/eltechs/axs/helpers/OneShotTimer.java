package com.eltechs.axs.helpers;

import android.os.CountDownTimer;

public abstract class OneShotTimer extends CountDownTimer {
    public final void onTick(long j) {
    }

    public OneShotTimer(long j) {
        super(j, 10 * j);
    }
}
