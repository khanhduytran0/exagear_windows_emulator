package com.eltechs.axs.environmentService.components;

import com.eltechs.axs.ExagearImageConfiguration.ExagearImage;
import com.eltechs.axs.Globals;
import com.eltechs.axs.NetworkStateListener;
import com.eltechs.axs.NetworkStateListener.OnNetworkStateChangedListener;
import com.eltechs.axs.environmentService.EnvironmentComponent;
import com.eltechs.axs.helpers.Assert;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class EtcHostsFileUpdaterComponent extends EnvironmentComponent {
    /* access modifiers changed from: private */
    public final ExagearImage exagearImage;
    private transient NetworkStateListener listener = null;

    public EtcHostsFileUpdaterComponent(ExagearImage exagearImage2) {
        this.exagearImage = exagearImage2;
        Assert.isTrue(exagearImage2 != null);
    }

    public void start() {
        this.listener = new NetworkStateListener(Globals.getAppContext(), new OnNetworkStateChangedListener() {
            public void onNetworkStateChanged(String str) {
                File file = new File(EtcHostsFileUpdaterComponent.this.exagearImage.getPath(), "etc/hosts");
                Assert.isTrue(file.exists() && file.isFile());
                Assert.isTrue(file.canRead() && file.canWrite());
                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    printWriter.printf("%s\t%s\n", new Object[]{str, "localhost"});
                    printWriter.close();
                } catch (FileNotFoundException unused) {
                    Assert.unreachable();
                }
            }
        });
        this.listener.start();
    }

    public void stop() {
        this.listener.stop();
        this.listener = null;
    }
}
