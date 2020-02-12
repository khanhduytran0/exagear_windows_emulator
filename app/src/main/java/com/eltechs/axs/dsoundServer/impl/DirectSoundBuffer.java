package com.eltechs.axs.dsoundServer.impl;

import com.eltechs.axs.dsoundServer.DirectSoundGlobalNotifier;
import com.eltechs.axs.xserver.impl.masks.Mask;

public interface DirectSoundBuffer {
    void destroy();

    void play(Mask<PlayFlags> mask);

    void recalcVolpan(int i, int i2);

    void resumePlayback();

    boolean setCurrentPosition(int i);

    void setEventCallback(DirectSoundGlobalNotifier directSoundGlobalNotifier);

    void setNotificationPositions(int[] iArr, int[] iArr2);

    void stop();

    void suspendPlayback();
}
