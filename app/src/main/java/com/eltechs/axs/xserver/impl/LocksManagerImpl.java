package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.LocksManager;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.concurrent.locks.ReentrantLock;

public class LocksManagerImpl implements LocksManager {
    private static final Subsystem[] locksOnInputDevices = {Subsystem.INPUT_DEVICES, Subsystem.WINDOWS_MANAGER, Subsystem.KEYBOARD_MODEL_MANAGER, Subsystem.FOCUS_MANAGER};
    /* access modifiers changed from: private */
    public final EnumMap<Subsystem, ReentrantLock> locks = new EnumMap<>(Subsystem.class);

    private final class MultiXLock implements XLock {
        private final Subsystem[] systems;

        public MultiXLock(Subsystem[] subsystemArr) {
            this.systems = subsystemArr;
            EnumMap access$100 = LocksManagerImpl.this.locks;
            for (Subsystem subsystem : subsystemArr) {
                ((ReentrantLock) access$100.get(subsystem)).lock();
            }
        }

        public void close() {
            EnumMap access$100 = LocksManagerImpl.this.locks;
            for (int length = this.systems.length - 1; length >= 0; length--) {
                ((ReentrantLock) access$100.get(this.systems[length])).unlock();
            }
        }
    }

    private static final class NullXLock implements XLock {
        /* access modifiers changed from: private */
        public static final XLock INSTANCE = new NullXLock();

        public void close() {
        }

        private NullXLock() {
        }
    }

    private static final class SingleXLock implements XLock {
        private final ReentrantLock lock;

        public SingleXLock(ReentrantLock reentrantLock) {
            this.lock = reentrantLock;
            reentrantLock.lock();
        }

        public void close() {
            this.lock.unlock();
        }
    }

    public LocksManagerImpl() {
        for (Subsystem put : Subsystem.values()) {
            this.locks.put(put, new ReentrantLock());
        }
    }

    public boolean isLocked(Subsystem subsystem) {
        return ((ReentrantLock) this.locks.get(subsystem)).isLocked();
    }

    public XLock lock(Subsystem subsystem) {
        return new SingleXLock((ReentrantLock) this.locks.get(subsystem));
    }

    public XLock lock(Subsystem[] subsystemArr) {
        if (subsystemArr.length == 0) {
            return NullXLock.INSTANCE;
        }
        if (subsystemArr.length == 1) {
            return lock(subsystemArr[0]);
        }
        Arrays.sort(subsystemArr);
        return new MultiXLock(subsystemArr);
    }

    public XLock lockForInputDevicesManipulation() {
        return new MultiXLock(locksOnInputDevices);
    }

    public XLock lockAll() {
        return lock(Subsystem.values());
    }
}
