package com.eltechs.axs.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.eltechs.axs.Locales;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;
// import com.eltechs.axs.applicationState.PurchasableComponentsCollectionAware;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.configuration.startup.EnvironmentCustomisationParameters;
import com.eltechs.axs.configuration.startup.PerApplicationSettingsStore;
// import com.eltechs.axs.payments.PurchasableComponent;
// import com.eltechs.axs.payments.PurchasableComponentsCollection;
import com.eltechs.axs.widgets.actions.AbstractAction;
import com.eltechs.axs.widgets.actions.ActionGroup;
import com.eltechs.axs.widgets.popupMenu.AXSPopupMenu;
import com.eltechs.axs.xserver.ScreenInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdvancedRunOptions<StateClass extends ApplicationStateBase<StateClass> & SelectedExecutableFileAware<StateClass> /* & PurchasableComponentsCollectionAware*/ > extends FrameworkActivity<StateClass> {
    private static final int[] SUPPORTED_COLOUR_DEPTHS = {15, 16, 32};
    private static final ScreenResolution[] TYPICAL_SCREEN_RESOLUTIONS = {new ScreenResolution(640, 480), new ScreenResolution(800, 600), new ScreenResolution(1024, 768), new ScreenResolution(1280, 720)};
    private ImageButton changeColourDepthButton;
    private ImageButton changeDefaultControlsNameButton;
    private ImageButton changeLocaleButton;
    private ImageButton changeScreenResolutionButton;
    private TextView colourDepthDisplay;
    private AXSPopupMenu colourDepthsPopupMenu;
    private TextView defaultControlsNameDisplay;
    private AXSPopupMenu defaultControlsNamesPopupMenu;
    /* access modifiers changed from: private */
    public EnvironmentCustomisationParameters environmentCustomisationParameters;
    private TextView localeDisplay;
    private AXSPopupMenu localesPopupMenu;
    private TextView screenResolutionDisplay;
    private AXSPopupMenu screenResolutionsPopupMenu;

    private class ChangeColourDepth extends AbstractAction {
        private final int bpp;

        ChangeColourDepth(int i) {
            super(AdvancedRunOptions.this.formatBpp(i), true);
            this.bpp = i;
        }

        public boolean isChecked() {
            return this.bpp == AdvancedRunOptions.this.environmentCustomisationParameters.getScreenInfo().depth;
        }

        public void run() {
            ScreenInfo screenInfo = AdvancedRunOptions.this.environmentCustomisationParameters.getScreenInfo();
            EnvironmentCustomisationParameters access$100 = AdvancedRunOptions.this.environmentCustomisationParameters;
            ScreenInfo screenInfo2 = new ScreenInfo(screenInfo.widthInPixels, screenInfo.heightInPixels, screenInfo.widthInMillimeters, screenInfo.heightInMillimeters, this.bpp);
            access$100.setScreenInfo(screenInfo2);
            AdvancedRunOptions.this.fillData();
        }
    }

    private class ChangeDefaultControlsName extends AbstractAction {
        private final String defaultControlsName;

        ChangeDefaultControlsName(String str) {
            super(AdvancedRunOptions.this.formatDefaultControlsName(str), true);
            this.defaultControlsName = str;
        }

        public boolean isChecked() {
            return this.defaultControlsName.equals(AdvancedRunOptions.this.environmentCustomisationParameters.getDefaultControlsName());
        }

        public void run() {
            AdvancedRunOptions.this.environmentCustomisationParameters.setDefaultControlsName(this.defaultControlsName);
            AdvancedRunOptions.this.fillData();
        }
    }

    private class ChangeLocale extends AbstractAction {
        private final String locale;

        ChangeLocale(String str) {
            super(AdvancedRunOptions.this.formatLocale(str), true);
            this.locale = str;
        }

        public boolean isChecked() {
            return this.locale.equals(AdvancedRunOptions.this.environmentCustomisationParameters.getLocaleName());
        }

        public void run() {
            AdvancedRunOptions.this.environmentCustomisationParameters.setLocaleName(this.locale);
            AdvancedRunOptions.this.fillData();
        }
    }

    private class ChangeScreenResolution extends AbstractAction {
        private final ScreenResolution resolution;

        ChangeScreenResolution(ScreenResolution screenResolution) {
            super(AdvancedRunOptions.this.formatScreenResolution(screenResolution.width, screenResolution.height), true);
            this.resolution = screenResolution;
        }

        public boolean isChecked() {
            ScreenInfo screenInfo = AdvancedRunOptions.this.environmentCustomisationParameters.getScreenInfo();
            return this.resolution.width == screenInfo.widthInPixels && this.resolution.height == screenInfo.heightInPixels;
        }

        public void run() {
            ScreenInfo screenInfo = AdvancedRunOptions.this.environmentCustomisationParameters.getScreenInfo();
            EnvironmentCustomisationParameters access$100 = AdvancedRunOptions.this.environmentCustomisationParameters;
            ScreenInfo screenInfo2 = new ScreenInfo(this.resolution.width, this.resolution.height, this.resolution.width / 10, this.resolution.height / 10, screenInfo.depth);
            access$100.setScreenInfo(screenInfo2);
            AdvancedRunOptions.this.fillData();
        }
    }

    private static class ScreenResolution {
        public final int height;
        public final int width;

        ScreenResolution(int i, int i2) {
            this.width = i;
            this.height = i2;
        }
    }

    /* access modifiers changed from: private */
    public String formatDefaultControlsName(String str) {
        return str;
    }

    /* access modifiers changed from: private */
    public String formatLocale(String str) {
        return str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.advanced_run_options);
        resizeRootViewToStandardDialogueSize();
        this.environmentCustomisationParameters = ((SelectedExecutableFileAware) getApplicationState()).getSelectedExecutableFile().getEnvironmentCustomisationParameters();
        lookupWidgets();
        createPopupMenus();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        fillData();
    }

    private void lookupWidgets() {
        this.screenResolutionDisplay = (TextView) findViewById(R.id.aro_screen_resolution);
        this.changeScreenResolutionButton = (ImageButton) findViewById(R.id.aro_change_screen_resolution);
        this.colourDepthDisplay = (TextView) findViewById(R.id.aro_colour_depth);
        this.changeColourDepthButton = (ImageButton) findViewById(R.id.aro_change_colour_depth);
        this.localeDisplay = (TextView) findViewById(R.id.aro_locale);
        this.changeLocaleButton = (ImageButton) findViewById(R.id.aro_change_locale);
        this.defaultControlsNameDisplay = (TextView) findViewById(R.id.aro_default_controls_name);
        this.changeDefaultControlsNameButton = (ImageButton) findViewById(R.id.aro_change_default_controls_name);
    }

    private void createPopupMenus() {
        createScreenResolutionsPopupMenu();
        createColourDepthsPopupMenu();
        createLocalesPopupMenu();
        createDefaultControlsNamePopupMenu();
    }

    private void createScreenResolutionsPopupMenu() {
        ActionGroup actionGroup = new ActionGroup();
        actionGroup.setExclusive(true);
        for (ScreenResolution changeScreenResolution : TYPICAL_SCREEN_RESOLUTIONS) {
            actionGroup.add(new ChangeScreenResolution(changeScreenResolution));
        }
        this.screenResolutionsPopupMenu = new AXSPopupMenu(this, this.changeScreenResolutionButton, 5);
        this.screenResolutionsPopupMenu.add(actionGroup);
    }

    private void createColourDepthsPopupMenu() {
        ActionGroup actionGroup = new ActionGroup();
        actionGroup.setExclusive(true);
        for (int changeColourDepth : SUPPORTED_COLOUR_DEPTHS) {
            actionGroup.add(new ChangeColourDepth(changeColourDepth));
        }
        this.colourDepthsPopupMenu = new AXSPopupMenu(this, this.changeColourDepthButton, 5);
        this.colourDepthsPopupMenu.add(actionGroup);
    }

    private void createLocalesPopupMenu() {
        ActionGroup actionGroup = new ActionGroup();
        actionGroup.setExclusive(true);
        for (String changeLocale : Locales.getSupportedLocales()) {
            actionGroup.add(new ChangeLocale(changeLocale));
        }
        this.localesPopupMenu = new AXSPopupMenu(this, this.changeLocaleButton, 5);
        this.localesPopupMenu.add(actionGroup);
    }

    private List<String> getSupportedDefaultControlsNames() {
        // PurchasableComponentsCollection purchasableComponentsCollection = ((PurchasableComponentsCollectionAware) getApplicationState()).getPurchasableComponentsCollection();
        ArrayList arrayList = new ArrayList();
		/*
        for (PurchasableComponent name : purchasableComponentsCollection.getPurchasableComponents()) {
            arrayList.add(name.getName());
        }
		*/
        return arrayList;
    }

    private void createDefaultControlsNamePopupMenu() {
        ActionGroup actionGroup = new ActionGroup();
        actionGroup.setExclusive(true);
        for (String changeDefaultControlsName : getSupportedDefaultControlsNames()) {
            actionGroup.add(new ChangeDefaultControlsName(changeDefaultControlsName));
        }
        this.defaultControlsNamesPopupMenu = new AXSPopupMenu(this, this.changeDefaultControlsNameButton, 5);
        this.defaultControlsNamesPopupMenu.add(actionGroup);
    }

    /* access modifiers changed from: private */
    public void fillData() {
        EnvironmentCustomisationParameters environmentCustomisationParameters2 = ((SelectedExecutableFileAware) getApplicationState()).getSelectedExecutableFile().getEnvironmentCustomisationParameters();
        ScreenInfo screenInfo = environmentCustomisationParameters2.getScreenInfo();
        this.screenResolutionDisplay.setText(formatScreenResolution(screenInfo.widthInPixels, screenInfo.heightInPixels));
        this.colourDepthDisplay.setText(formatBpp(screenInfo.depth));
        this.localeDisplay.setText(environmentCustomisationParameters2.getLocaleName());
        this.defaultControlsNameDisplay.setText(environmentCustomisationParameters2.getDefaultControlsName());
    }

    public void onChangeScreenResolutionClicked(View view) {
        this.screenResolutionsPopupMenu.show();
    }

    public void onChangeColourDepthClicked(View view) {
        this.colourDepthsPopupMenu.show();
    }

    public void onChangeLocaleClicked(View view) {
        this.localesPopupMenu.show();
    }

    public void onChangeDefaultControlsNameClicked(View view) {
        this.defaultControlsNamesPopupMenu.show();
    }

    public void onResetToDefaultsClicked(View view) {
        this.environmentCustomisationParameters.copyFrom(((SelectedExecutableFileAware) getApplicationState()).getSelectedExecutableFile().getDefaultEnvironmentCustomisationParameters());
        fillData();
    }

    public void onOKClicked(View view) {
        try {
            PerApplicationSettingsStore.get(((SelectedExecutableFileAware) getApplicationState()).getSelectedExecutableFile()).storeDetectedExecutableFileConfiguration();
        } catch (IOException unused) {
        }
        finish();
    }

    /* access modifiers changed from: private */
    public String formatScreenResolution(int i, int i2) {
        return String.format("%dx%d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
    }

    /* access modifiers changed from: private */
    public String formatBpp(int i) {
        return String.format("%d bpp", new Object[]{Integer.valueOf(i)});
    }
}
