package com.eltechs.axs.configuration.startup;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.ScreenInfo;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class EnvironmentCustomisationParameters implements Externalizable {
    public static String DEFAULT_CONTROLS_NAME_NONE = "None";
    private static final int STORAGE_FORMAT_VERSION_1 = 1;
    private static final int STORAGE_FORMAT_VERSION_CURR = 2;
    private static final long serialVersionUID = -353894079071511841L;
    private String defaultControlsName = DEFAULT_CONTROLS_NAME_NONE;
    private String localeName = "C";
    private ScreenInfo screenInfo;

    public EnvironmentCustomisationParameters() {
        ScreenInfo screenInfo2 = new ScreenInfo(800, 600, 80, 60, 32);
        this.screenInfo = screenInfo2;
    }

    public ScreenInfo getScreenInfo() {
        return this.screenInfo;
    }

    public void setScreenInfo(ScreenInfo screenInfo2) {
        this.screenInfo = screenInfo2;
    }

    public String getLocaleName() {
        return this.localeName;
    }

    public void setLocaleName(String str) {
        this.localeName = str;
    }

    public String getDefaultControlsName() {
        return this.defaultControlsName;
    }

    public void setDefaultControlsName(String str) {
        this.defaultControlsName = str;
    }

    public void copyFrom(EnvironmentCustomisationParameters environmentCustomisationParameters) {
        this.screenInfo = environmentCustomisationParameters.screenInfo;
        this.localeName = environmentCustomisationParameters.localeName;
        this.defaultControlsName = environmentCustomisationParameters.defaultControlsName;
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        int readInt = objectInput.readInt();
        Assert.isTrue(readInt <= 2, "Attempted to load EnvironmentCustomisationParameters created by a newer version of AXS.");
        if (readInt == 1) {
            this.screenInfo = (ScreenInfo) objectInput.readObject();
            this.localeName = objectInput.readUTF();
            this.defaultControlsName = DEFAULT_CONTROLS_NAME_NONE;
        } else if (readInt == 2) {
            this.screenInfo = (ScreenInfo) objectInput.readObject();
            this.localeName = objectInput.readUTF();
            this.defaultControlsName = objectInput.readUTF();
        }
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeInt(2);
        objectOutput.writeObject(this.screenInfo);
        objectOutput.writeUTF(this.localeName);
        objectOutput.writeUTF(this.defaultControlsName);
    }
}
