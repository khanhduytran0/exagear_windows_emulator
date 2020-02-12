package com.eltechs.axs.alsaServer.impl.audioTrackBacked;

import android.media.AudioTrack;
import com.eltechs.axs.alsaServer.ClientFormats;
import com.eltechs.axs.alsaServer.impl.PCMPlayer;
import com.eltechs.axs.alsaServer.impl.PCMPlayersFactory;
import com.eltechs.axs.helpers.Assert;

public class AudioTrackBackedPCMPlayersFactory implements PCMPlayersFactory {
    public PCMPlayer create(int i, int i2, ClientFormats clientFormats) {
        boolean z = true;
        int i3 = i2 == 1 ? 4 : 12;
        int i4 = clientFormats == ClientFormats.U8 ? 3 : 2;
        AudioTrack audioTrack = new AudioTrack(3, i, i3, i4, AudioTrack.getMinBufferSize(i, i3, i4), 1);
        if (audioTrack.getState() != 1) {
            z = false;
        }
        Assert.state(z);
        return new AudioTrackBackedPCMPlayer(audioTrack);
    }
}
