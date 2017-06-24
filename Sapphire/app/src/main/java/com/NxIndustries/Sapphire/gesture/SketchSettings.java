package com.NxIndustries.Sapphire.gesture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.settings.SettingsPreferenceFragmentStock;
import com.NxIndustries.Sapphire.settings.SettingsProvider;

/**
 * Created by ry on 2/12/17.
 */

    public class SketchSettings extends SettingsPreferenceFragmentStock implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        CheckBoxPreference chkDisposeQuickLauncherAfterLaunch = null;
        CheckBoxPreference chkEnableMultipleStrokes = null;
        Preference prefTimeMultipleStrokes = null;
        Preference prefGesturePrecision = null;
        Preference prefGestureNotification = null;

        private Context context;

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            new G2LDataBaseHandler(getActivity(), null, null, 1);
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.sapphire_gesture_preference);
            chkEnableMultipleStrokes = (CheckBoxPreference) findPreference("keyChkEnableMultipleStroke");
            prefTimeMultipleStrokes = findPreference("keyWaitTime");
            prefGesturePrecision = findPreference("keyGestureSensitivity");
            prefGestureNotification = findPreference("keyPrefGest");

            chkDisposeQuickLauncherAfterLaunch = (CheckBoxPreference) findPreference("keyChkQuitAferLaunching");

            loadSettings();

            chkDisposeQuickLauncherAfterLaunch.setOnPreferenceChangeListener(this);
            chkEnableMultipleStrokes.setOnPreferenceChangeListener(this);
            prefTimeMultipleStrokes.setOnPreferenceClickListener(this);
            prefGesturePrecision.setOnPreferenceClickListener(this);
        }

        private void loadSettings() {

            G2LDataBaseHandler dbh = new G2LDataBaseHandler(getActivity(), null, null, 1);
            if (dbh.getSettings(SettingsProvider.FINISH_ACTIVITY).equals("true")) {
                chkDisposeQuickLauncherAfterLaunch.setChecked(true);
            } else {
                chkDisposeQuickLauncherAfterLaunch.setChecked(false);
            }
            if (dbh.getSettings(SettingsProvider.ENABLE_MULTIPLE_STROKE).equals("true")) {
                chkEnableMultipleStrokes.setChecked(true);
            } else {
                chkEnableMultipleStrokes.setChecked(false);
            }
            dbh.close();

        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            G2LDataBaseHandler dbh = new G2LDataBaseHandler(getActivity(), null, null, 1);
            if (preference == chkDisposeQuickLauncherAfterLaunch) {
                dbh.setSettings(SettingsProvider.FINISH_ACTIVITY, newValue.toString());
                if (newValue.toString().equals("true")) {
                    chkDisposeQuickLauncherAfterLaunch.setChecked(true);
                } else {
                    chkDisposeQuickLauncherAfterLaunch.setChecked(false);
                }
            } else if (preference == chkEnableMultipleStrokes) {
                dbh.setSettings(SettingsProvider.ENABLE_MULTIPLE_STROKE, newValue.toString());
                if (newValue.toString().equals("true")) {
                    chkEnableMultipleStrokes.setChecked(true);
                } else {
                    chkEnableMultipleStrokes.setChecked(false);
                }
            }
            dbh.close();
            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == prefTimeMultipleStrokes) {
                return false;
            } else if (preference == prefGesturePrecision) {
                return false;
            }
            startActivity(new Intent(context, SketchPreferences.class));
            return false;
        }
    }