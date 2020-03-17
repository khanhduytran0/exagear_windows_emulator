package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.ed.R;
import com.eltechs.axs.activities.WarningActivity;
import com.eltechs.axs.applicationState.UserApplicationsDirectoryNameAware;
import com.eltechs.axs.helpers.FileFinder;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.StringEscapeUtils;

public class CheckWritePermissionOfUserApplicationDirs<StateClass extends UserApplicationsDirectoryNameAware> extends SimpleInteractiveStartupActionBase<StateClass> {
    private static final int DIR_WITH_USER_APPLICATION_SEARCH_DEPTH = 3;

    public void execute() {
        try {
            for (File file : FileFinder.findDirectory(new File("/mnt"), 3, ((UserApplicationsDirectoryNameAware) getApplicationState()).getUserApplicationsDirectoryName().getName())) {
                if (!file.canWrite()) {
                    requestUserInput(WarningActivity.class, StringEscapeUtils.escapeHtml4(String.format(getAppContext().getString(R.string.uadwp_external_sdcard_not_writable), new Object[]{file.getAbsolutePath()})));
                    return;
                }
            }
            sendDone();
        } catch (IOException e) {
            sendError("Failed to enumerate executable files in /mnt/sdcard/ExaGear/.", e);
        }
    }

    public void userInteractionFinished() {
        sendDone();
    }

    public void userInteractionCanceled() {
        sendDone();
    }
}
