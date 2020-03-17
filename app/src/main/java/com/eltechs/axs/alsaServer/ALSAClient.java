package com.eltechs.axs.alsaServer;

import com.eltechs.axs.alsaServer.impl.PCMPlayer;
import com.eltechs.axs.helpers.ReluctantlyGarbageCollectedArrays;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ALSAClient {
    public static final int MAX_CHANNELS = 2;
    public static final int MAX_RATE = 48000;
    public static final int MIN_CHANNELS = 1;
    public static final int MIN_RATE = 8000;
    private final ReluctantlyGarbageCollectedArrays arrays = new ReluctantlyGarbageCollectedArrays();
    private int channels = 1;
    private ClientFormats format = ClientFormats.U8;
    private PCMPlayer pcmPlayer = null;
    private final PCMPlayersManager pcmPlayersManager;
    private int rate = MIN_RATE;

    public ALSAClient(PCMPlayersManager pCMPlayersManager) {
        this.pcmPlayersManager = pCMPlayersManager;
    }

    public void reset() {
        if (this.pcmPlayer != null) {
            this.pcmPlayersManager.deletePCMPlayer(this.pcmPlayer);
            this.pcmPlayer = null;
        }
    }

    private void prepareAudioTrack() {
        if (this.pcmPlayer == null) {
            this.pcmPlayer = this.pcmPlayersManager.createPCMPlayer(this.rate, this.channels, this.format);
        }
    }

    public void start() {
        prepareAudioTrack();
        this.pcmPlayer.startPlaying();
    }

    public void stop() {
        if (this.pcmPlayer != null) {
            this.pcmPlayer.pausePlaying();
            this.pcmPlayer.flushBuffer();
        }
    }

    public void drain() {
        if (this.pcmPlayer != null) {
            this.pcmPlayer.drainBuffer();
        }
    }

    public void writeDataToTrack(ByteBuffer byteBuffer, int i) {
        prepareAudioTrack();
        if (this.pcmPlayer == null) {
            return;
        }
        if (this.format == ClientFormats.U8) {
            byte[] byteArray = this.arrays.getByteArray(i);
            byteBuffer.get(byteArray, 0, i);
            this.pcmPlayer.writeData(byteArray, 0, i);
            return;
        }
        if (this.format == ClientFormats.S16BE) {
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        } else {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        int i2 = i / 2;
        short[] shortArray = this.arrays.getShortArray(i2);
        byteBuffer.asShortBuffer().get(shortArray, 0, i2);
        this.pcmPlayer.writeData(shortArray, 0, i2);
    }

    public int pointer() {
        if (this.pcmPlayer != null) {
            return this.pcmPlayer.getPlayingPosition();
        }
        return 0;
    }

    public boolean setFormat(int i) {
        if (!ClientFormats.checkFormat(i)) {
            return false;
        }
        this.format = ClientFormats.values()[i];
        return true;
    }

    public boolean setChannels(int i) {
        if (i < 1 || i > 2) {
            return false;
        }
        this.channels = i;
        return true;
    }

    public boolean setRate(int i) {
        if (i < 8000 || i > 48000) {
            return false;
        }
        this.rate = i;
        return true;
    }
}
