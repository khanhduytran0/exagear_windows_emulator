package com.eltechs.axs.sysvipc;

import com.eltechs.axs.helpers.Assert;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;

public class SHMEngineImpl implements SHMEngine {
    private static final int SIZE_OF_INT32 = 4;
    private static final int SIZE_OF_INT64 = 8;
    private final Communicator communicator;

    private static native boolean initialiseNativeParts();

    private native MappedByteBuffer mapAshmemSegment(FileDescriptor fileDescriptor, long j, boolean z);

    private native void unmapAshmemSegment(MappedByteBuffer mappedByteBuffer, long j);

    static {
        System.loadLibrary("ipc-helpers");
        Assert.state(initialiseNativeParts(), "Managed and native parts of SHMEngineImpl do not match one another.");
    }

    public SHMEngineImpl(String str) throws IOException {
        this.communicator = new Communicator(str);
    }

    public void stop() throws IOException {
        this.communicator.close();
    }

    public AttachedSHMSegment attachSegment(int i, boolean z) {
        try {
            byte[] bArr = new byte[4];
            byte[] bArr2 = new byte[12];
            FileDescriptor[] fileDescriptorArr = new FileDescriptor[1];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            wrap.order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer wrap2 = ByteBuffer.wrap(bArr2);
            wrap2.order(ByteOrder.LITTLE_ENDIAN);
            wrap.putInt(i);
            this.communicator.communicate(RequestCodes.SHM_GET_SIZE_AND_FD, bArr, bArr2, fileDescriptorArr);
            long j = wrap2.getLong();
            if (wrap2.getInt() != 0) {
                return null;
            }
            MappedByteBuffer mapAshmemSegment = mapAshmemSegment(fileDescriptorArr[0], j, z);
            if (mapAshmemSegment == null) {
                return null;
            }
            mapAshmemSegment.order(ByteOrder.nativeOrder());
            return new AttachedSHMSegmentImpl(mapAshmemSegment, j);
        } catch (IOException unused) {
            return null;
        }
    }

    public void detachSHMSegment(AttachedSHMSegment attachedSHMSegment) {
        AttachedSHMSegmentImpl attachedSHMSegmentImpl = (AttachedSHMSegmentImpl) attachedSHMSegment;
        Assert.notNull(attachedSHMSegmentImpl.getContent(), "Segment already detached");
        unmapAshmemSegment(attachedSHMSegmentImpl.content, attachedSHMSegmentImpl.size);
        attachedSHMSegmentImpl.content = null;
        attachedSHMSegmentImpl.size = -1;
    }
}
