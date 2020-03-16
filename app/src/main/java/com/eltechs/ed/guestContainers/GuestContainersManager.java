package com.eltechs.ed.guestContainers;

import android.content.Context;
import android.util.LongSparseArray;
import com.eltechs.axs.AppConfig;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.FileHelpers;
import com.eltechs.axs.helpers.SafeFileHelpers;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.ed.WineRegistryEditor;
import com.eltechs.ed.XDGLink;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import com.eltechs.axs.activities.*;
import android.util.*;

public class GuestContainersManager {
    private static final String CONTAINER_DESKTOP_DIR = ".wine/drive_c/users/xdroid/Desktop/";
    private static final String CONTAINER_DIR_PREFIX = "xdroid_";
    private static final String CONTAINER_ICONS_32x32_DIR = ".local/share/icons/hicolor/32x32/apps/";
    private static final String CONTAINER_PATTERN_GUEST_DIR = "/opt/guestcont-pattern/";
    private static final String CONTAINER_START_MENU_DIR = ".local/share/applications/wine/Programs/";
    public static final String LOCAL_RUN_SCRIPT = "run.sh";
    private static final String NOTEPAD_GUEST_PATH = "/opt/AkelPad.exe";
    public static final String RECIPES_GUEST_DIR = "/opt/recipe/";
    public static final String TAG = "GuestContainersManager";
    private static volatile GuestContainersManager mInstance;
    private LongSparseArray<GuestContainer> mContainers;
    private Context mContext;
    private File mHomeDir;
    private File mImageDir;
    private Long mMaxContainerId;

    public GuestContainersManager(Context context) {
        this.mContext = context;
        this.mImageDir = new File(context.getFilesDir(), "image");
		this.mHomeDir = new File(this.mImageDir, "home");
        makeContainersList();
        convertFromOldVersion();
    }

    public static synchronized GuestContainersManager getInstance(Context context) {
        GuestContainersManager guestContainersManager;
        synchronized (GuestContainersManager.class) {
            if (mInstance == null) {
                mInstance = new GuestContainersManager(context);
            }
            guestContainersManager = mInstance;
        }
        return guestContainersManager;
    }

