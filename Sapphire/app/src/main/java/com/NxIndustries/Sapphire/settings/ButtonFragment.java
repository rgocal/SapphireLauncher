package com.NxIndustries.Sapphire.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.util.AppHelper;
import com.NxIndustries.Sapphire.util.ShortcutPickHelper;

/**
 * Created by ry on 10/21/16.
 */

public class ButtonFragment extends SettingsPreferenceFragmentStock
        implements Preference.OnPreferenceChangeListener {

    ShortcutPickHelper mPicker;

    ListPreference mHomeButton;
    ListPreference mMenuButton;
    ListPreference mBackButton;

    String mGesture;
    ListPreference mGestureCurrent;

    public static ButtonFragment newInstance() {
        return new ButtonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.button_fragment);
        setHasOptionsMenu(true);

        mHomeButton = (ListPreference) findPreference(SettingsProvider.KEY_HOME_BUTTON_ACTION);
        mMenuButton = (ListPreference) findPreference(SettingsProvider.KEY_MENU_BUTTON_ACTION);
        mBackButton = (ListPreference) findPreference(SettingsProvider.KEY_BACK_BUTTON_ACTION);

        mHomeButton.setOnPreferenceChangeListener(this);
        mMenuButton.setOnPreferenceChangeListener(this);
        mBackButton.setOnPreferenceChangeListener(this);

        mPicker = new ShortcutPickHelper(getActivity(), new ShortcutPickHelper.OnPickListener() {
            @Override
            public void shortcutPicked(String uri, String friendlyName, boolean isApplication) {
                if (uri == null) {
                    return;
                }
                if (mGesture != null) {
                    SettingsProvider.putString(mContext, mGesture + "_custom", uri);
                    mGesture = null;
                }
                if (mGestureCurrent != null) {
                    updateSummary(mGestureCurrent, "custom");
                    mGestureCurrent = null;
                }
            }
        });

        updatePrefs();
    }

    private void updatePrefs() {
        updateSummary(mHomeButton, mHomeButton.getValue());
        updateSummary(mMenuButton, mMenuButton.getValue());
        updateSummary(mBackButton, mBackButton.getValue());
    }

    private void updateSummary(ListPreference pref, String value) {
        if (value.equals("custom")) {
            pref.setSummary(AppHelper.getFriendlyNameForUri(
                    getActivity(), getActivity().getPackageManager(),
                    SettingsProvider.getString(getActivity(), pref.getKey() + "_custom", "")));
        } else {
            CharSequence[] array = pref.getEntryValues();
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(value)) {
                    pref.setSummary(pref.getEntries()[i]);
                    return;
                }
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHomeButton ||
                preference == mMenuButton || preference == mBackButton) {
            if (newValue.equals("custom")) {
                mGesture = preference.getKey();
                mGestureCurrent = (ListPreference) preference;
                mPicker.pickShortcut(null, null, getId());
                return true;
            }
            updateSummary((ListPreference) preference, (String) newValue);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != Activity.RESULT_CANCELED
                && resultCode != Activity.RESULT_CANCELED) {
            mPicker.onActivityResult(requestCode, resultCode, data);
        }
    }
}
