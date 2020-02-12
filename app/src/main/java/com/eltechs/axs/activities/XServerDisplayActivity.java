package com.eltechs.axs.activities;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import android.widget.FrameLayout.*;
import com.eltechs.axs.*;
import com.eltechs.axs.applicationState.*;
import com.eltechs.axs.container.annotations.*;
import com.eltechs.axs.environmentService.*;
import com.eltechs.axs.environmentService.components.*;
import com.eltechs.axs.helpers.*;
// import com.eltechs.axs.payments.*;
import com.eltechs.axs.widgets.actions.*;
import com.eltechs.axs.widgets.popupMenu.*;
import com.eltechs.axs.widgets.viewOfXServer.*;
import com.eltechs.axs.xserver.*;
import com.eltechs.ed.R;
import java.util.*;

public class XServerDisplayActivity<StateClass extends ApplicationStateBase<StateClass> /* & PurchasableComponentsCollectionAware */& XServerDisplayActivityConfigurationAware & SelectedExecutableFileAware<StateClass>> extends FrameworkActivity<StateClass> {
    private static final long COUNT_DOWN_INTERVAL = 20000;
    private static final long COUNT_DOWN_TOTAL = 86400000;
    private static final boolean ENABLE_TRACING_METHODS = false;
    private static final int REQUEST_CODE_INFORMER = 10003;
    private Runnable contextMenuRequestHandler;
    private XServerDisplayActivityInterfaceOverlay interfaceOverlay;
    private CountDownTimer periodicIabCheckTimer = new CountDownTimer(COUNT_DOWN_TOTAL, COUNT_DOWN_INTERVAL) {
        public void onTick(long j) {
            XServerDisplayActivity.this.checkUiThread();
			// MOD: Bypass iab check.
			/*
            if (XServerDisplayActivity.this.isActivityResumed()) {
                XServerDisplayActivity.this.checkIab();
            }
			*/
        }

        public void onFinish() {
            XServerDisplayActivity.this.periodicIabCheckTimer.start();
        }
    };
    private View uiOverlayView;
    private ViewOfXServer viewOfXServer;

    private static class NoMenuPopup implements Runnable {
        public static final Runnable INSTANCE = new NoMenuPopup();

        public void run() {
        }

        private NoMenuPopup() {
        }
    }

    private static class TrivialInterfaceOverlay implements XServerDisplayActivityInterfaceOverlay {
        public void detach() {
        }

        private TrivialInterfaceOverlay() {
        }

        public View attach(XServerDisplayActivity xServerDisplayActivity, ViewOfXServer viewOfXServer) {
            View view = new View(xServerDisplayActivity);
            view.setBackgroundColor(xServerDisplayActivity.getResources().getColor(17170445));
            return view;
        }
    }
	
    @Autowired
    private void setXServerDisplayActivityInterfaceOverlay(XServerDisplayActivityInterfaceOverlay xServerDisplayActivityInterfaceOverlay) {
        this.interfaceOverlay = xServerDisplayActivityInterfaceOverlay;
    }

    public void onCreate(Bundle bundle) {
        ViewFacade viewFacade = null;
        super.onCreate(bundle);
        ApplicationStateBase applicationState = getApplicationState();
        XServerComponent xServerComponent = (XServerComponent) applicationState.getEnvironment().getComponent(XServerComponent.class);
        Class cls = (Class) getIntent().getSerializableExtra("facadeclass");
        if (cls != null) {
            try {
                viewFacade = (ViewFacade) cls.getDeclaredConstructor(new Class[]{XServer.class, ApplicationStateBase.class}).newInstance(new Object[]{xServerComponent.getXServer(), applicationState});
            } catch (Exception unused) {
                Assert.state(false);
            }
            getWindow().addFlags(128);
            getWindow().addFlags(4194304);
            setContentView(R.layout.main);
            if (checkForSuddenDeath()) {
                this.viewOfXServer = new ViewOfXServer(this, xServerComponent.getXServer(), viewFacade, applicationState.getXServerViewConfiguration());
                this.periodicIabCheckTimer.start();
                return;
            }
            return;
        }
        viewFacade = null;
        getWindow().addFlags(128);
        getWindow().addFlags(4194304);
        setContentView(R.layout.main);
        if (checkForSuddenDeath()) {
        }
		
		setXServerDisplayActivityInterfaceOverlay(new TrivialInterfaceOverlay());
    }