    public List<GuestContainer> getContainersList() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mContainers.size(); i++) {
            arrayList.add(this.mContainers.valueAt(i));
        }
        return arrayList;
    }

    public GuestContainer getContainerById(Long l) {
        return (GuestContainer) this.mContainers.get(l.longValue());
    }

    public String getHostPath(String str) {
        return new File(this.mImageDir, str).getAbsolutePath();
    }

    public String getGuestPath(String str) {
        return FileHelpers.cutRootPrefixFromPath(new File(str), this.mImageDir);
    }

    public String getGuestImagePath() {
        return this.mImageDir.getAbsolutePath();
    }

    public String getGuestWinePrefixPath() {
        return new File(this.mHomeDir, "xdroid/.wine").getAbsolutePath();
    }

    private void fillContainerInfo(GuestContainer guestContainer) {
        StringBuilder sb = new StringBuilder();
        sb.append(guestContainer.mPath);
        sb.append("/.wine");
        guestContainer.mWinePrefixPath = sb.toString();
        guestContainer.mDesktopPath = new File(guestContainer.mPath, CONTAINER_DESKTOP_DIR).getAbsolutePath();
        guestContainer.mStartMenuPath = new File(guestContainer.mPath, CONTAINER_START_MENU_DIR).getAbsolutePath();
        guestContainer.mIconsPath = new File(guestContainer.mPath, CONTAINER_ICONS_32x32_DIR).getAbsolutePath();
        guestContainer.mConfig = new GuestContainerConfig(this.mContext, guestContainer);
    }

    private void makeContainersList() {
		this.mContainers = new LongSparseArray<GuestContainer>();
		this.mMaxContainerId = Long.valueOf(0);

		File[] listFiles = this.mHomeDir.listFiles();
		
		for (File file : listFiles) {
			if (file.isDirectory()) {
				if (file.getName().startsWith(CONTAINER_DIR_PREFIX)) {
					GuestContainer guestContainer = new GuestContainer();
					Long valueOf = Long.valueOf(Long.parseLong(file.getName().replace(CONTAINER_DIR_PREFIX, "")));
					guestContainer.mId = valueOf;
					guestContainer.mPath = file.getAbsolutePath();

					fillContainerInfo(guestContainer);

					this.mContainers.append(valueOf.longValue(), guestContainer);

					if (valueOf.longValue() > this.mMaxContainerId.longValue()) {
						this.mMaxContainerId = valueOf;
					}
				}
			}
		}
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:12|13|14|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        org.apache.commons.io.FileUtils.deleteDirectory(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0055, code lost:
        return false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0051 */
    private boolean initNewContainer(GuestContainer guestContainer, GuestContainer guestContainer2) {
        try {
			File file;
			if (guestContainer2 != null) {
				file = new File(guestContainer2.mPath);
			} else {
				file = new File(this.mImageDir, CONTAINER_PATTERN_GUEST_DIR);
			}
			fillContainerInfo(guestContainer);
			File file2 = new File(guestContainer.mPath);
			FileUtils.copyDirectory(file, file2, new FileFilter() {
					public boolean accept(File file) {
						return !SafeFileHelpers.isSymlink(file.getAbsolutePath());
					}
				}, true);
			File file3 = new File(guestContainer.mWinePrefixPath, LOCAL_RUN_SCRIPT);
			if (!file3.exists()) {
				FileUtils.copyFile(new File(getHostPath(RECIPES_GUEST_DIR), "run/simple.sh"), file3);
			}
			if (guestContainer2 == null) {
				guestContainer.mConfig.loadDefaults();
			} else {
				GuestContainerConfig.cloneContainerConfig(guestContainer2, guestContainer);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			// FileUtils.deleteDirectory(r1);
		}
		return false;
	}

    public GuestContainer createContainer() {
        return createContainer(null);
    }

    public GuestContainer createContainer(GuestContainer guestContainer) {
        Long valueOf = Long.valueOf(this.mMaxContainerId.longValue() + 1);
        // File file = this.mHomeDir;
        StringBuilder sb = new StringBuilder();
        sb.append(CONTAINER_DIR_PREFIX);
        sb.append(valueOf);
        File file2 = new File(mHomeDir, sb.toString());
        if (!file2.mkdirs()) {
			return null;
        }
        GuestContainer guestContainer2 = new GuestContainer();
        guestContainer2.mId = valueOf;
        guestContainer2.mPath = file2.getAbsolutePath();
        if (!initNewContainer(guestContainer2, guestContainer)) {
            return null;
        }
        // Long l = this.mMaxContainerId;
        this.mMaxContainerId = Long.valueOf(this.mMaxContainerId.longValue() + 1);
        this.mContainers.append(valueOf.longValue(), guestContainer2);
        return guestContainer2;
    }

    public void deleteContainer(GuestContainer guestContainer) {
        if (getCurrentContainer() == guestContainer) {
            makeContainerCurrent(null);
        }
        guestContainer.mConfig.deleteConfig();
        this.mContainers.delete(guestContainer.mId.longValue());
        File file = new File(guestContainer.mPath);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException unused) {
            String parent = file.getParent();
            StringBuilder sb = new StringBuilder();
            sb.append("corrupted_");
            sb.append(file.getName());
            file.renameTo(new File(parent, sb.toString()));
        }
    }

    public void cloneContainer(GuestContainer guestContainer) {
        createContainer(guestContainer);
    }

    public GuestContainer getCurrentContainer() {
        Long currentGuestContId = AppConfig.getInstance(this.mContext).getCurrentGuestContId();
        if (currentGuestContId.longValue() != 0) {
            return (GuestContainer) this.mContainers.get(currentGuestContId.longValue());
        }
        return null;
    }

    public void makeContainerCurrent(GuestContainer guestContainer) {
        AppConfig.getInstance(this.mContext).setCurrentGuestContId(Long.valueOf(guestContainer != null ? guestContainer.mId.longValue() : 0));
    }

    public void makeContainerActive(GuestContainer guestContainer) {
        File file = new File(this.mHomeDir, "xdroid");
        file.delete();
        StringBuilder sb = new StringBuilder();
        sb.append("./xdroid_");
        sb.append(guestContainer.mId);
        SafeFileHelpers.symlink(sb.toString(), file.getAbsolutePath());
    }

    public String getIconPath(XDGLink xDGLink) {
        File file = new File(xDGLink.guestCont.mIconsPath);
        StringBuilder sb = new StringBuilder();
        sb.append(xDGLink.icon);
        sb.append(".png");
        File file2 = new File(file, sb.toString());
        if (file2.exists()) {
            return file2.getAbsolutePath();
        }
        return null;
    }

    public void copyXDGLinkToDesktop(XDGLink xDGLink) {
        File file = xDGLink.linkFile;
        try {
            FileUtils.copyFile(file, new File(xDGLink.guestCont.mDesktopPath, file.getName()));
        } catch (IOException unused) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0040, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r2.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0049, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[ExcHandler: IOException | ClassNotFoundException (unused java.lang.Throwable), SYNTHETIC, Splitter:B:1:0x001a] */
    private static ScreenInfo loadOldWinePrefixScreenInfo(String str) {
        // Throwable th;
        Throwable th2;
        StringBuilder sb = new StringBuilder();
        sb.append("screenInfo");
        sb.append(str.replace(IOUtils.DIR_SEPARATOR_UNIX, '_'));
        try {
            FileInputStream openPrivateFileForReading = AndroidHelpers.openPrivateFileForReading(sb.toString());
            try {
                ScreenInfo screenInfo = (ScreenInfo) new ObjectInputStream(openPrivateFileForReading).readObject();
                if (openPrivateFileForReading != null) {
                    openPrivateFileForReading.close();
                }
                return screenInfo;
            } catch (Throwable th3) {
                // Throwable th4 = th3;
                // th = r1;
                th2 = th3;
            }
            if (openPrivateFileForReading != null) {
                if (th2 != null) {
                    openPrivateFileForReading.close();
                } else {
                    openPrivateFileForReading.close();
                }
            }
            throw new RuntimeException(th2);
            // throw th2;
        } catch (IOException /* | ClassNotFoundException */ unused) {
        }
		return null;
	}

    private void fixWinePrefixForXDGLinks(File file, String str, String str2) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    fixWinePrefixForXDGLinks(file2, str, str2);
                    if (file2.listFiles().length == 0) {
                        file2.delete();
                    }
                } else if (file2.getName().toLowerCase().endsWith(".desktop")) {
                    try {
                        if (!FileHelpers.replaceStringInFile(file2, str, str2)) {
                            file2.delete();
                        }
                    } catch (IOException unused) {
                    }
                }
            }
        }
    }

    private void convertXDGLinks(File file, GuestContainer guestContainer) {
        String guestPath = getGuestPath(getGuestWinePrefixPath());
        fixWinePrefixForXDGLinks(new File(guestContainer.mPath, CONTAINER_DESKTOP_DIR), getGuestPath(file.getAbsolutePath()), guestPath);
        fixWinePrefixForXDGLinks(new File(guestContainer.mPath, CONTAINER_START_MENU_DIR), getGuestPath(file.getAbsolutePath()), guestPath);
    }

    private void processAndUpdateRunScript(GuestContainer guestContainer) throws IOException {
        File file = new File(guestContainer.mWinePrefixPath, LOCAL_RUN_SCRIPT);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[((int) file.length())];
        fileInputStream.read(bArr);
        fileInputStream.close();
        if (new String(bArr).contains(" -w")) {
            guestContainer.mConfig.setRunArguments("-w");
        }
        FileUtils.copyFile(new File(getHostPath(RECIPES_GUEST_DIR), "run/simple.sh"), file);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(16:7|(2:8|9)|10|(2:12|13)|14|16|17|18|19|20|21|22|23|24|25|31) */
    /* JADX WARNING: Can't wrap try/catch for region: R(17:7|(2:8|9)|10|12|13|14|16|17|18|19|20|21|22|23|24|25|31) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x00bb */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x00be */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x00c1 */
    public void convertFromOldVersion() {
        try {
			// File[] listFiles;
			File file = new File(this.mImageDir, "/home/xdroid/wp/");
			if (file.exists()) {
				for (File file2 : file.listFiles()) {
					if (file2.isDirectory()) {
						GuestContainer createContainer = createContainer();
						createContainer.mConfig.setScreenInfo(loadOldWinePrefixScreenInfo(getGuestPath(file2.getAbsolutePath())));
						try {
							FileUtils.copyDirectory(new File(this.mImageDir, "/home/xdroid/.local/share/icons"), new File(createContainer.mPath, ".local/share/icons"));
							FileUtils.copyDirectory(new File(this.mImageDir, "/home/xdroid/.local/share/applications/wine/Programs/"), new File(createContainer.mPath, CONTAINER_START_MENU_DIR));
						} catch (IOException unused) {
						}
						File file3 = new File(createContainer.mPath, ".wine");
						try {
							FileUtils.deleteDirectory(file3);
						} catch (IOException unused2) {
						}
						file2.renameTo(file3);
						convertXDGLinks(file2, createContainer);
						WineRegistryEditor wineRegistryEditor = new WineRegistryEditor(new File(file3, "user.reg"));
						wineRegistryEditor.read();
						wineRegistryEditor.setStringParam("Software\\Wine\\DirectInput", "MouseWarpOverride", "disable");
						FileUtils.copyFile(new File(this.mImageDir, NOTEPAD_GUEST_PATH), new File(file3, "drive_c/windows/notepad.exe"));
						FileUtils.copyFile(new File(this.mImageDir, NOTEPAD_GUEST_PATH), new File(file3, "drive_c/windows/system32/notepad.exe"));
						wineRegistryEditor.setStringParam("Software\\Wine\\DllOverrides", "notepad.exe", "native,builtin");
						wineRegistryEditor.write();
						processAndUpdateRunScript(createContainer);
						FileHelpers.replaceStringInFile(new File(file3, "user.reg"), getGuestPath(file2.getAbsolutePath()), getGuestPath(getGuestWinePrefixPath()));
					}
				}
				File file4 = new File(this.mImageDir, "/home/xdroid/");
				file4.renameTo(new File(this.mImageDir, "/home/old_xdroid/"));
				file4.setReadable(true, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
}
