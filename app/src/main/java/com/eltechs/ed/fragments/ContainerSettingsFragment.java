package com.eltechs.ed.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import com.eltechs.ed.R;
import com.eltechs.ed.guestContainers.GuestContainerConfig;

public class ContainerSettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {
    public static final String ARG_CONT_ID = "CONT_ID";

    public void onCreatePreferences(Bundle bundle, String str) {
        Long valueOf = Long.valueOf(getArguments().getLong("CONT_ID"));
        PreferenceManager preferenceManager = getPreferenceManager();
        StringBuilder sb = new StringBuilder();
        sb.append(GuestContainerConfig.CONTAINER_CONFIG_FILE_KEY_PREFIX);
        sb.append(valueOf);
        preferenceManager.setSharedPreferencesName(sb.toString());
        setPreferencesFromResource(R.xml.container_prefs, str);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((int) R.string.wd_title_container_prop);
    }

    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            updatePreference(preference);
            preference.setSingleLineTitle(false);
        }
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        updatePreference(findPreference(str));
    }

    private void updatePreference(Preference preference) {
        if (preference != null && (preference instanceof EditTextPreference)) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setSummary((CharSequence) editTextPreference.getText());
        }
    }
}
