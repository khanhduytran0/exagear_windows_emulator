package com.eltechs.axs.dsoundServer.impl.opensl;

import com.eltechs.axs.dsoundServer.impl.DirectSoundBuffer;
import com.eltechs.axs.dsoundServer.impl.DirectSoundBufferFactory;
import com.eltechs.axs.sysvipc.AttachedSHMSegment;
import java.io.IOException;

public class OpenSLDirectSoundBufferFactoryImpl implements DirectSoundBufferFactory {
    private final long engine = createOpenSLEngine();

    private static native long createOpenSLEngine();

    private static native void destroyOpenSLEngine(long j);

    static {
        System.loadLibrary("dsound-helpers");
    }

    public OpenSLDirectSoundBufferFactoryImpl() throws IOException {
        if (this.engine == 0) {
            throw new IOException("Failed to initialise OpenSL.");
        }
    }

    public DirectSoundBuffer createBuffer(AttachedSHMSegment attachedSHMSegment) {
        try {
            return new OpenSLDirectSoundBufferImpl(this.engine, attachedSHMSegment.getContent());
        } catch (IOException unused) {
            return null;
        }
    }

    public void destroy() throws IOException {
        destroyOpenSLEngine(this.engine);
    }
}