    public void onResume() {
        super.onResume();
        if (!checkForSuddenDeath()) {
            this.contextMenuRequestHandler = NoMenuPopup.INSTANCE;
            buildUI();
            this.viewOfXServer.onResume();
            this.uiOverlayView.requestFocus();
            AXSEnvironment environment = getApplicationState().getEnvironment();
            if (environment != null) {
                environment.resumeEnvironment();
            }
            // checkIab();
            if (getApplicationContext().getPackageName().equals("com.eltechs.ed")) {
                AppConfig instance = AppConfig.getInstance(this);
                DialogFragment controlsInfoDialog = ((SelectedExecutableFileAware) getApplicationState()).getSelectedExecutableFile().getControlsInfoDialog();
                String controlsId = ((SelectedExecutableFileAware) getApplicationState()).getSelectedExecutableFile().getControlsId();
                Set controlsWithInfoShown = instance.getControlsWithInfoShown();
                if (!controlsWithInfoShown.contains(controlsId)) {
                    controlsInfoDialog.show(getSupportFragmentManager(), "CONTROLS_INFO");
                    controlsWithInfoShown.add(controlsId);
                    instance.setControlsWithInfoShown(controlsWithInfoShown);
                }
            }
        }
    }

    private void buildUI() {
        setContentView(R.layout.main);
        getRootLayout().addView(this.viewOfXServer);
        this.uiOverlayView = this.interfaceOverlay.attach(this, this.viewOfXServer);
        getRootLayout().addView(this.uiOverlayView);
    }

    private boolean checkForSuddenDeath() {
        if (Globals.getApplicationState() != null) {
            return ENABLE_TRACING_METHODS;
        }
        FatalErrorActivity.showFatalError(getResources().getString(R.string.xda_guest_suddenly_died));
        finish();
        return true;
    }
	
    public void onPause() {
        super.onPause();
        AXSEnvironment environment = getApplicationState().getEnvironment();
        if (environment != null) {
            environment.freezeEnvironment();
        }
        this.viewOfXServer.onPause();
        this.interfaceOverlay.detach();
        this.uiOverlayView = null;
        getRootLayout().removeAllViews();
    }

    public void onDestroy() {
        super.onDestroy();
        this.viewOfXServer = null;
        setContentView(new TextView(this));
        this.periodicIabCheckTimer.cancel();
        this.periodicIabCheckTimer = null;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == REQUEST_CODE_INFORMER) {
            if (i2 == 0) {
                StartupActivity.shutdownAXSApplication();
                finish();
            }
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    public void addDefaultPopupMenu(List<? extends Action> list) {
        View textView = new TextView(this);
        textView.setBackgroundColor(getResources().getColor(17170445));
        getRootLayout().addView(textView, new LayoutParams(0, 0, 5));
        final AXSPopupMenu aXSPopupMenu = new AXSPopupMenu(this, textView);
        aXSPopupMenu.add(list);
        this.contextMenuRequestHandler = new Runnable() {
            public void run() {
                aXSPopupMenu.show();
            }
        };
    }

    public void placeViewOfXServer(int i, int i2, int i3, int i4) {
        if (this.viewOfXServer != null) {
            LayoutParams layoutParams = (LayoutParams) this.viewOfXServer.getLayoutParams();
            if (layoutParams.leftMargin != i || layoutParams.topMargin != i2 || layoutParams.width != i3 || layoutParams.height != i4) {
                layoutParams.leftMargin = i;
                layoutParams.topMargin = i2;
                layoutParams.width = i3;
                layoutParams.height = i4;
                this.viewOfXServer.setLayoutParams(layoutParams);
                this.viewOfXServer.invalidate();
            }
        }
    }

    public boolean isHorizontalStretchEnabled() {
        return this.viewOfXServer.isHorizontalStretchEnabled();
    }

    public void setHorizontalStretchEnabled(boolean z) {
        this.viewOfXServer.setHorizontalStretchEnabled(z);
    }

    public void showPopupMenu() {
        this.contextMenuRequestHandler.run();
    }

    private FrameLayout getRootLayout() {
        return (FrameLayout) findViewById(R.id.mainView);
    }

    public void startInformerActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_INFORMER);
    }

    public void freezeXServerScene() {
        if (this.viewOfXServer != null) {
            this.viewOfXServer.freezeRenderer();
        }
    }

    public void unfreezeXServerScene() {
        if (this.viewOfXServer != null) {
            this.viewOfXServer.unfreezeRenderer();
        }
    }
}

