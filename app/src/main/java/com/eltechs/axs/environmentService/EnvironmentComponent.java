package com.eltechs.axs.environmentService;

import com.eltechs.axs.helpers.Assert;
import java.io.IOException;

public abstract class EnvironmentComponent {
    private AXSEnvironment environment;

    public abstract void start() throws IOException;

    public abstract void stop();

    public final void addedToEnvironment(AXSEnvironment aXSEnvironment) {
        Assert.state(this.environment == null, "Already attached to an environment.");
        this.environment = aXSEnvironment;
    }

    /* access modifiers changed from: protected */
    public final AXSEnvironment getEnvironment() {
        return this.environment;
    }
}
