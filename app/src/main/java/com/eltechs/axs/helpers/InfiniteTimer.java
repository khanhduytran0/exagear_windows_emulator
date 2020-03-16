package com.eltechs.axs.helpers;

import android.os.CountDownTimer;

public abstract class InfiniteTimer extends CountDownTimer {
    private static long veryLongPeriod = 86400000;

    public InfiniteTimer(long j) {
        super(veryLongPeriod, j);
    }

    public final void onFinish() {
        start();
    }
}
