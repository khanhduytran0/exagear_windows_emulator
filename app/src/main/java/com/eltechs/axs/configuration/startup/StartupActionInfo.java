package com.eltechs.axs.configuration.startup;

public class StartupActionInfo {
    private final String progressFilename;
    private final String stepDescription;

    public StartupActionInfo(String str) {
        this.stepDescription = str;
        this.progressFilename = null;
    }

    public StartupActionInfo(String str, String str2) {
        this.stepDescription = str;
        this.progressFilename = str2;
    }

    public String getStepDescription() {
        return this.stepDescription;
    }

    public String getProgressFilename() {
        return this.progressFilename;
    }
}
