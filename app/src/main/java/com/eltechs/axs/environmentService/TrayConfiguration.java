package com.eltechs.axs.environmentService;

public class TrayConfiguration {
    private final int returnToDescription;
    private final int trayIcon;
    private final int trayIconName;

    public TrayConfiguration(int i, int i2, int i3) {
        this.trayIcon = i;
        this.trayIconName = i2;
        this.returnToDescription = i3;
    }

    public int getTrayIcon() {
        return this.trayIcon;
    }

    public int getTrayIconName() {
        return this.trayIconName;
    }

    public int getReturnToDescription() {
        return this.returnToDescription;
    }
}
