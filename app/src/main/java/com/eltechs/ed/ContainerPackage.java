package com.eltechs.ed;

import java.util.Arrays;
import java.util.List;

public class ContainerPackage {
    public static final List<ContainerPackage> LIST = Arrays.asList(new ContainerPackage[]{new ContainerPackage("Core Fonts", "corefonts"), new ContainerPackage("Tahoma Font", "tahoma"), new ContainerPackage("MS .NET 2.0", "dotnet20"), new ContainerPackage("MS Jet 4.0 (+ MS DAC 2.7)", "jet40")});
    public final String mDisplayName;
    public final String mName;

    public ContainerPackage(String str, String str2) {
        this.mDisplayName = str;
        this.mName = str2;
    }

    public String toString() {
        return this.mDisplayName;
    }
}
