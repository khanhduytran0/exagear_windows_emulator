package com.eltechs.axs.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.eltechs.axs.Globals;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class AndroidHelpers {
    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) Globals.getAppContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int dpToPx(int i) {
        return (int) ((((float) i) * Globals.getAppContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static void toggleSoftInput() {
        ((InputMethodManager) Globals.getAppContext().getSystemService("input_method")).toggleSoftInput(2, 0);
    }

    public static FileInputStream openPrivateFileForReading(String str) throws FileNotFoundException {
        return Globals.getAppContext().openFileInput(str);
    }

    public static FileOutputStream openPrivateFileForWriting(String str) throws FileNotFoundException {
        return Globals.getAppContext().openFileOutput(str, 0);
    }

    public static void deletePrivateFile(String str) {
        Globals.getAppContext().deleteFile(str);
    }

    public static File getExternalFilesDirectory(Context context, String str) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir == null) {
            externalFilesDir = context.getFilesDir();
        }
        Assert.notNull(externalFilesDir);
        return new File(externalFilesDir, str);
    }

    public static File getInternalFilesDirectory(Context context, String str) {
        return new File(context.getFilesDir(), str);
    }

    public static File getMainSDCard() {
        return Environment.getExternalStorageDirectory();
    }

    public static Class<? extends Activity> getAppLaunchActivityClass(Context context) {
        Class<? extends Activity> cls;
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntentForPackage == null) {
            return null;
        }
        try {
            cls = (Class) Class.forName(launchIntentForPackage.getComponent().getClassName());
        } catch (ClassNotFoundException unused) {
            cls = null;
        }
        return cls;
    }

    public static String getString(int i) {
        return Globals.getAppContext().getResources().getString(i);
    }

    public static byte[] getAssetAsByteArray(Context context, String str) throws IOException {
        InputStream open = context.getAssets().open(str);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOStreamHelpers.copy(open, byteArrayOutputStream);
        open.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static void toast(String str) {
        Toast.makeText(Globals.getAppContext(), str, 1).show();
    }

    public static void toastShort(String str) {
        Toast.makeText(Globals.getAppContext(), str, 0).show();
    }
}
