package com.eltechs.ed.guestContainers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.ed.Locales;
import com.eltechs.ed.controls.Controls;
import java.io.File;

public class GuestContainerConfig {
    public static final String CONTAINER_CONFIG_FILE_KEY_PREFIX = "com.eltechs.ed.CONTAINER_CONFIG_";
    public static final String KEY_CONTROLS = "CONTROLS";
    public static final String KEY_DEFAULT_CONTROLS_NOT_SHORTCUT = "DEFAULT_CONTROLS_NOT_SHORTCUT";
    public static final String KEY_DEFAULT_RESOLUTION_NOT_SHORTCUT = "DEFAULT_RESOLUTION_NOT_SHORTCUT";
    public static final String KEY_HIDE_TASKBAR_SHORTCUT = "HIDE_TASKBAR_SHORTCUT";
    public static final String KEY_LOCALE_NAME = "LOCALE_NAME";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_RUN_ARGUMENTS = "RUN_ARGUMENTS";
    public static final String KEY_RUN_GUIDE = "RUN_GUIDE";
    public static final String KEY_RUN_GUIDE_SHOWN = "RUN_GUIDE_SHOWN";
    public static final String KEY_SCREEN_COLOR_DEPTH = "SCREEN_COLOR_DEPTH";
    public static final String KEY_SCREEN_SIZE = "SCREEN_SIZE";
    public static final String KEY_STARTUP_ACTIONS = "STARTUP_ACTIONS";
    private static final String[] SUPPORTED_RESOLUTIONS = {"640,480", "800,600", "1024,768", "1280,720", "1280,1024", "1366,768", "1600,900", "1920,1080"};
    private GuestContainer mCont;
    private Context mContext;
    private SharedPreferences mSp;

    public GuestContainerConfig(Context context, GuestContainer guestContainer) {
        this.mContext = context;
        StringBuilder sb = new StringBuilder();
        sb.append(CONTAINER_CONFIG_FILE_KEY_PREFIX);
        sb.append(guestContainer.mId);
        this.mSp = context.getSharedPreferences(sb.toString(), 0);
        this.mCont = guestContainer;
    }

    static void cloneContainerConfig(GuestContainer guestContainer, GuestContainer guestContainer2) {
        GuestContainerConfig guestContainerConfig = guestContainer2.mConfig;
        StringBuilder sb = new StringBuilder();
        sb.append("Container_");
        sb.append(guestContainer2.mId);
        guestContainerConfig.setName(sb.toString());
        guestContainer2.mConfig.setScreenInfo(guestContainer.mConfig.getScreenInfo());
        guestContainer2.mConfig.setLocaleName(guestContainer.mConfig.getLocaleName());
        guestContainer2.mConfig.setRunArguments(guestContainer.mConfig.getRunArguments());
        guestContainer2.mConfig.setControls(guestContainer.mConfig.getControls());
        guestContainer2.mConfig.setHideTaskbarOnShortcut(guestContainer.mConfig.getHideTaskbarOnShortcut());
        guestContainer2.mConfig.setDefaultControlsNotShortcut(guestContainer.mConfig.getDefaultControlsNotShortcut());
        guestContainer2.mConfig.setDefaultResolutionNotShortcut(guestContainer.mConfig.getDefaultResolutionNotShortcut());
        guestContainer2.mConfig.setStartupActions(guestContainer.mConfig.getStartupActions());
        guestContainer2.mConfig.setRunGuide(guestContainer.mConfig.getRunGuide());
        guestContainer2.mConfig.setRunGuideShown(guestContainer.mConfig.getRunGuideShown().booleanValue());
    }

