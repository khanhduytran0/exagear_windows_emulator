package com.eltechs.axs.xconnectors;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.impl.ProtoHelpers;
import com.eltechs.axs.proto.output.PODWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

public class XResponse {
    private static final byte RESP_CODE_ERROR = 0;
    private static final byte RESP_CODE_SUCCESS = 1;
    private static final int SIMPLE_EVENT_LENGTH = 28;
    private static final int SIMPLE_REPLY_LENGTH = 24;
    /* access modifiers changed from: private */
    public static final byte[] zero = new byte[32];
    private final XRequest inResponseTo;
    private final XOutputStream outputStream;
    private final int requestSequenceNumber;

    public interface ResponseDataWriter extends BufferFiller {
    }

    public XResponse(XRequest xRequest, XOutputStream xOutputStream) {
        this.inResponseTo = xRequest;
        this.requestSequenceNumber = xRequest.getSequenceNumber();
        this.outputStream = xOutputStream;
    }

    public XResponse(int i, XOutputStream xOutputStream) {
        this.inResponseTo = null;
        this.requestSequenceNumber = i;
        this.outputStream = xOutputStream;
    }

    public void sendSimpleSuccessReply(byte b, final ResponseDataWriter responseDataWriter) throws IOException {
        Throwable th;
        XStreamLock lock = this.outputStream.lock();
        try {
            this.outputStream.writeByte((byte)1);
            this.outputStream.writeByte((byte)b);
            this.outputStream.writeShort((short) this.requestSequenceNumber);
            this.outputStream.writeInt(0);
            this.outputStream.write(24, new BufferFiller() {
                public void write(ByteBuffer byteBuffer) {
                    responseDataWriter.write(byteBuffer);
                    byteBuffer.put(XResponse.zero, 0, byteBuffer.remaining());
                }
            });
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            // th.addSuppressed(th2);
        	throw new RuntimeException(th2);
		}
    }

    public void sendSuccessReplyWithPayload(byte b, final ResponseDataWriter responseDataWriter, int i, ResponseDataWriter responseDataWriter2) throws IOException {
        Throwable th;
        int roundUpLength4 = ProtoHelpers.roundUpLength4(i);
        XStreamLock lock = this.outputStream.lock();
        try {
            this.outputStream.writeByte((byte)1);
            this.outputStream.writeByte((byte)b);
            this.outputStream.writeShort((short) this.requestSequenceNumber);
            this.outputStream.writeInt(ProtoHelpers.calculateLengthInWords(roundUpLength4));
            this.outputStream.write(24, new BufferFiller() {
                public void write(ByteBuffer byteBuffer) {
                    if (responseDataWriter != null) {
                        responseDataWriter.write(byteBuffer);
                    }
                    byteBuffer.put(XResponse.zero, 0, byteBuffer.remaining());
                }
            });
            this.outputStream.write(i, responseDataWriter2);
            int calculatePad = ProtoHelpers.calculatePad(i);
            if (calculatePad != 0) {
                this.outputStream.write(zero, 0, calculatePad);
            }
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            // th.addSuppressed(th2);
        	throw new RuntimeException(th2);
		}
    }

    public void sendEvent(byte b, byte b2, final ResponseDataWriter responseDataWriter) throws IOException {
        Throwable th;
        boolean z = false;
        Assert.isTrue(b != 1, "Event codes must be other than RESP_CODE_SUCCESS and RESP_CODE_ERROR.");
        if (b != 0) {
            z = true;
        }
        Assert.isTrue(z, "Event codes must be other than RESP_CODE_SUCCESS and RESP_CODE_ERROR.");
        XStreamLock lock = this.outputStream.lock();
        try {
            this.outputStream.writeByte((byte)b);
            this.outputStream.writeByte((byte)b2);
            this.outputStream.writeShort((short) this.requestSequenceNumber);
            this.outputStream.write(28, new BufferFiller() {
                public void write(ByteBuffer byteBuffer) {
                    responseDataWriter.write(byteBuffer);
                    byteBuffer.put(
					XResponse.zero, 0, byteBuffer.remaining());
                }
            });
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            // th.addSuppressed(th2);
        	throw new RuntimeException(th2);
		}
    }

    private void sendReply(boolean z, byte b, byte b2, int i, Object... objArr) throws IOException {
        Throwable th;
        int i2 = 24;
        if (z) {
            int onWireLength = PODWriter.getOnWireLength(objArr);
            if (onWireLength <= 24) {
                i2 = 24 - onWireLength;
            } else {
                int i3 = onWireLength - 24;
                i2 = ProtoHelpers.calculatePad(i3);
                i = ProtoHelpers.roundUpLength4(i3) / 4;
            }
        }
        XStreamLock lock = this.outputStream.lock();
        try {
            this.outputStream.writeByte((byte)b);
            this.outputStream.writeByte((byte)b2);
            this.outputStream.writeShort((short) this.requestSequenceNumber);
            this.outputStream.writeInt(i);
            if (z) {
                PODWriter.write(this.outputStream, (Object) objArr);
                this.outputStream.write(zero, 0, i2);
            } else {
                this.outputStream.write(zero, 0, i2);
                PODWriter.write(this.outputStream, (Object) objArr);
            }
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            // th.addSuppressed(th2);
        	throw new RuntimeException(th2);
		}
    }

    public void sendSimpleSuccessReply(byte b, Object... objArr) throws IOException {
        sendReply(true, (byte) 1, b, 0, objArr);
    }

    public void sendSuccessReply(byte b, Object... objArr) throws IOException {
        sendReply(false, (byte) 1, b, ProtoHelpers.calculateLengthInWords(PODWriter.getOnWireLength(objArr)), objArr);
    }

    public void sendError(XProtocolError xProtocolError) throws IOException {
        Throwable th;
        XStreamLock lock = this.outputStream.lock();
        try {
            this.outputStream.write(new byte[]{0, xProtocolError.getErrorCode()});
            this.outputStream.writeShort((short) this.requestSequenceNumber);
            this.outputStream.writeInt(xProtocolError.getData());
            this.outputStream.writeShort(this.inResponseTo.getMinorOpcode());
            this.outputStream.write(new byte[]{this.inResponseTo.getMajorOpcode()});
            this.outputStream.write(zero, 0, 21);
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            // th.addSuppressed(th2);
        	throw new RuntimeException(th2);
		}
    }
}
