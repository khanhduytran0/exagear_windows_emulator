package com.eltechs.axs.applicationState;

import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;

public interface SelectedExecutableFileAware<StateClass extends SelectedExecutableFileAware<StateClass>> {
    DetectedExecutableFile<StateClass> getSelectedExecutableFile();

    void setSelectedExecutableFile(DetectedExecutableFile<StateClass> detectedExecutableFile);
}
