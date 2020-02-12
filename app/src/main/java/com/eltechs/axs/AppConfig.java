package com.eltechs.axs;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AppConfig {
    private static final String CONFIG_FILE_KEY = "com.eltechs.axs.CONFIG";
    private static volatile AppConfig mInstance;
    private SharedPreferences sp;

    public static synchronized AppConfig getInstance(Context context) {
        AppConfig appConfig;
        synchronized (AppConfig.class) {
            if (mInstance == null) {
                mInstance = new AppConfig();
                mInstance.sp = context.getSharedPreferences(CONFIG_FILE_KEY, 0);
            }
            appConfig = mInstance;
        }
        return appConfig;
    }

    public boolean isRunAfterNotification() {
        return this.sp.getBoolean("RUN_AFTER_NOTIFICATION", false);
    }

    public void setRunAfterNotification(boolean z) {
        this.sp.edit().putBoolean("RUN_AFTER_NOTIFICATION", z).apply();
    }

    public String getNotificationName() {
        return this.sp.getString("NOTIFICATION_NAME", null);
    }

    public void setNotificationName(String str) {
        this.sp.edit().putString("NOTIFICATION_NAME", str).apply();
    }

    public Date getFirstRunTime() {
        return new Date(this.sp.getLong("FIRST_RUN_TIME", 0));
    }

    public void setFirstRunTime(Date date) {
        this.sp.edit().putLong("FIRST_RUN_TIME", date.getTime()).apply();
    }

    public Date getLastSessionTime() {
        return new Date(this.sp.getLong("LAST_SESSION_TIME", 0));
    }

    public void setLastSessionTime(Date date) {
        this.sp.edit().putLong("LAST_SESSION_TIME", date.getTime()).apply();
    }

    public Date getExeFoundTime() {
        return new Date(this.sp.getLong("EXE_FOUND_TIME", 0));
    }

    public void setExeFoundTime(Date date) {
        this.sp.edit().putLong("EXE_FOUND_TIME", date.getTime()).apply();
    }

    public Set<String> getComlpetedRemindActions() {
        return this.sp.getStringSet("COMLPETED_REMIND_ACTIONS", new HashSet());
    }

    public void setCompletedRemindActions(Set<String> set) {
        this.sp.edit().putStringSet("COMLPETED_REMIND_ACTIONS", set).apply();
    }

    public boolean isXServerFirstConnectDone() {
        return this.sp.getBoolean("XSERVER_FIRST_CONNECT", false);
    }

    public void setXServerFirstConnectDone(boolean z) {
        this.sp.edit().putBoolean("XSERVER_FIRST_CONNECT", z).apply();
    }

    public Long getCurrentGuestContId() {
        return Long.valueOf(this.sp.getLong("CURRENT_GUEST_CONT_ID", 0));
    }

    public void setCurrentGuestContId(Long l) {
        this.sp.edit().putLong("CURRENT_GUEST_CONT_ID", l.longValue()).apply();
    }

    public Integer getEDMainOnStartAction() {
        return Integer.valueOf(this.sp.getInt("ED_MAIN_ON_START_ACTION", -1));
    }

    public void setEDMainOnStartAction(Integer num) {
        this.sp.edit().putInt("ED_MAIN_ON_START_ACTION", num.intValue()).apply();
    }

    public int getGuestLaunchesCount() {
        return this.sp.getInt("GUEST_LAUNCHES_COUNT", 0);
    }

    public void setGuestLaunchesCount(int i) {
        this.sp.edit().putInt("GUEST_LAUNCHES_COUNT", i).apply();
    }

    public boolean getRateAppDontShowAgain() {
        return this.sp.getBoolean("RATE_APP_DONT_SHOW_AGAIN", false);
    }

    public void setRateAppDontShowAgain(boolean z) {
        this.sp.edit().putBoolean("RATE_APP_DONT_SHOW_AGAIN", z).apply();
    }

    public Date getRateAppLastShowTime() {
        return new Date(this.sp.getLong("RATE_APP_LAST_SHOW_TIME", 0));
    }

    public void setRateAppLastShowTime(Date date) {
        this.sp.edit().putLong("RATE_APP_LAST_SHOW_TIME", date.getTime()).apply();
    }

    public Set<String> getControlsWithInfoShown() {
        return this.sp.getStringSet("CONTROLS_WITH_INFO_SHOWN", new HashSet());
    }

    public void setControlsWithInfoShown(Set<String> set) {
        this.sp.edit().putStringSet("CONTROLS_WITH_INFO_SHOWN", set).apply();
    }
}