    /* access modifiers changed from: 0000 */
    public void deleteConfig() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mContext.getFilesDir().getParent());
        sb.append("/shared_prefs/");
        sb.append(CONTAINER_CONFIG_FILE_KEY_PREFIX);
        sb.append(this.mCont.mId);
        sb.append(".xml");
        File file = new File(sb.toString());
        if (file.exists()) {
            Context context = this.mContext;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(CONTAINER_CONFIG_FILE_KEY_PREFIX);
            sb2.append(this.mCont.mId);
            context.getSharedPreferences(sb2.toString(), 0).edit().clear().commit();
            file.delete();
        }
    }

    /* access modifiers changed from: 0000 */
    public void loadDefaults() {
        StringBuilder sb = new StringBuilder();
        sb.append("Container_");
        sb.append(this.mCont.mId);
        setName(sb.toString());
        setScreenInfoDefault();
        setLocaleName(Locales.getLocaleByDevice(this.mContext));
        setRunArguments("");
        setControls(Controls.getDefault());
        setHideTaskbarOnShortcut(false);
        setDefaultControlsNotShortcut(true);
        setDefaultResolutionNotShortcut(true);
        setStartupActions("");
        setRunGuide("");
        setRunGuideShown(false);
    }

    public String getName() {
        SharedPreferences sharedPreferences = this.mSp;
        String str = KEY_NAME;
        StringBuilder sb = new StringBuilder();
        sb.append("Container_");
        sb.append(this.mCont.mId);
        return sharedPreferences.getString(str, sb.toString());
    }

    private void setName(String str) {
        this.mSp.edit().putString(KEY_NAME, str).apply();
    }

    public ScreenInfo getScreenInfo() {
        int parseInt;
        int parseInt2;
        if (!this.mSp.contains(KEY_SCREEN_SIZE) || !this.mSp.contains(KEY_SCREEN_COLOR_DEPTH)) {
            return null;
        }
        String string = this.mSp.getString(KEY_SCREEN_SIZE, "default");
        if (string.equals("default")) {
            int[] defaultScreenSize = getDefaultScreenSize();
            parseInt = defaultScreenSize[0];
            parseInt2 = defaultScreenSize[1];
        } else {
            String[] split = string.split(",");
            parseInt = Integer.parseInt(split[0]);
            parseInt2 = Integer.parseInt(split[1]);
        }
        int i = parseInt2;
        int i2 = parseInt;
        ScreenInfo screenInfo = new ScreenInfo(i2, i, i2 / 10, i / 10, Integer.parseInt(this.mSp.getString(KEY_SCREEN_COLOR_DEPTH, "16")));
        return screenInfo;
    }

    public void setScreenInfo(ScreenInfo screenInfo) {
        Editor edit = this.mSp.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(screenInfo.widthInPixels));
        sb.append(",");
        sb.append(screenInfo.heightInPixels);
        String sb2 = sb.toString();
        String[] strArr = SUPPORTED_RESOLUTIONS;
        boolean z = false;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (sb2.equals(strArr[i])) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            String str = KEY_SCREEN_SIZE;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(Integer.toString(screenInfo.widthInPixels));
            sb3.append(",");
            sb3.append(screenInfo.heightInPixels);
            edit.putString(str, sb3.toString());
        } else {
            edit.putString(KEY_SCREEN_SIZE, "default");
        }
        edit.putString(KEY_SCREEN_COLOR_DEPTH, Integer.toString(screenInfo.depth));
        edit.apply();
    }

    private void setScreenInfoDefault() {
        Editor edit = this.mSp.edit();
        edit.putString(KEY_SCREEN_SIZE, "default");
        edit.putString(KEY_SCREEN_COLOR_DEPTH, Integer.toString(16));
        edit.apply();
    }

    public String getLocaleName() {
        return this.mSp.getString(KEY_LOCALE_NAME, Locales.getLocaleByDevice(this.mContext));
    }

    public void setLocaleName(String str) {
        this.mSp.edit().putString(KEY_LOCALE_NAME, str).apply();
    }

    public String getRunArguments() {
        return this.mSp.getString(KEY_RUN_ARGUMENTS, "");
    }

    public void setRunArguments(String str) {
        this.mSp.edit().putString(KEY_RUN_ARGUMENTS, str).apply();
    }

    public Controls getControls() {
        String string = this.mSp.getString(KEY_CONTROLS, null);
        if (string == null) {
            return Controls.getDefault();
        }
        Controls find = Controls.find(string);
        if (find != null) {
            return find;
        }
        setControls(Controls.getDefault());
        return Controls.getDefault();
    }

    public void setControls(Controls controls) {
        this.mSp.edit().putString(KEY_CONTROLS, controls.getId()).apply();
    }

    public boolean getHideTaskbarOnShortcut() {
        return this.mSp.getBoolean(KEY_HIDE_TASKBAR_SHORTCUT, false);
    }

    public void setHideTaskbarOnShortcut(boolean z) {
        this.mSp.edit().putBoolean(KEY_HIDE_TASKBAR_SHORTCUT, z).apply();
    }

    public boolean getDefaultControlsNotShortcut() {
        return this.mSp.getBoolean(KEY_DEFAULT_CONTROLS_NOT_SHORTCUT, true);
    }

    public void setDefaultControlsNotShortcut(boolean z) {
        this.mSp.edit().putBoolean(KEY_DEFAULT_CONTROLS_NOT_SHORTCUT, z).apply();
    }

    public boolean getDefaultResolutionNotShortcut() {
        return this.mSp.getBoolean(KEY_DEFAULT_RESOLUTION_NOT_SHORTCUT, true);
    }

    public void setDefaultResolutionNotShortcut(boolean z) {
        this.mSp.edit().putBoolean(KEY_DEFAULT_RESOLUTION_NOT_SHORTCUT, z).apply();
    }

    public String getStartupActions() {
        return this.mSp.getString(KEY_STARTUP_ACTIONS, "");
    }

    public void setStartupActions(String str) {
        this.mSp.edit().putString(KEY_STARTUP_ACTIONS, str).apply();
    }

    public String getRunGuide() {
        return this.mSp.getString(KEY_RUN_GUIDE, "");
    }

    public void setRunGuide(String str) {
        this.mSp.edit().putString(KEY_RUN_GUIDE, str).apply();
    }

    public Boolean getRunGuideShown() {
        return Boolean.valueOf(this.mSp.getBoolean(KEY_RUN_GUIDE_SHOWN, false));
    }

    public void setRunGuideShown(boolean z) {
        this.mSp.edit().putBoolean(KEY_RUN_GUIDE_SHOWN, z).apply();
    }

    public static int[] getDefaultScreenSize() {
        DisplayMetrics displayMetrics = AndroidHelpers.getDisplayMetrics();
        int i = (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
        int i2 = (int) (((float) displayMetrics.heightPixels) / displayMetrics.density);
        int max = Math.max(i, i2);
        int min = Math.min(i, i2);
        return new int[]{Math.max(max, 800), Math.max(min, 600)};
    }
}
