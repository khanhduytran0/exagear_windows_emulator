package com.eltechs.axs.dsoundServer;

import com.eltechs.axs.dsoundServer.impl.PlayFlags;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.xconnectors.RequestHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XStreamLock;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

public class DirectSoundRequestHandler implements RequestHandler<DirectSoundClient> {
    private static final int HEADER_SIZE = 8;
    private static final int SIZE_OF_ATTACH_REQ = 4;
    private static final int SIZE_OF_INIT_NOTIFY_REQ = 0;
    private static final int SIZE_OF_INT = 4;
    private static final int SIZE_OF_PLAY_REQ = 4;
    private static final int SIZE_OF_RECALC_VOLPAN_REQ = 8;
    private static final int SIZE_OF_SET_CURRENT_POSITION_REQ = 4;
    private static final int SIZE_OF_STOP_REQ = 0;
    private final ReentrantReadWriteLock suspendLock = new ReentrantReadWriteLock(true);

    public void suspendRequestProcessing() {
        Assert.state(!this.suspendLock.isWriteLocked(), "suspendRequestProcessing() must not be called recursively.");
        this.suspendLock.writeLock().lock();
    }

    public void resumeRequestProcessing() {
        if (this.suspendLock.isWriteLocked()) {
            Assert.state(this.suspendLock.isWriteLockedByCurrentThread(), "resumeRequestProcessing() must be called by the thread that called suspendRequestProcessing()");
            this.suspendLock.writeLock().unlock();
        }
    }

    public ProcessingResult handleRequest(DirectSoundClient directSoundClient, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        ReadLock readLock = this.suspendLock.readLock();
        try {
            readLock.lock();
            return handleRequestImpl(directSoundClient, xInputStream, xOutputStream);
        } finally {
            readLock.unlock();
        }
    }

