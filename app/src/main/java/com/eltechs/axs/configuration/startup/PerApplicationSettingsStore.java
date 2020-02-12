package com.eltechs.axs.configuration.startup;

import com.eltechs.axs.helpers.AndroidHelpers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.io.IOUtils;

public class PerApplicationSettingsStore {
    private final String ecpFile;
    private final DetectedExecutableFile<?> settingsFor;

    private PerApplicationSettingsStore(DetectedExecutableFile<?> detectedExecutableFile) {
        this.settingsFor = detectedExecutableFile;
        String replace = new File(detectedExecutableFile.getParentDir(), detectedExecutableFile.getFileName()).getAbsolutePath().replace(IOUtils.DIR_SEPARATOR_UNIX, '_');
        StringBuilder sb = new StringBuilder();
        sb.append("ecp_");
        sb.append(replace);
        this.ecpFile = sb.toString();
    }

    public static PerApplicationSettingsStore get(DetectedExecutableFile<?> detectedExecutableFile) {
        return new PerApplicationSettingsStore(detectedExecutableFile);
    }

    public void updateDetectedExecutableFileConfiguration() throws IOException {
        try {
            this.settingsFor.getEnvironmentCustomisationParameters().copyFrom(loadEnvironmentCustomisationParameters());
        } catch (FileNotFoundException unused) {
        }
    }

    public void storeDetectedExecutableFileConfiguration() throws IOException {
        storeEnvironmentCustomisationParameters(this.settingsFor.getEnvironmentCustomisationParameters());
    }

    private EnvironmentCustomisationParameters loadEnvironmentCustomisationParameters() throws IOException {
        FileInputStream openPrivateFileForReading;
        try {
            openPrivateFileForReading = AndroidHelpers.openPrivateFileForReading(this.ecpFile);
            EnvironmentCustomisationParameters environmentCustomisationParameters = (EnvironmentCustomisationParameters) new ObjectInputStream(openPrivateFileForReading).readObject();
            if (openPrivateFileForReading != null) {
                openPrivateFileForReading.close();
            }
            return environmentCustomisationParameters;
        } catch (ClassNotFoundException e) {
            throw new IOException("Deserialisation of EnvironmentCustomisationParameters has failed.", e);
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
        // throw th;
    }

    private void storeEnvironmentCustomisationParameters(EnvironmentCustomisationParameters environmentCustomisationParameters) throws IOException {
        // Throwable th;
        FileOutputStream openPrivateFileForWriting = AndroidHelpers.openPrivateFileForWriting(this.ecpFile);
        try {
            new ObjectOutputStream(openPrivateFileForWriting).writeObject(environmentCustomisationParameters);
            if (openPrivateFileForWriting != null) {
                openPrivateFileForWriting.close();
                return;
            }
            return;
        } catch (Throwable th2) {
            throw new RuntimeException(th2);
        }
        // throw th;
    }
}
