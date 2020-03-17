package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.activities.SelectExecutableFileActivity;
import com.eltechs.axs.activities.StartupActivity;
import com.eltechs.axs.applicationState.AvailableExecutableFilesAware;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.applicationState.UserApplicationsDirectoryNameAware;
import com.eltechs.axs.configuration.startup.AvailableExecutableFiles;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;
import com.eltechs.axs.configuration.startup.ExecutableFileDetectorsCollection;
import com.eltechs.axs.configuration.startup.PerApplicationSettingsStore;
import com.eltechs.axs.helpers.Assert;
import java.io.IOException;
import java.util.List;

public class SelectExecutableFile<StateClass extends AvailableExecutableFilesAware<StateClass> & SelectedExecutableFileAware<StateClass> & UserApplicationsDirectoryNameAware> extends InteractiveStartupActionBase<StateClass, SelectExecutableFile.UserRequestedAction> {
    private final ExecutableFileDetectorsCollection<StateClass> detectors;
    private final String installationInstructions;

    public enum UserRequestedAction {
        EXECUTABLE_FILE_SELECTED,
        FULL_SCAN_REQUESTED
    }

    public SelectExecutableFile(String str, ExecutableFileDetectorsCollection<StateClass> executableFileDetectorsCollection) {
        this.installationInstructions = str;
        this.detectors = executableFileDetectorsCollection;
    }

    public void execute() {
        AvailableExecutableFiles availableExecutableFiles = ((AvailableExecutableFilesAware) getApplicationState()).getAvailableExecutableFiles();
        List supportedFiles = availableExecutableFiles.getSupportedFiles();
        List otherFiles = availableExecutableFiles.getOtherFiles();
        if (!supportedFiles.isEmpty() || !otherFiles.isEmpty()) {
            requestUserInput(SelectExecutableFileActivity.class);
        } else {
            sendErrorHtml(this.installationInstructions);
        }
    }

    public void userInteractionFinished(UserRequestedAction userRequestedAction) {
        switch (userRequestedAction) {
            case EXECUTABLE_FILE_SELECTED:
                runApplication();
                return;
            case FULL_SCAN_REQUESTED:
                getStartupActions().addAction(new DetectExecutableFiles(this.detectors, false));
                getStartupActions().addAction(new SelectExecutableFile(this.installationInstructions, this.detectors));
                sendDone();
                return;
            default:
                return;
        }
    }

    public void userInteractionCanceled() {
        StartupActivity.shutdownAXSApplication();
    }

    private void runApplication() {
        DetectedExecutableFile selectedExecutableFile = ((SelectedExecutableFileAware) ((AvailableExecutableFilesAware) getApplicationState())).getSelectedExecutableFile();
        persistPerApplicationSettings();
        getStartupActions().addActions(selectedExecutableFile.getFurtherStartupActions());
        sendDone();
    }

    private void persistPerApplicationSettings() {
        try {
            PerApplicationSettingsStore.get(((SelectedExecutableFileAware) ((AvailableExecutableFilesAware) getApplicationState())).getSelectedExecutableFile()).storeDetectedExecutableFileConfiguration();
        } catch (IOException unused) {
        }
    }
}
