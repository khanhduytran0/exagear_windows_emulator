package com.eltechs.axs.dsoundServer;

import com.eltechs.axs.dsoundServer.impl.DirectSoundBuffer;
import com.eltechs.axs.dsoundServer.impl.DirectSoundBufferFactory;
import com.eltechs.axs.dsoundServer.impl.PlayFlags;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.sysvipc.AttachedSHMSegment;
import com.eltechs.axs.sysvipc.SHMEngine;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.nio.MappedByteBuffer;

public class DirectSoundClient {
    private static final int MAGIC = 1145983812;
    private static final int MIN_SIZE_OF_SHM_SEGMENT = 64;
    private static final int SIZE_OF_INT = 4;
    private final DirectSoundBufferFactory bufferFactory;
    private final SHMEngine shmEngine;
    private DirectSoundBuffer soundBuffer;
    private AttachedSHMSegment underlyingShmSegment;

    public DirectSoundClient(SHMEngine sHMEngine, DirectSoundBufferFactory directSoundBufferFactory) {
        this.shmEngine = sHMEngine;
        this.bufferFactory = directSoundBufferFactory;
    }

    public boolean attach(int i) {
        Assert.state(this.underlyingShmSegment == null, "Can't attach a DirectSoundClient twice.");
        AttachedSHMSegment attachSegment = this.shmEngine.attachSegment(i, true);
        if (attachSegment == null) {
            return false;
        }
        if (!checkShmSegmentFormat(attachSegment.getContent())) {
            this.shmEngine.detachSHMSegment(attachSegment);
            return false;
        }
        this.underlyingShmSegment = attachSegment;
        this.soundBuffer = this.bufferFactory.createBuffer(attachSegment);
        if (this.soundBuffer == null) {
            this.shmEngine.detachSHMSegment(attachSegment);
            this.underlyingShmSegment = null;
            return false;
        }
        this.soundBuffer.setEventCallback(DirectSoundGlobalNotifier.getInstance());
        return true;
    }

    private boolean checkShmSegmentFormat(MappedByteBuffer mappedByteBuffer) {
        if (mappedByteBuffer.limit() > 64 && mappedByteBuffer.getInt(0) == MAGIC) {
            return true;
        }
        return false;
    }

    public boolean isAttached() {
        return this.underlyingShmSegment != null;
    }

    public void play(Mask<PlayFlags> mask) {
        this.soundBuffer.play(mask);
    }

    public void stop() {
        this.soundBuffer.stop();
    }

    public void recalcVolpan(int i, int i2) {
        this.soundBuffer.recalcVolpan(i, i2);
    }

    public boolean setCurrentPosition(int i) {
        return this.soundBuffer.setCurrentPosition(i);
    }

    public void setNotificationPositions(int[] iArr, int[] iArr2) {
        this.soundBuffer.setNotificationPositions(iArr, iArr2);
    }

    public void suspendPlayback() {
        if (this.soundBuffer != null) {
            this.soundBuffer.suspendPlayback();
        }
    }

    public void resumePlayback() {
        if (this.soundBuffer != null) {
            this.soundBuffer.resumePlayback();
        }
    }

    public void destroy() {
        if (this.underlyingShmSegment != null) {
            this.soundBuffer.destroy();
            this.shmEngine.detachSHMSegment(this.underlyingShmSegment);
            this.soundBuffer = null;
            this.underlyingShmSegment = null;
        }
        DirectSoundGlobalNotifier.handleClientDestroyed(this);
    }
}
