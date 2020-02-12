package com.eltechs.axs.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class IOStreamHelpers {
    private static final int COPY_BUFFER_LENGTH = 32768;

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[32768];
        while (true) {
            int read = inputStream.read(bArr);
            if (read <= 0) {
                outputStream.flush();
                return;
            }
            outputStream.write(bArr, 0, read);
        }
    }
}
