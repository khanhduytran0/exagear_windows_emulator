package com.eltechs.axs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.iwgang.countdownview.CountdownView;
import com.eltechs.axs.AppConfig;
import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;
// import com.eltechs.axs.applicationState.PurchasableComponentsCollectionAware;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.configuration.startup.AvailableExecutableFiles;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;
import com.eltechs.axs.configuration.startup.actions.SelectExecutableFile.UserRequestedAction;
import com.eltechs.axs.container.annotations.Autowired;
import com.eltechs.axs.container.annotations.PostAdd;
// import com.eltechs.axs.firebase.FAHelper;
import com.eltechs.axs.helpers.Assert;
// import com.eltechs.axs.helpers.PromoHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.collection.CompositeCollection;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.*;

public class SelectExecutableFileActivity<StateClass extends ApplicationStateBase<StateClass> /* & PurchasableComponentsCollectionAware */ & SelectedExecutableFileAware<StateClass>> extends FrameworkActivity<StateClass> {
    private static final int REQUEST_CODE_ADD_MORE_GAMES = 10003;
    private static final int REQUEST_CODE_CHOOSE_CUSTOMIZATION_PACKAGE = 10001;
    private static final int REQUEST_CODE_SET_RUN_OPTIONS = 10002;
    private static final int REQUEST_CODE_SHOW_BUY_PROMO_WINDOW = 10005;
    private static final int REQUEST_CODE_SHOW_BUY_WINDOW = 10004;
    public static final String TAG = "SelectExecutableFileActivity";
    /* access modifiers changed from: private */
    public List<DetectedExecutableFile<StateClass>> otherExecutableFiles;
    /* access modifiers changed from: private */
    public boolean showOthers = false;
    /* access modifiers changed from: private */
    public List<DetectedExecutableFile<StateClass>> supportedExecutableFiles;

    private class Adapter extends BaseAdapter {
        private final int commonPathPrefixLength;
        private final View[] myViews = new View[getCount()];

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public int getItemViewType(int i) {
            return -1;
        }

        public Adapter() {
            ArrayList arrayList = new ArrayList(supportedExecutableFiles.size() + otherExecutableFiles.size());
            for (DetectedExecutableFile parentDir : (Collection<DetectedExecutableFile>) CompositeCollection.of((Collection) supportedExecutableFiles, (Collection) otherExecutableFiles)) {
                arrayList.add(parentDir.getParentDir().getAbsolutePath());
            }
            this.commonPathPrefixLength = calculateCommonPrefixLength(arrayList);
        }

