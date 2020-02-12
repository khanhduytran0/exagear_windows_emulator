package com.eltechs.axs.xconnectors.epoll.impl;

public final class BufferSizeConfiguration {
    private int initialInputBufferCapacity = 4096;
    private int initialOutputBufferCapacity = 4096;
    private int inputBufferSizeHardLimit = 2097152;
    private int outputBufferSizeHardLimit = 2097152;
    private int outputBufferSizeLimit = 65536;

    public static BufferSizeConfiguration createDefaultConfiguration() {
        return new BufferSizeConfiguration();
    }

    public synchronized int getInitialInputBufferCapacity() {
        return this.initialInputBufferCapacity;
    }

    public synchronized void setInitialInputBufferCapacity(int i) {
        this.initialInputBufferCapacity = i;
    }

    public synchronized int getInitialOutputBufferCapacity() {
        return this.initialOutputBufferCapacity;
    }

    public synchronized void setInitialOutputBufferCapacity(int i) {
        this.initialOutputBufferCapacity = i;
    }

    public synchronized int getOutputBufferSizeLimit() {
        return this.outputBufferSizeLimit;
    }

    public synchronized void setOutputBufferSizeLimit(int i) {
        this.outputBufferSizeLimit = i;
    }

    public synchronized int getInputBufferSizeHardLimit() {
        return this.inputBufferSizeHardLimit;
    }

    public synchronized void setInputBufferSizeHardLimit(int i) {
        this.inputBufferSizeHardLimit = i;
    }

    public synchronized int getOutputBufferSizeHardLimit() {
        return this.outputBufferSizeHardLimit;
    }

    public synchronized void setOutputBufferSizeHardLimit(int i) {
        this.outputBufferSizeHardLimit = i;
    }
}
