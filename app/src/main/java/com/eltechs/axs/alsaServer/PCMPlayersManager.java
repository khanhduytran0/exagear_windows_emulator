package com.eltechs.axs.alsaServer;

import com.eltechs.axs.alsaServer.impl.PCMPlayer;
import com.eltechs.axs.alsaServer.impl.PCMPlayersFactory;
import java.util.ArrayList;
import java.util.Collection;

public class PCMPlayersManager {
    private PCMPlayersFactory pcmPlayersFactory;
    private Collection<PCMPlayer> tracks = new ArrayList();

    public PCMPlayersManager(PCMPlayersFactory pCMPlayersFactory) {
        this.pcmPlayersFactory = pCMPlayersFactory;
    }

    public PCMPlayer createPCMPlayer(int i, int i2, ClientFormats clientFormats) {
        PCMPlayer create = this.pcmPlayersFactory.create(i, i2, clientFormats);
        this.tracks.add(create);
        return create;
    }

    public void deletePCMPlayer(PCMPlayer pCMPlayer) {
        this.tracks.remove(pCMPlayer);
        pCMPlayer.stopAndReleaseResources();
    }
}
