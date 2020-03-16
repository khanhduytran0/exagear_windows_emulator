package com.eltechs.axs.configuration.startup;

import com.eltechs.axs.configuration.startup.actions.DetectExecutableFiles.ExecutableFileDetector;
import java.io.File;

public abstract class SimpleExecutableFileDetector<StateClass> implements ExecutableFileDetector<StateClass> {
    private final String fileName;

    /* access modifiers changed from: protected */
    public abstract DetectedExecutableFile<StateClass> createDescriptor(File file, String str);

    public SimpleExecutableFileDetector(String str) {
        this.fileName = str;
    }

    public final DetectedExecutableFile<StateClass> detect(File file, String str) {
        if (str.equalsIgnoreCase(this.fileName)) {
            return createDescriptor(file, str);
        }
        return null;
    }
}
