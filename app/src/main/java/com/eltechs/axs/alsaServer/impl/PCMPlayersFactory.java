package com.eltechs.axs.alsaServer.impl;

import com.eltechs.axs.alsaServer.ClientFormats;

public interface PCMPlayersFactory {
    PCMPlayer create(int i, int i2, ClientFormats clientFormats);
}
