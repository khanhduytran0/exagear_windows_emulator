package com.eltechs.axs.configuration.startup.actions;

import android.media.MediaScannerConnection;
import com.eltechs.axs.applicationState.UserApplicationsDirectoryNameAware;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.FileHelpers;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreatePutYourApplicationsHereDirectory<StateClass extends UserApplicationsDirectoryNameAware> extends AbstractStartupAction<StateClass> {
    private static final String README_NAME = "README.html";
    private final String readmeText;

    public CreatePutYourApplicationsHereDirectory(String str) {
        this.readmeText = str;
    }

    public void execute() {
        File file = new File(AndroidHelpers.getMainSDCard(), ((UserApplicationsDirectoryNameAware) getApplicationState()).getUserApplicationsDirectoryName().getName());
        try {
            FileHelpers.createDirectory(file);
            File file2 = FileHelpers.touch(file, README_NAME);
            FileWriter fileWriter = new FileWriter(new File(file, README_NAME));
            fileWriter.write(this.readmeText);
            fileWriter.close();
            MediaScannerConnection.scanFile(getAppContext(), new String[]{file2.getAbsolutePath()}, null, null);
        } catch (IOException unused) {
        }
        sendDone();
    }
}
