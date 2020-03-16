package com.eltechs.ed.startupActions;

import android.util.Log;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.applicationState.XServerDisplayActivityConfigurationAware;
import com.eltechs.axs.configuration.startup.AsyncStartupAction;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;
import com.eltechs.axs.configuration.startup.EnvironmentCustomisationParameters;
import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;
import com.eltechs.axs.configuration.startup.actions.CreateTypicalEnvironmentConfiguration;
import com.eltechs.axs.configuration.startup.actions.PrepareGuestImage;
import com.eltechs.axs.configuration.startup.actions.StartEnvironmentService;
import com.eltechs.axs.configuration.startup.actions.WaitForXClientConnection;
import com.eltechs.axs.environmentService.StartGuestApplication;
import com.eltechs.axs.environmentService.TrayConfiguration;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.FileHelpers;
import com.eltechs.axs.helpers.UiThread;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.ed.ContainerPackage;
import com.eltechs.ed.InstallRecipe;
import com.eltechs.ed.R;
import com.eltechs.ed.XDGLink;
import com.eltechs.ed.activities.EDStartupActivity;
import com.eltechs.ed.controls.Controls;
import com.eltechs.ed.guestContainers.GuestContainer;
import com.eltechs.ed.guestContainers.GuestContainerConfig;
import com.eltechs.ed.guestContainers.GuestContainersManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class StartGuest<StateClass extends ApplicationStateBase<StateClass> & SelectedExecutableFileAware<StateClass> & XServerDisplayActivityConfigurationAware> extends AbstractStartupAction<StateClass> implements AsyncStartupAction<StateClass> {
    private static final String TAG = "StartGuest";
    private static final File mUserAreaDir = new File(AndroidHelpers.getMainSDCard(), "Download");
    private GuestContainer mCont;
    private String mContStartupActions;
    /* access modifiers changed from: private */
    public Controls mControls;
    private List<String> mEnv = new ArrayList();
    private List<String> mExeArgv = new ArrayList();
    private File mExeWorkDir;
    private boolean mForceUseDefaultContols = false;
    private boolean mForceUseDefaultResolution = false;
    private final GuestContainersManager mGcm = GuestContainersManager.getInstance(((ApplicationStateBase) getApplicationState()).getAndroidApplicationContext());
    private boolean mHideTaskbar = false;
    private boolean mHideXServerImage = false;
    private String mLocaleName;
    private String mRunArguments;
    private String mRunGuide;
    private File mRunScriptToCopy;
    private ScreenInfo mScreenInfo;
	
	// MOD: Additional method
	public static class RunOption {
		protected RunOption() {
		}
	}

    public static class InstallApp extends RunOption {
        /* access modifiers changed from: private */
        public GuestContainer mCont;
        /* access modifiers changed from: private */
        public String mExePath;
        /* access modifiers changed from: private */
        public InstallRecipe mRecipe;

        public InstallApp(GuestContainer guestContainer, String str, InstallRecipe installRecipe) {
            this.mCont = guestContainer;
            this.mExePath = str;
            this.mRecipe = installRecipe;
        }
    }

    public static class InstallPackage extends RunOption {
        /* access modifiers changed from: private */
        public GuestContainer mCont;
        /* access modifiers changed from: private */
        public List<ContainerPackage> mPackages;

        public InstallPackage(GuestContainer guestContainer, List<ContainerPackage> list) {
            this.mCont = guestContainer;
            this.mPackages = list;
        }
    }

    public static class RunExplorer extends RunOption {
        /* access modifiers changed from: private */
        public GuestContainer mCont;

        public RunExplorer(GuestContainer guestContainer) {
            this.mCont = guestContainer;
        }
    }
	
	// MOD: Add Start Linux x86 option.
	public static class RunLinuxCommand extends RunOption {
		public GuestContainer mCont;
		public String[] mCommand;

        public RunLinuxCommand(GuestContainer guestContainer, String[] command) {
            this.mCont = guestContainer;
			this.mCommand = command;
        }
	}

    public static class RunXDGLink extends RunOption {
        /* access modifiers changed from: private */
        private XDGLink mLink;

        public RunXDGLink(XDGLink xDGLink) {
            this.mLink = xDGLink;
        }
    }

	public StartGuest(RunOption option) {
		if (option instanceof InstallPackage) {
			InstallPackage installPackage = (InstallPackage) option;
			this.mCont = installPackage.mCont;
			this.mExeWorkDir = new File(mUserAreaDir.getAbsolutePath());
			this.mExeArgv.addAll(Arrays.asList(new String[]{"/bin/bash", "-x", getRecipeGuestPath("install_packages.sh")}));
			String str = "";
			for (ContainerPackage containerPackage : installPackage.mPackages) {
				StringBuilder sb = new StringBuilder();
				sb.append(str);
				sb.append(containerPackage.mName);
				sb.append(" ");
				str = sb.toString();
			}
			List<String> list = this.mEnv;
			StringBuilder sb2 = new StringBuilder();
			sb2.append("INSTALL_PACKAGES=");
			sb2.append(str);
			list.add(sb2.toString());
			this.mHideXServerImage = true;
			if (this.mCont.mConfig.getDefaultControlsNotShortcut()) {
				this.mForceUseDefaultContols = true;
			}
			if (this.mCont.mConfig.getDefaultResolutionNotShortcut()) {
				this.mForceUseDefaultResolution = true;
			}
		} else if (option instanceof InstallApp) {
			InstallApp installApp = (InstallApp) option;
			InstallRecipe access = installApp.mRecipe;
			String accessStr = installApp.mExePath;
			this.mCont = installApp.mCont;
			this.mScreenInfo = access.mScreenInfo;
			this.mControls = access.mControls;
			this.mLocaleName = access.mLocaleName;
			this.mRunArguments = access.mRunArguments;
			this.mContStartupActions = access.mStartupActions;
			this.mRunGuide = access.mRunGuide;
			this.mRunScriptToCopy = new File(this.mGcm.getGuestImagePath(), getRecipeGuestPath(access.mRunScriptName));
			this.mExeWorkDir = new File(accessStr).getParentFile();
			String cutRootPrefixFromPath = FileHelpers.cutRootPrefixFromPath(new File(installApp.mExePath), mUserAreaDir);
			StringBuilder sb = new StringBuilder();
			sb.append(this.mGcm.getGuestPath(this.mGcm.getGuestWinePrefixPath()));
			sb.append("/dosdevices/d:");
			sb.append(cutRootPrefixFromPath);
			String sb2 = sb.toString();
			List<String> list = this.mExeArgv;
			StringBuilder sb3 = new StringBuilder();
			sb3.append("eval \"wine '");
			sb3.append(sb2);
			sb3.append("'\"");
			list.addAll(Arrays.asList(new String[]{"/bin/bash", "-x", getRecipeGuestPath(access.mInstallScriptName), sb3.toString()}));
			if (this.mCont == null || this.mCont.mConfig.getDefaultControlsNotShortcut()) {
				this.mForceUseDefaultContols = true;
			}
			if (this.mCont == null || this.mCont.mConfig.getDefaultResolutionNotShortcut()) {
				this.mForceUseDefaultResolution = true;
			}
		} else if (option instanceof RunExplorer) {
			this.mCont = ((RunExplorer) option).mCont;
			this.mExeWorkDir = new File(mUserAreaDir.getAbsolutePath());
			this.mExeArgv.addAll(Arrays.asList(new String[]{getRecipeGuestPath("run/simple.sh"), "eval \"wine /opt/exec_wrapper.exe /opt/TFM.exe D:/\""}));
			if (this.mCont.mConfig.getDefaultControlsNotShortcut()) {
				this.mForceUseDefaultContols = true;
			}
			if (this.mCont.mConfig.getDefaultResolutionNotShortcut()) {
				this.mForceUseDefaultResolution = true;
			}
		} else if (option instanceof RunXDGLink) {
			RunXDGLink runXDGLink = (RunXDGLink) option;
			XDGLink access = runXDGLink.mLink;
			this.mCont = access.guestCont;
			this.mExeWorkDir = new File(access.path == null ? mUserAreaDir.getAbsolutePath() : this.mGcm.getHostPath(access.path));
			List<String> list = this.mExeArgv;
			StringBuilder sb = new StringBuilder();
			sb.append(this.mGcm.getGuestPath(this.mGcm.getGuestWinePrefixPath()));
			sb.append("/");
			sb.append(GuestContainersManager.LOCAL_RUN_SCRIPT);
			StringBuilder sb2 = new StringBuilder();
			sb2.append("eval \"");
			sb2.append(access.exec);
			sb2.append(" ");
			sb2.append(this.mCont.mConfig.getRunArguments());
			sb2.append("\"");
			list.addAll(Arrays.asList(new String[]{sb.toString(), sb2.toString()}));
			if (this.mCont.mConfig.getHideTaskbarOnShortcut()) {
				this.mHideTaskbar = true;
			}
		} else if (option instanceof RunLinuxCommand) {
			RunLinuxCommand runLinuxCommand = (RunLinuxCommand) option;
			this.mCont = runLinuxCommand.mCont;
			this.mExeWorkDir = new File(mUserAreaDir.getAbsolutePath());
			this.mExeArgv.addAll(Arrays.asList(runLinuxCommand.mCommand));

			this.mHideXServerImage = true;
			if (this.mCont.mConfig.getDefaultControlsNotShortcut()) {
				this.mForceUseDefaultContols = true;
			}
			if (this.mCont.mConfig.getDefaultResolutionNotShortcut()) {
				this.mForceUseDefaultResolution = true;
			}
		}
	}

    public void execute() {
        if (this.mCont == null) {
            this.mCont = this.mGcm.createContainer();
        }
        this.mGcm.makeContainerActive(this.mCont);
        if (this.mScreenInfo != null) {
            this.mCont.mConfig.setScreenInfo(this.mScreenInfo);
        } else {
            this.mScreenInfo = this.mCont.mConfig.getScreenInfo();
        }
        if (this.mControls != null) {
            this.mCont.mConfig.setControls(this.mControls);
            if (!this.mControls.getId().equals("default")) {
                this.mCont.mConfig.setHideTaskbarOnShortcut(true);
            }
        } else {
            this.mControls = this.mCont.mConfig.getControls();
        }
        if (this.mLocaleName != null) {
            this.mCont.mConfig.setLocaleName(this.mLocaleName);
        } else {
            this.mLocaleName = this.mCont.mConfig.getLocaleName();
        }
        if (this.mRunArguments != null) {
            this.mCont.mConfig.setRunArguments(this.mRunArguments);
        }
        if (this.mRunGuide != null) {
            this.mCont.mConfig.setRunGuide(this.mRunGuide);
        }
        if (this.mContStartupActions != null) {
            this.mCont.mConfig.setStartupActions(this.mContStartupActions);
        } else {
            this.mContStartupActions = this.mCont.mConfig.getStartupActions();
        }
        if (this.mRunScriptToCopy != null) {
            try {
                FileUtils.copyFile(this.mRunScriptToCopy, new File(this.mCont.mWinePrefixPath, GuestContainersManager.LOCAL_RUN_SCRIPT));
            } catch (IOException unused) {
                return;
            }
        }
        String str = "/home/xdroid/";
        String guestPath = this.mGcm.getGuestPath(this.mGcm.getGuestWinePrefixPath());
        if (this.mForceUseDefaultContols) {
            this.mControls = Controls.getDefault();
        }
        if (this.mForceUseDefaultResolution) {
            this.mScreenInfo = setScreenInfoDefaultResolution(this.mScreenInfo);
        }
        String str2 = (String) this.mExeArgv.get(this.mExeArgv.size() - 1);
        if (str2.contains("wine ")) {
            String wineOptions = getWineOptions(this.mScreenInfo, this.mHideTaskbar);
            int indexOf = str2.indexOf("wine ") + "wine ".length();
            List<String> list = this.mExeArgv;
            int size = this.mExeArgv.size() - 1;
            StringBuilder sb = new StringBuilder();
            sb.append(str2.substring(0, indexOf));
            sb.append(wineOptions);
            sb.append(" ");
            sb.append(str2.substring(indexOf));
            list.set(size, sb.toString());
        }
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("screenInfo = ");
        sb2.append(this.mScreenInfo);
        Log.i(str3, sb2.toString());
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("controls = ");
        sb3.append(this.mControls);
        Log.i(str4, sb3.toString());
        String str5 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("winePrefix = ");
        sb4.append(guestPath);
        Log.i(str5, sb4.toString());
        String str6 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("exeWorkingDir = ");
        sb5.append(this.mExeWorkDir);
        Log.i(str6, sb5.toString());
        String str7 = TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("exeArgv = ");
        sb6.append(this.mExeArgv);
        Log.i(str7, sb6.toString());
        final ArrayList arrayList = new ArrayList();
        if (this.mContStartupActions != null && !this.mContStartupActions.isEmpty()) {
            arrayList.add(new ContainerStartupAction(this.mCont, this.mContStartupActions));
        }
        arrayList.add(new PrepareGuestImage(str, mUserAreaDir));
        final EnvironmentCustomisationParameters environmentCustomisationParameters = new EnvironmentCustomisationParameters();
        environmentCustomisationParameters.setScreenInfo(this.mScreenInfo);
        environmentCustomisationParameters.setLocaleName(this.mLocaleName);
        UiThread.post(new Runnable() {
            public void run() {
                ((SelectedExecutableFileAware) ((ApplicationStateBase) StartGuest.this.getApplicationState())).setSelectedExecutableFile(new DetectedExecutableFile(environmentCustomisationParameters, StartGuest.this.mControls.getId(), StartGuest.this.mControls.createInfoDialog()));
                ((XServerDisplayActivityConfigurationAware) ((ApplicationStateBase) StartGuest.this.getApplicationState())).setXServerDisplayActivityInterfaceOverlay(StartGuest.this.mControls.create());
            }
        });
        arrayList.add(new CreateTypicalEnvironmentConfiguration(12, false));
        List<String> list2 = this.mEnv;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("HOME=");
        sb7.append(str);
        StringBuilder sb8 = new StringBuilder();
        sb8.append("WINEPREFIX=");
        sb8.append(guestPath);
        list2.addAll(Arrays.asList(new String[]{sb7.toString(), sb8.toString()}));
        CreateLaunchConfiguration createLaunchConfiguration = new CreateLaunchConfiguration(this.mExeWorkDir, guestPath, (String[]) this.mExeArgv.toArray(new String[0]), (String[]) this.mEnv.toArray(new String[0]), EDStartupActivity.SOCKET_PATH_SUFFIX, mUserAreaDir.getAbsolutePath());
        arrayList.add(createLaunchConfiguration);
        arrayList.add(new StartEnvironmentService(new TrayConfiguration(R.drawable.tray, R.string.host_app_name, R.string.host_app_name)));
        arrayList.add(new StartGuestApplication(true, true));
        String guestImagePath = this.mGcm.getGuestImagePath();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(str);
        sb9.append(".ed_progress");
        arrayList.add(new WaitForXClientConnection(new File(guestImagePath, sb9.toString()).getAbsolutePath(), this.mHideXServerImage));
        UiThread.post(new Runnable() {
            public void run() {
                ((ApplicationStateBase) StartGuest.this.getApplicationState()).getStartupActionsCollection().addActions(arrayList);
                StartGuest.this.sendDone();
            }
        });
    }

    private static String getWineOptions(ScreenInfo screenInfo, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("explorer ");
        String str = "/desktop=%s,%dx%d";
        Object[] objArr = new Object[3];
        objArr[0] = z ? "xdroid" : "shell";
        objArr[1] = Integer.valueOf(screenInfo.widthInPixels);
        objArr[2] = Integer.valueOf(screenInfo.heightInPixels);
        sb.append(String.format(str, objArr));
        return sb.toString();
    }

    private static ScreenInfo setScreenInfoDefaultResolution(ScreenInfo screenInfo) {
        int[] defaultScreenSize = GuestContainerConfig.getDefaultScreenSize();
        int i = defaultScreenSize[0];
        int i2 = defaultScreenSize[1];
        ScreenInfo screenInfo2 = new ScreenInfo(i, i2, i / 10, i2 / 10, screenInfo.depth);
        return screenInfo2;
    }

    public static String getRecipeGuestPath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(GuestContainersManager.RECIPES_GUEST_DIR);
        sb.append(str);
        return sb.toString();
    }
}
