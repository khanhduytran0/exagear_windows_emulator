package com.eltechs.axs.xconnectors.epoll;

import java.io.File;

public class UnixSocketConfiguration {
    private boolean isAbstract;
    private String relPath;
    private String rootPath;

    private UnixSocketConfiguration(boolean z, String str, String str2) {
        this.isAbstract = z;
        this.rootPath = str;
        this.relPath = str2;
    }

    public boolean isAbstract() {
        return this.isAbstract;
    }

    public String getGuestPath() {
        return this.relPath;
    }

    public String getHostPath() {
        return new File(this.rootPath, this.relPath).getAbsolutePath();
    }

    public static UnixSocketConfiguration createAbstractSocket(String str) {
        return new UnixSocketConfiguration(true, "/", str);
    }

    public static UnixSocketConfiguration createRegularSocket(String str, String str2) {
        return new UnixSocketConfiguration(false, str, str2);
    }
}
