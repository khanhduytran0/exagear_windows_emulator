package com.eltechs.axs.ExagearImageConfiguration;

import android.content.Context;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.FileHelpers;
import java.io.File;
import java.io.IOException;

public class ExagearImage {
    private final File path;

    private ExagearImage(File file) {
        this.path = file;
    }

    public static ExagearImage find(Context context, String str, boolean z) {
        if (z) {
            return new ExagearImage(AndroidHelpers.getInternalFilesDirectory(context, str));
        }
        return new ExagearImage(AndroidHelpers.getExternalFilesDirectory(context, str));
    }

    public File getPath() {
        return this.path;
    }

    public boolean isValid() {
        return this.path.isDirectory() && FileHelpers.doesFileExist(this.path, ExagearImagePaths.IMG_VERSION);
    }

    public int getImageVersion() {
        try {
            return Integer.parseInt((String) FileHelpers.readAsLines(new File(this.path, ExagearImagePaths.IMG_VERSION)).get(0));
        } catch (IOException | NumberFormatException e) {
			// MOD: Additional code for debugging.
			e.printStackTrace();
            return -1;
        }
    }

    public File getConfigurationDir() {
        return new File(this.path, ExagearImagePaths.DOT_EXAGEAR);
    }

    public File getVpathsList() {
        return new File(this.path, ExagearImagePaths.EXAGEAR_VPATHS_LIST);
    }
}
