package com.eltechs.axs.dsoundServer;

import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XStreamLock;
import java.io.IOException;

public class DirectSoundGlobalNotifier {
    private static DirectSoundGlobalNotifier instance;
    private final DirectSoundClient client;
    private final XOutputStream outputStream;

    public static DirectSoundGlobalNotifier getInstance() {
        return instance;
    }

    public static void createInstance(DirectSoundClient directSoundClient, XOutputStream xOutputStream) {
        instance = new DirectSoundGlobalNotifier(directSoundClient, xOutputStream);
    }

    public static void handleClientDestroyed(DirectSoundClient directSoundClient) {
        if (instance != null && instance.client == directSoundClient) {
            instance = null;
        }
    }

    public DirectSoundGlobalNotifier(DirectSoundClient directSoundClient, XOutputStream xOutputStream) {
        this.client = directSoundClient;
        this.outputStream = xOutputStream;
    }

    public void notifyPositionReached(int i) throws IOException {
        // Throwable th;
        XStreamLock lock = this.outputStream.lock();
        try {
            this.outputStream.writeInt(1);
            this.outputStream.writeInt(i);
            this.outputStream.flush();
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
    }
}
