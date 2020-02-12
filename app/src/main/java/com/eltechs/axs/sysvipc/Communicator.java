package com.eltechs.axs.sysvipc;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import com.eltechs.axs.helpers.Assert;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Communicator {
    private static final int MAGIC = 1448302931;
    private static final int SIZE_OF_INT16 = 2;
    private static final int SIZE_OF_INT32 = 4;
    private static final int SIZE_OF_INT64 = 8;
    private final LocalSocket emulatorConnection = new LocalSocket();

    Communicator(String str) throws IOException {
        this.emulatorConnection.connect(new LocalSocketAddress(str, Namespace.ABSTRACT));
    }

    public void close() throws IOException {
        this.emulatorConnection.close();
    }

    public void communicate(RequestCodes requestCodes, byte[] bArr, byte[] bArr2, FileDescriptor[] fileDescriptorArr) throws IOException {
        boolean z = true;
        if (!(fileDescriptorArr == null || fileDescriptorArr.length == 1)) {
            z = false;
        }
        Assert.isTrue(z);
        byte[] bArr3 = new byte[(bArr.length + 8)];
        byte[] bArr4 = new byte[(8 + bArr2.length)];
        ByteBuffer wrap = ByteBuffer.wrap(bArr3);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer wrap2 = ByteBuffer.wrap(bArr4);
        wrap2.order(ByteOrder.LITTLE_ENDIAN);
        wrap.putInt(MAGIC);
        wrap.putShort((short)(short) bArr3.length);
        wrap.putShort((short)(short) requestCodes.getCode());
        wrap.put(bArr);
        synchronized (this.emulatorConnection) {
            this.emulatorConnection.getOutputStream().write(bArr3);
            if (this.emulatorConnection.getInputStream().read(bArr4) != bArr4.length) {
                throw new IOException("Response of the Sys V IPC emulation server is too short.");
            }
            FileDescriptor[] ancillaryFileDescriptors = this.emulatorConnection.getAncillaryFileDescriptors();
            if (MAGIC == wrap2.getInt()) {
                if (bArr4.length == wrap2.getShort()) {
                    wrap2.getShort();
                    if (ancillaryFileDescriptors != null || fileDescriptorArr == null) {
                        wrap2.get(bArr2);
                        if (ancillaryFileDescriptors != null) {
                            fileDescriptorArr[0] = ancillaryFileDescriptors[0];
                        }
                    } else {
                        throw new IOException("The Sys V IPC emulation server has responded with no file descriptors when one is expected.");
                    }
                }
            }
            throw new IOException("The Sys V IPC emulation server has sent a malformed response.");
        }
    }

    public void communicate(RequestCodes requestCodes, byte[] bArr, byte[] bArr2) throws IOException {
        communicate(requestCodes, bArr, bArr2, null);
    }
}
