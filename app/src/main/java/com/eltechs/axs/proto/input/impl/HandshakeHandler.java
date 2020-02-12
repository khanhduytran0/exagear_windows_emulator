package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.proto.output.PODWriter;
import com.eltechs.axs.proto.output.replies.AuthDenial;
import com.eltechs.axs.proto.output.replies.ServerInfo;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XStreamLock;
import com.eltechs.axs.xserver.IdInterval;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;
import java.nio.ByteOrder;

public class HandshakeHandler {
    private static final byte LSB_MARKER = 108;
    private static final int MINIMAL_AUTH_REQUEST_LENGTH = 12;
    private static final byte MSB_MARKER = 66;
    private final XServer target;

    public HandshakeHandler(XServer xServer) {
        this.target = xServer;
    }

    public ProcessingResult handleAuthRequest(XClient xClient, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        if (xInputStream.getAvailableBytesCount() < 12) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        byte b = xInputStream.getByte();
        if (b == 66) {
            xInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
            xOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        } else if (b != 108) {
            return denyAuthentication(xOutputStream, "Byte order marker is invalid.");
        } else {
            xInputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            xOutputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        }
        xInputStream.getByte();
        short s = xInputStream.getShort();
        xInputStream.getShort();
        if (s != 11) {
            return denyAuthentication(xOutputStream, "Unsupported major X protocol version");
        }
        if (xClient.getIdInterval() == null) {
            return denyAuthentication(xOutputStream, "Too many connections.");
        }
        short s2 = xInputStream.getShort();
        short s3 = xInputStream.getShort();
        xInputStream.getShort();
        int roundUpLength4 = ProtoHelpers.roundUpLength4(s2) + ProtoHelpers.roundUpLength4(s3);
        if (xInputStream.getAvailableBytesCount() < roundUpLength4) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        xInputStream.get(new byte[roundUpLength4]);
        sendServerInformation(xOutputStream, xClient.getIdInterval());
        xClient.setAuthenticated(true);
        return ProcessingResult.PROCESSED;
    }

    private void sendServerInformation(XOutputStream xOutputStream, IdInterval idInterval) throws IOException {
        Throwable th;
        Throwable th2;
        XLock lock = this.target.getLocksManager().lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.DRAWABLES_MANAGER});
        try {
            XStreamLock lock2 = xOutputStream.lock();
            try {
                PODWriter.write(xOutputStream, (Object) new ServerInfo(this.target, idInterval));
                if (lock2 != null) {
                    lock2.close();
                }
                if (lock != null) {
                    lock.close();
                    return;
                }
                return;
            } catch (Throwable th3) {
                th2 = th3;
            }
            // throw th;
            if (lock2 != null) {
                if (th2 != null) {
                    lock2.close();
                } else {
                    lock2.close();
                }
            }
            throw th2;
            // throw th2;
        } catch (Throwable th5) {
            Throwable th6 = th5;
            try {
                throw th6;
            } catch (Throwable th7) {
                th6.addSuppressed(th7);
            }
        }
    }

    private ProcessingResult denyAuthentication(XOutputStream xOutputStream, String str) throws IOException {
        Throwable th;
        XStreamLock lock = xOutputStream.lock();
        try {
            PODWriter.write(xOutputStream, (Object) new AuthDenial(str));
            if (lock != null) {
                lock.close();
            }
            return ProcessingResult.PROCESSED_KILL_CONNECTION;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
        
    }
}
