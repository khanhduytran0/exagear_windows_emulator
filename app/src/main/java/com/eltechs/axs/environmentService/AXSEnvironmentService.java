package com.eltechs.axs.environmentService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.support.v4.app.NotificationCompat.Builder;
import com.eltechs.axs.Globals;
import com.eltechs.axs.activities.SwitchToAxsFromSystemTrayActivity;
import com.eltechs.axs.applicationState.EnvironmentAware;
import com.eltechs.axs.container.annotations.PreRemove;
import com.eltechs.axs.environmentService.AXSEnvironment.StartupCallback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AXSEnvironmentService extends Service {
    private final List<EnvironmentComponent> startedComponents = new ArrayList<EnvironmentComponent>();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
		StrictMode.setThreadPolicy(ThreadPolicy.LAX);
		StrictMode.setVmPolicy(VmPolicy.LAX);
		EnvironmentAware environmentAware = Globals.getApplicationState();
		StartupCallback startupCallback = environmentAware.getEnvironment().startupCallback;
        try {
			environmentAware.setEnvironmentServiceInstance(this);
            startEnvironmentComponents();
            startupCallback.serviceStarted();
            configureAsForegroundService();
            return 2;
        } catch (Throwable e) {
            stopSelf();
            environmentAware.setEnvironmentServiceInstance(null);
            startupCallback.serviceFailed(e);
            return 2;
        }
    }

    @PreRemove
    private void destroy() {
        shutdownComponents();
        stopSelf();
    }

    public void onDestroy() {
        shutdownComponents();
        super.onDestroy();
    }

    private AXSEnvironment getEnvironment() {
        return ((EnvironmentAware) Globals.getApplicationState()).getEnvironment();
    }

    private void startEnvironmentComponents() throws IOException {
        try {
            Iterator it = getEnvironment().iterator();
            while (it.hasNext()) {
                EnvironmentComponent environmentComponent = (EnvironmentComponent) it.next();
                environmentComponent.start();
                this.startedComponents.add(0, environmentComponent);
            }
        } catch (IOException e) {
            for (EnvironmentComponent stop : this.startedComponents) {
                stop.stop();
            }
            this.startedComponents.clear();
            throw e;
        }
    }

    private void shutdownComponents() {
        for (EnvironmentComponent stop : this.startedComponents) {
            stop.stop();
        }
        this.startedComponents.clear();
        EnvironmentAware environmentAware = Globals.getApplicationState();
        if (environmentAware != null) {
            environmentAware.setEnvironmentServiceInstance(null);
        }
    }

    private void configureAsForegroundService() {
        TrayConfiguration trayConfiguration = getEnvironment().trayConfiguration;
        Intent intent = new Intent(this, SwitchToAxsFromSystemTrayActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
		/*
        if (VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("notification_channel_id", "ExaGear", 1));
        }
		*/
        Notification build = new Builder(this, "notification_channel_id").setSmallIcon(trayConfiguration.getTrayIcon()).setContentText(getResources().getText(trayConfiguration.getReturnToDescription())).setContentTitle(getResources().getText(trayConfiguration.getTrayIconName())).setContentIntent(PendingIntent.getActivity(this, 0, intent, 0)).build();
        notificationManager.notify(1, build);
        startForeground(1, build);
    }
}