    public ProcessingResult handleRequestImpl(DirectSoundClient directSoundClient, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        if (xInputStream.getAvailableBytesCount() < 8) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        int i = xInputStream.getInt();
        int i2 = xInputStream.getInt();
        if (xInputStream.getAvailableBytesCount() < i2) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        if (i == 255) {
            if (i2 != 0) {
                return ProcessingResult.PROCESSED_KILL_CONNECTION;
            }
            return initGlobalNotifier(directSoundClient, xOutputStream);
        } else if (i == 0) {
            if (i2 != 4) {
                return ProcessingResult.PROCESSED_KILL_CONNECTION;
            }
            return attach(directSoundClient, xInputStream.getInt(), xOutputStream);
        } else if (!directSoundClient.isAttached()) {
            return ProcessingResult.PROCESSED_KILL_CONNECTION;
        } else {
            switch (i) {
                case 1:
                    if (i2 != 4) {
                        return ProcessingResult.PROCESSED_KILL_CONNECTION;
                    }
                    return play(directSoundClient, xInputStream.getInt(), xOutputStream);
                case 2:
                    if (i2 != 0) {
                        return ProcessingResult.PROCESSED_KILL_CONNECTION;
                    }
                    return stop(directSoundClient, xOutputStream);
                case 3:
                    if (i2 != 4) {
                        return ProcessingResult.PROCESSED_KILL_CONNECTION;
                    }
                    return setCurrentPosition(directSoundClient, xInputStream.getInt(), xOutputStream);
                case 4:
                    int i3 = xInputStream.getInt();
                    if (i2 != (i3 * 4 * 2) + 4) {
                        return ProcessingResult.PROCESSED_KILL_CONNECTION;
                    }
                    return setNotificationPositions(directSoundClient, i3, xInputStream, xOutputStream);
                case 5:
                    int i4 = xInputStream.getInt();
                    int i5 = xInputStream.getInt();
                    if (i2 != 8) {
                        return ProcessingResult.PROCESSED_KILL_CONNECTION;
                    }
                    return recalcVolpan(directSoundClient, i4, i5, xOutputStream);
                default:
                    return ProcessingResult.PROCESSED_KILL_CONNECTION;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0027, code lost:
        if (r2 != null) goto L_0x0029;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0029, code lost:
        if (r3 != null) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002f, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0030, code lost:
        r3.addSuppressed(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0034, code lost:
        r2.close();
     */
    private ProcessingResult attach(DirectSoundClient directSoundClient, int i, XOutputStream xOutputStream) throws IOException {
        if (directSoundClient.isAttached()) {
            return ProcessingResult.PROCESSED_KILL_CONNECTION;
        }
        if (!directSoundClient.attach(i)) {
            return ProcessingResult.PROCESSED_KILL_CONNECTION;
        }
        XStreamLock lock = xOutputStream.lock();
        xOutputStream.writeInt(0);
        if (lock != null) {
            lock.close();
        }
        return ProcessingResult.PROCESSED;
        // throw th;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
        if (r2 != null) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
        if (r3 != null) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002b, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002c, code lost:
        r3.addSuppressed(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0030, code lost:
        r2.close();
     */
    private ProcessingResult play(DirectSoundClient directSoundClient, int i, XOutputStream xOutputStream) throws IOException {
        Mask create = Mask.create(PlayFlags.class, i);
        if (create == null) {
            return ProcessingResult.PROCESSED_KILL_CONNECTION;
        }
        directSoundClient.play(create);
        XStreamLock lock = xOutputStream.lock();
        xOutputStream.writeInt(0);
        if (lock != null) {
            lock.close();
        }
        return ProcessingResult.PROCESSED;
        // throw th;
    }

    private ProcessingResult stop(DirectSoundClient directSoundClient, XOutputStream xOutputStream) throws IOException {
        directSoundClient.stop();
        XStreamLock lock = xOutputStream.lock();
		Throwable th0;
        try {
            xOutputStream.writeInt(0);
            if (lock != null) {
                lock.close();
            }
            return ProcessingResult.PROCESSED;
        } catch (Throwable th) {
            th0 = th;
        }
        // throw th;
        if (lock != null) {
            if (th0 != null) {
                try {
                    lock.close();
                } catch (Throwable th2) {
                    th0.addSuppressed(th2);
                }
            } else {
                lock.close();
            }
        }
        throw new RuntimeException(th0);
    }

    private ProcessingResult recalcVolpan(DirectSoundClient directSoundClient, int i, int i2, XOutputStream xOutputStream) throws IOException {
        directSoundClient.recalcVolpan(i, i2);
        XStreamLock lock = xOutputStream.lock();
        try {
            xOutputStream.writeInt(0);
            if (lock != null) {
                lock.close();
            }
            return ProcessingResult.PROCESSED;
        } catch (Throwable th) {
            if (lock != null) {
				if (th != null) {
					try {
						lock.close();
					} catch (Throwable th2) {
						th.addSuppressed(th2);
					}
				} else {
					lock.close();
				}
			}
			throw new RuntimeException(th);
        }
        // throw th;
        
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001a, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001e, code lost:
        if (r2 != null) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0020, code lost:
        if (r3 != null) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0026, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0027, code lost:
        r3.addSuppressed(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002b, code lost:
        r2.close();
     */
    private ProcessingResult setCurrentPosition(DirectSoundClient directSoundClient, int i, XOutputStream xOutputStream) throws IOException {
        if (!directSoundClient.setCurrentPosition(i)) {
            return ProcessingResult.PROCESSED_KILL_CONNECTION;
        }
        XStreamLock lock = xOutputStream.lock();
        xOutputStream.writeInt(0);
        if (lock != null) {
            lock.close();
        }
        return ProcessingResult.PROCESSED;
        // throw th;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0030, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0034, code lost:
        if (r7 != null) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
        if (r1 != null) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003c, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        r1.addSuppressed(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0041, code lost:
        r7.close();
     */
    private ProcessingResult setNotificationPositions(DirectSoundClient directSoundClient, int i, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        if (i != 0) {
            int[] iArr = new int[i];
            int[] iArr2 = new int[i];
            for (int i2 = 0; i2 < i; i2++) {
                iArr[i2] = xInputStream.getInt();
                iArr2[i2] = xInputStream.getInt();
            }
            directSoundClient.setNotificationPositions(iArr, iArr2);
        } else {
            directSoundClient.setNotificationPositions(null, null);
        }
        XStreamLock lock = xOutputStream.lock();
        xOutputStream.writeInt(0);
        if (lock != null) {
            lock.close();
        }
        return ProcessingResult.PROCESSED;
        // throw th;
    }

    private ProcessingResult initGlobalNotifier(DirectSoundClient directSoundClient, XOutputStream xOutputStream) throws IOException {
        DirectSoundGlobalNotifier.createInstance(directSoundClient, xOutputStream);
        XStreamLock lock = xOutputStream.lock();
        try {
            xOutputStream.writeInt(0);
            if (lock != null) {
                lock.close();
            }
            return ProcessingResult.PROCESSED;
        } catch (Throwable th) {
            if (lock != null) {
				if (th != null) {
					try {
						lock.close();
					} catch (Throwable th2) {
						th.addSuppressed(th2);
					}
				} else {
					lock.close();
				}
			}
			throw new RuntimeException(th);
        }
    }
}
