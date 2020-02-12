package com.eltechs.axs.alsaServer;

import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.xconnectors.RequestHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XStreamLock;
import java.io.IOException;

public class ALSARequestHandler implements RequestHandler<ALSAClient> {
    public static final int HEADER_SIZE = 8;

    public ProcessingResult handleRequest(ALSAClient aLSAClient, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        Throwable th;
        if (xInputStream.getAvailableBytesCount() < 8) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        int i = xInputStream.getInt();
        int i2 = xInputStream.getInt();
        if (xInputStream.getAvailableBytesCount() < i2) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        switch (i) {
            case 0:
                aLSAClient.reset();
                break;
            case 1:
                aLSAClient.reset();
                boolean z = false;
                boolean z2 = aLSAClient.setChannels(xInputStream.getInt()) && (aLSAClient.setFormat(xInputStream.getInt()));
                if (aLSAClient.setRate(xInputStream.getInt()) && z2) {
                    z = true;
                }
                if (!z) {
                    return ProcessingResult.PROCESSED_KILL_CONNECTION;
                }
                break;
            case 2:
                aLSAClient.start();
                break;
            case 3:
                aLSAClient.writeDataToTrack(xInputStream.getAsByteBuffer(i2), i2);
                break;
            case 4:
                aLSAClient.stop();
                break;
            case 5:
                XStreamLock lock = xOutputStream.lock();
                try {
                    xOutputStream.writeInt(aLSAClient.pointer());
                    if (lock != null) {
                        lock.close();
                        break;
                    }
                } catch (Throwable th2) {
                    // th.addSuppressed(th2);
					th2.printStackTrace();
                    break;
                }
                break;
            case 6:
                aLSAClient.drain();
                break;
        }
        return ProcessingResult.PROCESSED;
        // throw th;
    }
}
