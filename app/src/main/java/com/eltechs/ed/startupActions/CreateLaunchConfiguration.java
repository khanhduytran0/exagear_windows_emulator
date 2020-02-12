package com.eltechs.ed.startupActions;

import com.eltechs.axs.configuration.UBTLaunchConfiguration;
import com.eltechs.axs.configuration.UBTLaunchConfiguration.VFSHacks;
import com.eltechs.axs.configuration.startup.EnvironmentCustomisationParameters;
import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;
import com.eltechs.axs.helpers.SafeFileHelpers;
import com.eltechs.ed.EDApplicationState;
import java.io.File;
import java.util.EnumSet;

public class CreateLaunchConfiguration<StateClass extends EDApplicationState> extends AbstractStartupAction<StateClass> {
    final File applicationWorkingDir;
    private final String[] argv;
    private final String[] envp;
    private final String socketPathSuffix;
    private final String userAreaDir;
    private final String winePrefix;

    public CreateLaunchConfiguration(File file, String str, String[] strArr, String[] strArr2, String str2, String str3) {
        this.applicationWorkingDir = file;
        this.winePrefix = str;
        this.argv = strArr;
        this.envp = strArr2;
        this.socketPathSuffix = str2;
        this.userAreaDir = str3;
    }

    public void execute() {
        EDApplicationState eDApplicationState = (EDApplicationState) getApplicationState();
        EnvironmentCustomisationParameters environmentCustomisationParameters = eDApplicationState.getSelectedExecutableFile().getEnvironmentCustomisationParameters();
        UBTLaunchConfiguration uBTLaunchConfiguration = new UBTLaunchConfiguration();
        uBTLaunchConfiguration.setFsRoot(eDApplicationState.getExagearImage().getPath().getAbsolutePath());
        uBTLaunchConfiguration.setGuestExecutablePath(this.applicationWorkingDir.getAbsolutePath());
        uBTLaunchConfiguration.setGuestExecutable(this.argv[0]);
        uBTLaunchConfiguration.setGuestArguments(this.argv);
        uBTLaunchConfiguration.setGuestEnvironmentVariables(this.envp);
        uBTLaunchConfiguration.addEnvironmentVariable("LC_ALL", environmentCustomisationParameters.getLocaleName());
        uBTLaunchConfiguration.addArgumentsToEnvironment(eDApplicationState.getEnvironment());
        File path = ((EDApplicationState) getApplicationState()).getExagearImage().getPath();
        StringBuilder sb = new StringBuilder();
        sb.append(this.winePrefix);
        sb.append("/dosdevices/c:");
        SafeFileHelpers.symlink("../drive_c", new File(path, sb.toString()).getAbsolutePath());
        String str = this.userAreaDir;
        File path2 = ((EDApplicationState) getApplicationState()).getExagearImage().getPath();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.winePrefix);
        sb2.append("/dosdevices/d:");
        SafeFileHelpers.symlink(str, new File(path2, sb2.toString()).getAbsolutePath());
        File path3 = ((EDApplicationState) getApplicationState()).getExagearImage().getPath();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.winePrefix);
        sb3.append("/dosdevices/e:");
        SafeFileHelpers.symlink("/tmp/", new File(path3, sb3.toString()).getAbsolutePath());
        File path4 = ((EDApplicationState) getApplicationState()).getExagearImage().getPath();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.winePrefix);
        sb4.append("/dosdevices/z:");
        SafeFileHelpers.symlink("/", new File(path4, sb4.toString()).getAbsolutePath());
        uBTLaunchConfiguration.setVfsHacks(EnumSet.of(VFSHacks.TREAT_LSTAT_SOCKET_AS_STATTING_WINESERVER_SOCKET, VFSHacks.TRUNCATE_STAT_INODE, VFSHacks.SIMPLE_PASS_DEV));
        uBTLaunchConfiguration.setSocketPathSuffix(this.socketPathSuffix);
        eDApplicationState.setUBTLaunchConfiguration(uBTLaunchConfiguration);
        sendDone();
    }
}
