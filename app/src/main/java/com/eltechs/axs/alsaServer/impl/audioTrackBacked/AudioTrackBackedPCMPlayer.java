package com.eltechs.axs.alsaServer.impl.audioTrackBacked;

import android.media.AudioTrack;
import com.eltechs.axs.alsaServer.impl.PCMPlayer;

public class AudioTrackBackedPCMPlayer implements PCMPlayer {
    final AudioTrack audioTrack;
    private int framesWritten = 0;

    public AudioTrackBackedPCMPlayer(AudioTrack audioTrack2) {
        this.audioTrack = audioTrack2;
    }

    public void stopAndReleaseResources() {
        if (this.audioTrack.getPlayState() == 3) {
            this.audioTrack.pause();
        }
        if (this.audioTrack.getPlayState() == 2) {
            this.audioTrack.flush();
        }
        this.audioTrack.release();
    }

    public void startPlaying() {
        this.audioTrack.play();
    }

    public void pausePlaying() {
        this.audioTrack.pause();
    }

    public void drainBuffer() {
        this.audioTrack.stop();
    }

    public void flushBuffer() {
        this.audioTrack.flush();
        this.framesWritten = 0;
    }

    public int getPlayingPosition() {
        return this.framesWritten;
    }

    public void writeData(byte[] bArr, int i, int i2) {
        this.audioTrack.write(bArr, i, i2);
        this.framesWritten += i2 / this.audioTrack.getChannelCount();
    }

    public void writeData(short[] sArr, int i, int i2) {
        this.audioTrack.write(sArr, i, i2);
        this.framesWritten += i2 / this.audioTrack.getChannelCount();
    }
}
