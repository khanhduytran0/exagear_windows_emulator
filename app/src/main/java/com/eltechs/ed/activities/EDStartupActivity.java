package com.eltechs.ed.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog.Builder;
import com.eltechs.axs.ExagearImageConfiguration.ExagearImage;
import com.eltechs.axs.activities.StartupActivity;
import com.eltechs.axs.configuration.MemsplitConfiguration;
import com.eltechs.axs.configuration.startup.StartupActionsCollection;
import com.eltechs.axs.configuration.startup.actions.RequestPermissions;
import com.eltechs.axs.configuration.startup.actions.UnpackExagearImageObb;
import com.eltechs.ed.EDApplicationState;
import com.eltechs.ed.R;
import com.eltechs.ed.startupActions.InitGuestContainersManager;
import com.eltechs.ed.startupActions.InstallRecipesFromAssets;
import com.eltechs.ed.startupActions.WDesktop;
import java.io.File;
import com.eltechs.axs.ExagearImageConfiguration.*;

public class EDStartupActivity extends StartupActivity<EDApplicationState> {
    // private static final String BASE64_APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAozYFhPvaURKRrOcUfVLV8l+M8A9tdU9NmOe8nzc9hNEtUyfTb9XDaClrojl54no1xJmMjCum/xmhd5qub9wclA5voW6D2gSD/R54Y/TAHlLVdhqdnN8eJnBaPw8eSe23DfXqG7S+/zjRbUuW56CPPgvCZCKrfiTi7b9x9WADf50egN/f2IkYhI/2n17ew80S5CyHSd+ZNNU03w72JDEOsCL2NLeWLKaJ5F+yxJ6Xzg9tutoTCNigOogLY+BdigFIITFz1EVBL+3POJDyKE6Sqt3dl+iCjWUA7MHBuYHd0bVo5NDbMMuMxtPFOhh31o89WNyOqKOHpSCsUxYGJOUHUwIDAQAB";
    private static final String GENERIC_IMAGE_DIRECTORY_NAME = "image";
    private static final String PROGRESS_FILE_NAME = "ed_progress";
    public static final String SOCKET_PATH_SUFFIX = "ed";

    public EDStartupActivity() {
        super(EDApplicationState.class);
    }

    public void finish() {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) "WARNING!!!");
        builder.setIcon((int) R.drawable.ic_warning_24dp);
        builder.setMessage((CharSequence) "Shutdown while startup in progress may corrupt application state!\n\nAre you sure you want to exit?");
        builder.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                StartupActivity.shutdownAXSApplication(true);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /* access modifiers changed from: protected */
    public void initialiseStartupActions() {
        EDApplicationState eDApplicationState = (EDApplicationState) getApplicationState();
        Context applicationContext = getApplicationContext();
        StartupActionsCollection startupActionsCollection = eDApplicationState.getStartupActionsCollection();
        eDApplicationState.setMemsplitConfiguration(new MemsplitConfiguration(true));
        eDApplicationState.setExagearImage(ExagearImage.find(applicationContext, GENERIC_IMAGE_DIRECTORY_NAME, true));
        startupActionsCollection.addAction(new RequestPermissions(this, StartupActivity.REQUEST_CODE_GET_PERMISSIONS));
        startupActionsCollection.addAction(new UnpackExagearImageObb(true, new String[]{"/home"}, new File(applicationContext.getFilesDir(), PROGRESS_FILE_NAME).getAbsolutePath()));
        startupActionsCollection.addAction(new InstallRecipesFromAssets());
        startupActionsCollection.addAction(new InitGuestContainersManager());
        startupActionsCollection.addAction(new WDesktop());
    }
}
