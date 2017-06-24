package com.NxIndustries.Sapphire.settings;

import android.os.Bundle;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.preference.NumberPickerPreference;

public class DockFragment extends SettingsPreferenceFragmentStock {

    public static DockFragment newInstance() {
        return new DockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.dock_preferences);

        final NumberPickerPreference dockIcons = (NumberPickerPreference)
                findPreference(SettingsProvider.KEY_DOCK_ICONS);

        if (mProfile != null) {
            if (SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_DOCK_ICONS, 0) < 1) {
                SettingsProvider.putInt(getActivity(),
                        SettingsProvider.KEY_DOCK_ICONS, (int) mProfile.numHotseatIcons);
                dockIcons.setDefaultValue((int) mProfile.numHotseatIcons);
            }
        }
    }
}
