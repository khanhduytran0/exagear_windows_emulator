package com.eltechs.ed;

import android.text.TextUtils;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.ed.controls.ArcanumControls;
import com.eltechs.ed.controls.Civ3Controls;
import com.eltechs.ed.controls.Controls;
import com.eltechs.ed.controls.Disciples2Controls;
import com.eltechs.ed.controls.FalloutControls;
import com.eltechs.ed.controls.HoMM3Controls;
import com.eltechs.ed.controls.JA2Controls;
import com.eltechs.ed.controls.MMControls;
import com.eltechs.ed.controls.Panzer2Controls;
import com.eltechs.ed.controls.RtsControls;
import com.eltechs.ed.startupActions.ContainerStartupAction;
import java.util.Arrays;
import java.util.List;

public class InstallRecipe {
    public static final List<InstallRecipe> LIST = Arrays.asList(new InstallRecipe[]{
		new InstallRecipe("Age of Wonders").setScreenInfo(new ScreenInfo(640, 480, 32)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/age_of_wonders"),
		new InstallRecipe("Arcanum: Of Steamworks and Magick Obscura").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new ArcanumControls()).setRunArguments("-no3d -doublebuffer").setDownloadURL("https://www.gog.com/game/arcanum_of_steamworks_and_magick_obscura"),
		new InstallRecipe("Caesar III").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/caesar_3"),
		new InstallRecipe("Diablo 2").setRunScript("run/diablo2.sh").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setRunArguments("-w"),
		new InstallRecipe("Disciples 2").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new Disciples2Controls()).setDownloadURL("https://www.gog.com/game/disciples_2_gold"),
		new InstallRecipe("Divine Divinity").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new ArcanumControls()).setDownloadURL("https://www.gog.com/game/divine_divinity").setStartupActions(ContainerStartupAction.ID_DIVINE_DIVINITY_SETTINGS).setRunGuide(AppRunGuide.ID_DIVINE_DIVINITY),
		new InstallRecipe("Fallout").setScreenInfo(new ScreenInfo(640, 480, 32)).setControls(new FalloutControls()).setDownloadURL("https://www.gog.com/game/fallout").setRunGuide(AppRunGuide.ID_FALLOUT),
		new InstallRecipe("Fallout 2").setScreenInfo(new ScreenInfo(640, 480, 32)).setControls(new FalloutControls()).setDownloadURL("https://www.gog.com/game/fallout_2").setRunGuide(AppRunGuide.ID_FALLOUT2),
		new InstallRecipe("Heroes of Might and Magic 3").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/heroes_of_might_and_magic_3_complete_edition"),
		new InstallRecipe("Heroes of Might and Magic 4").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/heroes_of_might_and_magic_4_complete"),
		new InstallRecipe("Heroes Chronicles").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/heroes_chronicles_all_chapters"),
		new InstallRecipe("Jagged Alliance 2").setScreenInfo(new ScreenInfo(640, 480, 16)).setControls(new JA2Controls()).setRunArguments("WINELOADERNOEXEC=1").setDownloadURL("https://www.gog.com/game/heroes_of_might_and_magic_2_gold_edition"),
		new InstallRecipe("Microsoft Word Viewer 2003"),
		new InstallRecipe("Microsoft Office 2010").setInstallScript("office2010.sh"),
		new InstallRecipe("Might and Magic 6").setScreenInfo(new ScreenInfo(640, 480, 16)).setControls(new MMControls()).setDownloadURL("https://www.gog.com/game/might_and_magic_6_limited_edition"),
		new InstallRecipe("Might and Magic 7").setScreenInfo(new ScreenInfo(640, 480, 16)).setControls(new MMControls()).setDownloadURL("https://www.gog.com/game/might_and_magic_7_for_blood_and_honor").setStartupActions(ContainerStartupAction.ID_MM7_SETTINGS),
		new InstallRecipe("Might and Magic 8").setScreenInfo(new ScreenInfo(640, 480, 16)).setControls(new MMControls()).setDownloadURL("https://www.gog.com/game/might_and_magic_8_day_of_the_destroyer").setStartupActions(ContainerStartupAction.ID_MM8_SETTINGS),
		new InstallRecipe("Neighbours From Hell").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new Disciples2Controls()).setDownloadURL("https://www.gog.com/game/neighbours_from_hell_compilation"),
		new InstallRecipe("Panzer General 2").setScreenInfo(new ScreenInfo(640, 480, 16)).setControls(new Panzer2Controls()).setDownloadURL("https://www.gog.com/game/panzer_general_2"),
		new InstallRecipe("Pharaoh and Cleopatra").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/pharaoh_cleopatra"),
		new InstallRecipe("Sid Meier's Alpha Centauri").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/sid_meiers_alpha_centauri"),
		new InstallRecipe("Sid Meier's Civilization III").setScreenInfo(new ScreenInfo(1024, 768, 15)).setControls(new Civ3Controls()).setDownloadURL("https://www.gog.com/game/sid_meiers_civilization_iii_complete").setRunGuide(AppRunGuide.ID_CIV3),
		new InstallRecipe("StarCraft").setScreenInfo(new ScreenInfo(640, 480, 16)).setControls(new RtsControls()),
		new InstallRecipe("Stronghold Crusader").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new RtsControls()).setDownloadURL("https://www.gog.com/game/stronghold_crusader"),
		new InstallRecipe("Total Annihilation").setScreenInfo(new ScreenInfo(640, 480, 16)).setControls(new RtsControls()).setDownloadURL("https://www.gog.com/game/total_anihilation_commander_pack"),
		new InstallRecipe("Zeus + Poseidon (Acropolis)").setScreenInfo(new ScreenInfo(800, 600, 16)).setControls(new HoMM3Controls()).setDownloadURL("https://www.gog.com/game/zeus_poseidon"),
		new InstallRecipe("Other app (not from the list)")
	});
	
    public Controls mControls = null;
    public String mDownloadURL = null;
    public String mInstallScriptName = "simple.sh";
    public String mLocaleName = null;
    public final String mName;
    public String mRunArguments = null;
    public String mRunGuide = null;
	public String mAdvancedName = "undefined";
    public String mRunScriptName = "run/simple.sh";
    public ScreenInfo mScreenInfo = null;
    public String mStartupActions = null;

    public InstallRecipe(String str) {
        this.mName = str;
    }

    public InstallRecipe setAdvancedName(String str) {
        this.mAdvancedName = str;
        return this;
    }
	
    public InstallRecipe setInstallScript(String str) {
        this.mInstallScriptName = str;
        return this;
    }

    public InstallRecipe setRunScript(String str) {
        this.mRunScriptName = str;
        return this;
    }

    public InstallRecipe setScreenInfo(ScreenInfo screenInfo) {
        this.mScreenInfo = screenInfo;
        return this;
    }

    public InstallRecipe setControls(Controls controls) {
        this.mControls = controls;
        return this;
    }

    public InstallRecipe setLocaleName(String str) {
        this.mLocaleName = str;
        return this;
    }

    public InstallRecipe setRunArguments(String str) {
        this.mRunArguments = str;
        return this;
    }

    public InstallRecipe setDownloadURL(String str) {
        this.mDownloadURL = str;
        return this;
    }

    public InstallRecipe setStartupActions(String... strArr) {
        this.mStartupActions = TextUtils.join(" ", strArr);
        return this;
    }

    public InstallRecipe setRunGuide(String str) {
        this.mRunGuide = str;
        return this;
    }

    public String getName() {
        return this.mName;
    }

    public String toString() {
        return getName();
    }

    public String getDownloadURL() {
        return this.mDownloadURL;
    }
}
