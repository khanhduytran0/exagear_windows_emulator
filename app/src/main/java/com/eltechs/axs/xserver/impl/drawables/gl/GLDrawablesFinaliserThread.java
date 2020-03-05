package com.eltechs.axs.xserver.impl.drawables.gl;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

public class GLDrawablesFinaliserThread extends Thread {
    private final Map<Reference<?>, Runnable> finalisationHandlers = new HashMap();
    private final ReferenceQueue<?> referenceQueue;

    GLDrawablesFinaliserThread(ReferenceQueue<?> referenceQueue2) {
        this.referenceQueue = referenceQueue2;
    }

    public void registerFinalisationHandler(Object obj, Runnable runnable) {
        PhantomReference phantomReference = new PhantomReference(obj, this.referenceQueue);
        synchronized (this.finalisationHandlers) {
            this.finalisationHandlers.put(phantomReference, runnable);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:0|1|8|6|7|14|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0016, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:0:0x0000 */
    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP:0: B:0:0x0000->B:13:0x0000, LOOP_START, SYNTHETIC, Splitter:B:0:0x0000] */
    public void run() {
        try {
			Runnable runnable;
			while (true) {
				Reference remove = this.referenceQueue.remove();
				synchronized (this.finalisationHandlers) {
					runnable = (Runnable) this.finalisationHandlers.remove(remove);
				}
				runnable.run();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
    }
}
