package com.eltechs.axs;

import android.content.SharedPreferences;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;
import com.eltechs.axs.helpers.Assert;
import java.io.File;
import org.apache.commons.io.IOUtils;

public class ApplicationConfigurationAccessor<StateClass extends ApplicationStateBase<StateClass> & SelectedExecutableFileAware<?>> {
    protected final SharedPreferences prefs;

    public ApplicationConfigurationAccessor() {
        DetectedExecutableFile selectedExecutableFile = ((SelectedExecutableFileAware) ((ApplicationStateBase) Globals.getApplicationState())).getSelectedExecutableFile();
        Assert.state(selectedExecutableFile != null);
        this.prefs = Globals.getAppContext().getSharedPreferences(new File(selectedExecutableFile.getParentDir(), selectedExecutableFile.getFileName()).getAbsolutePath().replace(IOUtils.DIR_SEPARATOR_UNIX, '_'), 0);
    }
}
