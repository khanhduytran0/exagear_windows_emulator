package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.ExagearImageConfiguration.ExagearImageConfigurationHelpers;
import com.eltechs.axs.applicationState.EnvironmentAware;
import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.applicationState.UBTLaunchConfigurationAware;
import com.eltechs.axs.configuration.UBTLaunchConfiguration;
import com.eltechs.axs.configuration.UBTLaunchConfiguration.VFSHacks;
import com.eltechs.axs.configuration.startup.EnvironmentCustomisationParameters;
import com.eltechs.axs.helpers.FileHelpers;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.LinkedHashMap;

public class CreateTypicalWineLaunchConfiguration<StateClass extends EnvironmentAware & ExagearImageAware & UBTLaunchConfigurationAware & SelectedExecutableFileAware<StateClass>> extends AbstractStartupAction<StateClass> {
    private final boolean additionalDiskPointsToGameDir;
    private final String[] argv;
    private final String[] envp;
    private final String homeDir;
    private final boolean putAdditionalDisks;
    private final String socketPathSuffix;

    public CreateTypicalWineLaunchConfiguration(String str, String[] strArr, String[] strArr2, String str2, boolean z, boolean z2) {
        this.homeDir = str;
        this.argv = strArr;
        this.envp = strArr2;
        this.socketPathSuffix = str2;
        this.putAdditionalDisks = z;
        this.additionalDiskPointsToGameDir = z2;
    }

    public void execute() {
        EnvironmentAware environmentAware = (EnvironmentAware) getApplicationState();
        SelectedExecutableFileAware selectedExecutableFileAware = (SelectedExecutableFileAware) environmentAware;
        File parentDir = selectedExecutableFileAware.getSelectedExecutableFile().getParentDir();
        EnvironmentCustomisationParameters environmentCustomisationParameters = selectedExecutableFileAware.getSelectedExecutableFile().getEnvironmentCustomisationParameters();
        String exagearRootFromPath = FileHelpers.getExagearRootFromPath(parentDir);
        UBTLaunchConfiguration uBTLaunchConfiguration = new UBTLaunchConfiguration();
        uBTLaunchConfiguration.setFsRoot(((ExagearImageAware) environmentAware).getExagearImage().getPath().getAbsolutePath());
        uBTLaunchConfiguration.setGuestExecutablePath(parentDir.getAbsolutePath());
        uBTLaunchConfiguration.setGuestExecutable("/usr/bin/wine");
        uBTLaunchConfiguration.setGuestArguments(this.argv);
        uBTLaunchConfiguration.setGuestEnvironmentVariables(this.envp);
        uBTLaunchConfiguration.addEnvironmentVariable("LC_ALL", environmentCustomisationParameters.getLocaleName());
        uBTLaunchConfiguration.addArgumentsToEnvironment(environmentAware.getEnvironment());
        uBTLaunchConfiguration.addEnvironmentVariable("EXADROID_DISABLE_SHORT_NAMES", "y");
        try {
            if (FileHelpers.checkCaseInsensitivityInDirectory(new File(exagearRootFromPath))) {
                uBTLaunchConfiguration.addEnvironmentVariable("EXADROID_FS_CASE_INSENSITIVE", "y");
            }
            String absolutePath = parentDir.getAbsolutePath();
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            try {
                ExagearImageConfigurationHelpers exagearImageConfigurationHelpers = new ExagearImageConfigurationHelpers(((ExagearImageAware) ((EnvironmentAware) getApplicationState())).getExagearImage());
                StringBuilder sb = new StringBuilder();
                sb.append(this.homeDir);
                sb.append(".wine/");
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append("dosdevices/c_");
                String sb4 = sb3.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(sb2);
                sb5.append("drive_c");
                linkedHashMap.put(sb4, sb5.toString());
                StringBuilder sb6 = new StringBuilder();
                sb6.append(sb2);
                sb6.append("dosdevices/d_");
                linkedHashMap.put(sb6.toString(), exagearRootFromPath);
                StringBuilder sb7 = new StringBuilder();
                sb7.append(sb2);
                sb7.append("dosdevices");
                exagearImageConfigurationHelpers.createFakeSymlink(sb7.toString(), "d_", exagearRootFromPath);
                if (this.putAdditionalDisks) {
                    if (this.additionalDiskPointsToGameDir) {
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(sb2);
                        sb8.append("dosdevices/e_");
                        linkedHashMap.put(sb8.toString(), absolutePath);
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append(sb2);
                        sb9.append("dosdevices");
                        exagearImageConfigurationHelpers.createFakeSymlink(sb9.toString(), "e_", absolutePath);
                    } else {
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(sb2);
                        sb10.append("dosdevices/e_");
                        linkedHashMap.put(sb10.toString(), exagearRootFromPath);
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append(sb2);
                        sb11.append("dosdevices");
                        exagearImageConfigurationHelpers.createFakeSymlink(sb11.toString(), "e_", exagearRootFromPath);
                    }
                }
                uBTLaunchConfiguration.setFileNameReplacements(linkedHashMap);
                uBTLaunchConfiguration.setVfsHacks(EnumSet.of(VFSHacks.ASSUME_NO_SYMLINKS_EXCEPT_PRERESOLVED, VFSHacks.TREAT_LSTAT_SOCKET_AS_STATTING_WINESERVER_SOCKET));
                uBTLaunchConfiguration.setSocketPathSuffix(this.socketPathSuffix);
                ((UBTLaunchConfigurationAware) environmentAware).setUBTLaunchConfiguration(uBTLaunchConfiguration);
                sendDone();
            } catch (IOException e) {
                sendError("Failed to create fake symlink.", e);
            }
        } catch (IOException e2) {
            sendError("Failed to check exagear directory properties.", e2);
        }
    }
}
