package com.eltechs.ed.startupActions;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.eltechs.axs.helpers.IOStreamHelpers;
import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;
import com.eltechs.axs.helpers.SafeFileHelpers;
import com.eltechs.axs.helpers.ZipInstallerAssets;
import com.eltechs.axs.helpers.ZipInstallerAssets.InstallCallback;
import com.eltechs.ed.guestContainers.GuestContainersManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InstallRecipesFromAssets<StateClass extends ExagearImageAware> extends AbstractStartupAction<StateClass> {
    public void execute() {
        final File file = new File(((ExagearImageAware) getApplicationState()).getExagearImage().getPath(), GuestContainersManager.RECIPES_GUEST_DIR);
        try {
            SafeFileHelpers.removeDirectory(file);
            
			new AsyncTask<Void, Void, Void>(){
				@Override
				protected Void doInBackground(Void[] p1)
				{
					copyFolderFromAsset("recipe", file);
					return null;
				}
				
				@Override
				public void onPostExecute(Void result) {
					super.onPostExecute(result);
					try {
						Runtime.getRuntime().exec("chmod -R 700 " + file.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					InstallRecipesFromAssets.this.sendDone();
				}
			}.execute();
        } catch (IOException e) {
            sendError(e.toString());
        }
    }
	
	private void copyFolderFromAsset(String itemAsset, File outputFile) {
		try {
			AssetManager asset = getAppContext().getAssets();
			String[] itemList = asset.list(itemAsset);
			if (itemList.length == 0) {
				outputFile.getParentFile().mkdir();
				outputFile.createNewFile();
				IOStreamHelpers.copy(asset.open(itemAsset), new FileOutputStream(outputFile));
			} else {
				for (String item : itemList) {
					copyFolderFromAsset(itemAsset + "/" + item, new File(outputFile, item));
				}
			}
		} catch (Throwable th) {
			throw new RuntimeException("Failed copying recipe folder", th);
		}
	}
}
