package com.eltechs.axs.configuration;

import com.eltechs.axs.network.SocketPaths;
import java.io.Serializable;

public class GuestApplicationsTrackerConfiguration implements Serializable {
    private String address = SocketPaths.GUEST_APPLICATIONS_TRACKER;

    public void setSocket(String str) {
        this.address = str;
    }

    public String getSocket() {
        return this.address;
    }
}
