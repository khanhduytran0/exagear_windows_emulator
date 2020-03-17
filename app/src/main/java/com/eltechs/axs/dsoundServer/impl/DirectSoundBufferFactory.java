package com.eltechs.axs.dsoundServer.impl;

import com.eltechs.axs.sysvipc.AttachedSHMSegment;

public interface DirectSoundBufferFactory {
    DirectSoundBuffer createBuffer(AttachedSHMSegment attachedSHMSegment);
}
