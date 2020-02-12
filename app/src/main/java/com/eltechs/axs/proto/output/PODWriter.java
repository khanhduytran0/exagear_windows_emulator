package com.eltechs.axs.proto.output;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.impl.ProtoHelpers;
import com.eltechs.axs.proto.output.PODVisitor.Callback;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PODWriter {

    private static final class BufferWriter implements Callback {
        private final ByteBuffer buffer;

        BufferWriter(ByteBuffer byteBuffer) {
            this.buffer = byteBuffer;
        }

        public void apply(Object obj) throws IOException {
            if (obj instanceof Byte) {
                this.buffer.put(((Byte) obj).byteValue());
            } else if (obj instanceof Boolean) {
                this.buffer.put(obj.equals(Boolean.TRUE) ? (byte) 1 : 0);
            } else if (obj instanceof Short) {
                this.buffer.putShort((short)((Short) obj).shortValue());
            } else if (obj instanceof Integer) {
                this.buffer.putInt(((Integer) obj).intValue());
            } else {
                Assert.isTrue(false, String.format("Unsupported POD member type %s.", new Object[]{obj.getClass()}));
            }
        }
    }

    private static final class LengthFinder implements Callback {
        /* access modifiers changed from: private */
        public int size;

        private LengthFinder() {
        }

        public void apply(Object obj) throws IOException {
            if (obj instanceof Byte) {
                this.size++;
            } else if (obj instanceof Boolean) {
                this.size++;
            } else if (obj instanceof Short) {
                this.size += 2;
            } else if (obj instanceof Integer) {
                this.size += 4;
            } else if (obj instanceof String) {
                this.size += ProtoHelpers.roundUpLength4(((String) obj).length());
            } else {
                Assert.isTrue(false, String.format("Unsupported POD member type %s.", new Object[]{obj.getClass()}));
            }
        }
    }

    private static final class XStreamWriter implements Callback {
        private final XOutputStream outputStream;

        XStreamWriter(XOutputStream xOutputStream) {
            this.outputStream = xOutputStream;
        }

        public void apply(Object obj) throws IOException {
            if (obj instanceof Byte) {
                this.outputStream.writeByte((byte)((Byte) obj).byteValue());
            } else if (obj instanceof Boolean) {
                this.outputStream.writeByte((byte) (obj.equals(Boolean.TRUE) ? (byte) 1 : 0));
            } else if (obj instanceof Short) {
                this.outputStream.writeShort(((Short) obj).shortValue());
            } else if (obj instanceof Integer) {
                this.outputStream.writeInt(((Integer) obj).intValue());
            } else if (obj instanceof String) {
                this.outputStream.writeString8((String) obj);
            } else {
                Assert.isTrue(false, String.format("Unsupported POD member type %s.", new Object[]{obj.getClass()}));
            }
        }
    }

    private PODWriter() {
    }

    public static void write(XOutputStream xOutputStream, Object obj) throws IOException {
        PODVisitor.visit(obj, new XStreamWriter(xOutputStream));
    }

    public static void write(ByteBuffer byteBuffer, Object obj) {
        try {
            PODVisitor.visit(obj, new BufferWriter(byteBuffer));
        } catch (IOException unused) {
        }
    }

    public static int getOnWireLength(Object obj) {
        try {
            LengthFinder lengthFinder = new LengthFinder();
            PODVisitor.visit(obj, lengthFinder);
            return lengthFinder.size;
        } catch (IOException unused) {
            Assert.state(false, "IOException can't be thrown by LengthFinder.");
            return -1;
        }
    }
}