        private int calculateCommonPrefixLength(List<String> list) {
            if (list.isEmpty()) {
                return 0;
            }
            String[] split = ((String) list.get(0)).split("/");
            int length = split.length - 1;
            for (String split2 : list) {
                String[] split3 = split2.split("/");
                length = Math.min(length, split3.length - 1);
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (!split[i].equals(split3[i])) {
                        length = i;
                        break;
                    } else {
                        i++;
                    }
                }
            }
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                i2 += split[i3].length() + 1;
            }
            return i2;
        }

        public int getCount() {
            if (showOthers) {
                return supportedExecutableFiles.size() + otherExecutableFiles.size() + 1;
            }
            return supportedExecutableFiles.size() + (otherExecutableFiles.isEmpty() ^ true ? 1 : 0);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            DetectedExecutableFile detectedExecutableFile;
            if (this.myViews[i] != null) {
                return this.myViews[i];
            }
            boolean z = false;
            View inflate = getLayoutInflater().inflate(i != supportedExecutableFiles.size() ? R.layout.select_executable_file_elem : R.layout.select_executable_file_separator, viewGroup, false);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.sef_file_icon);
            TextView textView = (TextView) inflate.findViewById(R.id.sef_file_description);
            ImageButton imageButton = (ImageButton) inflate.findViewById(R.id.sef_show_configuration_menu);
            if (i == supportedExecutableFiles.size()) {
                textView.setText(showOthers ? R.string.hide_files : R.string.more_files);
                installMoreLessOptionButtonListener(inflate);
                if (!supportedExecutableFiles.isEmpty() && !otherExecutableFiles.isEmpty()) {
                    z = true;
                }
                textView.setEnabled(z);
                inflate.setEnabled(z);
            } else {
                if (i < supportedExecutableFiles.size()) {
                    z = true;
                }
                if (z) {
                    detectedExecutableFile = (DetectedExecutableFile) supportedExecutableFiles.get(i);
                } else {
                    detectedExecutableFile = (DetectedExecutableFile) otherExecutableFiles.get((i - supportedExecutableFiles.size()) - 1);
                }
                imageView.setImageBitmap(detectedExecutableFile.getIcon());
                textView.setText(generateFileDescription(detectedExecutableFile));
                if (z) {
                    installSupportedFileButtonListener(detectedExecutableFile, imageView, inflate, imageButton);
                } else {
                    // detectedExecutableFile.setUserSelectedCustomizationPackage(null);
                    installUnsupportedFileButtonListener(detectedExecutableFile, imageView, inflate, imageButton);
                }
            }
            this.myViews[i] = inflate;
            return inflate;
        }

        private Spanned generateFileDescription(DetectedExecutableFile<StateClass> detectedExecutableFile) {
            return Html.fromHtml(String.format("<b>%s</b><br>in %s", new Object[]{StringEscapeUtils.escapeHtml4(detectedExecutableFile.getFileName()), StringEscapeUtils.escapeHtml4(detectedExecutableFile.getParentDir().getAbsolutePath().substring(this.commonPathPrefixLength))}));
        }

        private void installSupportedFileButtonListener(final DetectedExecutableFile<StateClass> detectedExecutableFile, View view, View view2, View view3) {
            OnClickListener r0 = new OnClickListener() {
                public void onClick(View view) {
                    ((SelectedExecutableFileAware) getApplicationState()).setSelectedExecutableFile(detectedExecutableFile);
                    signalUserInteractionFinished(UserRequestedAction.EXECUTABLE_FILE_SELECTED);
                }
            };
            OnClickListener r1 = new OnClickListener() {
                public void onClick(View view) {
                    ((SelectedExecutableFileAware) getApplicationState()).setSelectedExecutableFile(detectedExecutableFile);
                    startActivityForResult(10002, AdvancedRunOptions.class);
                }
            };
            view.setOnClickListener(r0);
            view2.setOnClickListener(r0);
            view3.setOnClickListener(r1);
        }

        private void installUnsupportedFileButtonListener(final DetectedExecutableFile<StateClass> detectedExecutableFile, View view, View view2, View view3) {
            OnClickListener r0 = new OnClickListener() {
                public void onClick(View view) {
                    ((SelectedExecutableFileAware) getApplicationState()).setSelectedExecutableFile(detectedExecutableFile);
                    if (true) { // detectedExecutableFile.getEffectiveCustomizationPackage() != null) {
                        signalUserInteractionFinished(UserRequestedAction.EXECUTABLE_FILE_SELECTED);
                    } else {
                        startActivityForResult(10001, SelectCustomizationPackageActivity.class);
                    }
                }
            };
            OnClickListener r1 = new OnClickListener() {
                public void onClick(View view) {
                    ((SelectedExecutableFileAware) getApplicationState()).setSelectedExecutableFile(detectedExecutableFile);
                    startActivityForResult(10002, AdvancedRunOptions.class);
                }
            };
            view.setOnClickListener(r0);
            view2.setOnClickListener(r0);
            view3.setOnClickListener(r1);
        }

        private void installMoreLessOptionButtonListener(View view) {
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    showOthers = !showOthers;
                    resetList();
                }
            });
        }
    }

    public SelectExecutableFileActivity() {
        enableLogging(false);
    }

    @Autowired
    private void setAvailableExecutableFiles(AvailableExecutableFiles<StateClass> availableExecutableFiles) {
        this.supportedExecutableFiles = availableExecutableFiles.getSupportedFiles();
        this.otherExecutableFiles = availableExecutableFiles.getOtherFiles();
        if (this.supportedExecutableFiles.isEmpty()) {
            this.showOthers = true;
        }
    }

    @PostAdd
    private void checkConsistency() {
        Assert.state((this.supportedExecutableFiles == null || this.otherExecutableFiles == null) ? false : true, "The collection of available executable files must be defined before calling SelectExecutableFileActivity.");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.select_executable_file);
        // processRemindActions();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        resetList();
    }

    /* access modifiers changed from: private */
    public void resetList() {
        ((ListView) findViewById(R.id.list_of_available_executable_files)).setAdapter(new Adapter());
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        findViewById(R.id.promo_layout).setVisibility(View.GONE);
        if (i == 10001 && i2 != 0) {
            signalUserInteractionFinished(UserRequestedAction.EXECUTABLE_FILE_SELECTED);
        } else if (i == REQUEST_CODE_ADD_MORE_GAMES) {
            resetList();
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }

    public void onAddMoreGamesClicked(View view) {
        startActivityForResult(REQUEST_CODE_ADD_MORE_GAMES, AddGameWizard.class);
    }

    public void onRequestRescanClicked(View view) {
        signalUserInteractionFinished(UserRequestedAction.FULL_SCAN_REQUESTED);
    }
}
