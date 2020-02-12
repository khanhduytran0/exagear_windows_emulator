package com.eltechs.axs.guestApplicationsTracker;

import com.eltechs.axs.ExagearImageConfiguration.TempDirMaintenanceComponent;
import com.eltechs.axs.configuration.UBTLaunchConfiguration;
import com.eltechs.axs.configuration.UBTLaunchConfiguration.VFSHacks;
import com.eltechs.axs.environmentService.AXSEnvironment;
import com.eltechs.axs.environmentService.components.GuestApplicationsTrackerComponent;
import com.eltechs.axs.environmentService.components.SysVIPCEmulatorComponent;
import com.eltechs.axs.environmentService.components.VFSTrackerComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class UBT {
    private static native int runUbt(String str, String str2, String str3, String[] strArr, String[] strArr2);

    private UBT() {
    }

    static {
        System.loadLibrary("ubt-helpers");
    }

    public static int runUbt(UBTLaunchConfiguration uBTLaunchConfiguration, AXSEnvironment aXSEnvironment, String str) {
        String fsRoot = uBTLaunchConfiguration.getFsRoot();
        String guestExecutable = uBTLaunchConfiguration.getGuestExecutable();
        List guestArguments = uBTLaunchConfiguration.getGuestArguments();
        List guestEnvironmentVariables = uBTLaunchConfiguration.getGuestEnvironmentVariables();
        String guestExecutablePath = uBTLaunchConfiguration.getGuestExecutablePath();
        StringBuilder sb = new StringBuilder();
        sb.append(fsRoot);
        sb.append("/.exagear/vpaths-list");
        String sb2 = sb.toString();
        GuestApplicationsTrackerComponent guestApplicationsTrackerComponent = (GuestApplicationsTrackerComponent) aXSEnvironment.getComponent(GuestApplicationsTrackerComponent.class);
        ArrayList arrayList = new ArrayList(24 + guestArguments.size());
        arrayList.add("libubt");
        arrayList.add("--vfs-kind");
        arrayList.add("guest-first");
        arrayList.add("--path-prefix");
        arrayList.add(fsRoot);
        arrayList.add("--vpaths-list");
        arrayList.add(sb2);
        arrayList.add("-f");
        arrayList.add(guestExecutable);
        arrayList.add("--fork-controller");
        StringBuilder sb3 = new StringBuilder();
        sb3.append("ua:");
        sb3.append(guestApplicationsTrackerComponent.getSocket());
        arrayList.add(sb3.toString());
        SysVIPCEmulatorComponent sysVIPCEmulatorComponent = (SysVIPCEmulatorComponent) aXSEnvironment.getComponent(SysVIPCEmulatorComponent.class);
        if (sysVIPCEmulatorComponent != null) {
            arrayList.add("--ipc-emul-server");
            StringBuilder sb4 = new StringBuilder();
            sb4.append("ua:");
            sb4.append(sysVIPCEmulatorComponent.getDomainName());
            arrayList.add(sb4.toString());
        }
        TempDirMaintenanceComponent tempDirMaintenanceComponent = (TempDirMaintenanceComponent) aXSEnvironment.getComponent(TempDirMaintenanceComponent.class);
        if (tempDirMaintenanceComponent != null) {
            arrayList.add("--tmp-dir");
            arrayList.add(tempDirMaintenanceComponent.getTempDir().getAbsolutePath());
        }
        if (uBTLaunchConfiguration.isStraceEnabled()) {
            arrayList.add("--strace");
        }
        VFSTrackerComponent vFSTrackerComponent = (VFSTrackerComponent) aXSEnvironment.getComponent(VFSTrackerComponent.class);
        if (vFSTrackerComponent != null) {
            arrayList.add("--vfs-tracker-controller");
            StringBuilder sb5 = new StringBuilder();
            sb5.append("ua:");
            sb5.append(vFSTrackerComponent.getSocket());
            arrayList.add(sb5.toString());
            if (vFSTrackerComponent.getTrackedFiles().size() > 0) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("--track-files=");
                for (String append : vFSTrackerComponent.getTrackedFiles()) {
                    sb6.append(append);
                    sb6.append(',');
                }
                arrayList.add(sb6.toString());
            }
        }
        Set<VFSHacks> vfsHacks = uBTLaunchConfiguration.getVfsHacks();
        if (!vfsHacks.isEmpty()) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("--vfs-hacks=");
            for (VFSHacks shortName : vfsHacks) {
                sb7.append(shortName.getShortName());
                sb7.append(',');
            }
            arrayList.add(sb7.toString());
        }
        Map fileNameReplacements = uBTLaunchConfiguration.getFileNameReplacements();
        if (!fileNameReplacements.isEmpty()) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append("--file-name-replacements=");
            for (Entry entry : fileNameReplacements.entrySet()) {
                sb8.append((String) entry.getKey());
                sb8.append(',');
                sb8.append((String) entry.getValue());
                sb8.append(',');
            }
            arrayList.add(sb8.toString());
        }
        String socketPathSuffix = uBTLaunchConfiguration.getSocketPathSuffix();
        if (socketPathSuffix != null) {
            arrayList.add("--socket-path-suffix");
            arrayList.add(socketPathSuffix);
        }
        arrayList.add("--ubt-executable");
        arrayList.add(str);
        String elfLoaderPath = aXSEnvironment.getNativeLibsConfiguration().getElfLoaderPath();
        arrayList.add("--ubt-loader");
        arrayList.add(elfLoaderPath);
        arrayList.add("--");
        arrayList.addAll(guestArguments);
        return runUbt(guestExecutablePath, elfLoaderPath, str, (String[]) arrayList.toArray(new String[arrayList.size()]), (String[]) guestEnvironmentVariables.toArray(new String[guestEnvironmentVariables.size()]));
    }
}
