package com.eltechs.axs.configuration.startup;

import com.eltechs.axs.configuration.startup.actions.DetectExecutableFiles.ExecutableFileDetector;
import java.util.Collection;

public class ExecutableFileDetectorsCollection<StateClass> {
    private final ExecutableFileDetector<StateClass> defaultDetector;
    private final Collection<? extends ExecutableFileDetector<StateClass>> detectors;

    public ExecutableFileDetectorsCollection(Collection<? extends ExecutableFileDetector<StateClass>> collection, ExecutableFileDetector<StateClass> executableFileDetector) {
        this.detectors = collection;
        this.defaultDetector = executableFileDetector;
    }

    public Collection<? extends ExecutableFileDetector<StateClass>> getDetectors() {
        return this.detectors;
    }

    public ExecutableFileDetector<StateClass> getDefaultDetector() {
        return this.defaultDetector;
    }
}
