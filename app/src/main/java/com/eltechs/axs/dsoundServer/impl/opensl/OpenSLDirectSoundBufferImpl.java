package com.eltechs.axs.dsoundServer.impl.opensl;

import com.eltechs.axs.dsoundServer.DirectSoundGlobalNotifier;
import com.eltechs.axs.dsoundServer.impl.DirectSoundBuffer;
import com.eltechs.axs.dsoundServer.impl.PlayFlags;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.io.IOException;
import java.nio.MappedByteBuffer;

public class OpenSLDirectSoundBufferImpl implements DirectSoundBuffer {
    private final long player;

    private static native long createPlayer(long j, MappedByteBuffer mappedByteBuffer);

    private static native void destroyPlayer(long j);

    private static native void playImpl(long j, boolean z);

    private static native void recalcVolpanImpl(long j, int i, int i2);

    private static native void resumePlaybackImpl(long j);

    private static native boolean setCurrentPositionImpl(long j, int i);

    private static native void setEventCallbackImpl(long j, DirectSoundGlobalNotifier directSoundGlobalNotifier);

    private static native void setNotificationPositionsImpl(long j, int[] iArr, int[] iArr2);

    private static native void stopImpl(long j);

    private static native void suspendPlaybackImpl(long j);

    static {
        System.loadLibrary("dsound-helpers");
    }

    public OpenSLDirectSoundBufferImpl(long j, MappedByteBuffer mappedByteBuffer) throws IOException {
        this.player = createPlayer(j, mappedByteBuffer);
        if (this.player == 0) {
            throw new IOException("Failed to create an OpenSL player.");
        }
    }

    public void play(Mask<PlayFlags> mask) {
        if (!mask.isSubsetOf(Mask.of(PlayFlags.LOOPING, PlayFlags.TERMINATEBY_DISTANCE, PlayFlags.TERMINATEBY_PRIORITY, PlayFlags.TERMINATEBY_TIME))) {
            Assert.notImplementedYet("The only supported flag of IDirectSoundBuffer8::Play() is DSBPLAY_LOOPING");
        }
        playImpl(this.player, mask.isSet(PlayFlags.LOOPING));
    }

    public void stop() {
        stopImpl(this.player);
    }

    public void recalcVolpan(int i, int i2) {
        recalcVolpanImpl(this.player, i, i2);
    }

    public boolean setCurrentPosition(int i) {
        return setCurrentPositionImpl(this.player, i);
    }

    public void setNotificationPositions(int[] iArr, int[] iArr2) {
        setNotificationPositionsImpl(this.player, iArr, iArr2);
    }

    public void suspendPlayback() {
        suspendPlaybackImpl(this.player);
    }

    public void resumePlayback() {
        resumePlaybackImpl(this.player);
    }

    public void destroy() {
        destroyPlayer(this.player);
    }

    public void setEventCallback(DirectSoundGlobalNotifier directSoundGlobalNotifier) {
        setEventCallbackImpl(this.player, directSoundGlobalNotifier);
    }
}
