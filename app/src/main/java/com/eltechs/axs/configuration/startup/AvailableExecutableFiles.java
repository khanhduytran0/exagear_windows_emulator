package com.eltechs.axs.configuration.startup;

import java.util.List;

public class AvailableExecutableFiles<StateClass> {
    private final List<DetectedExecutableFile<StateClass>> otherFiles;
    private final List<DetectedExecutableFile<StateClass>> supportedFiles;

    public AvailableExecutableFiles(List<DetectedExecutableFile<StateClass>> list, List<DetectedExecutableFile<StateClass>> list2) {
        this.supportedFiles = list;
        this.otherFiles = list2;
    }

    public List<DetectedExecutableFile<StateClass>> getSupportedFiles() {
        return this.supportedFiles;
    }

    public List<DetectedExecutableFile<StateClass>> getOtherFiles() {
        return this.otherFiles;
    }
}
