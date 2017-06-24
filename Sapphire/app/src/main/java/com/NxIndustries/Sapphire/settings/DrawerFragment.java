package com.NxIndustries.Sapphire.settings;

import android.os.Bundle;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.preference.DoubleNumberPickerPreference;

public class DrawerFragment extends SettingsPreferenceFragmentStock {

    private DoubleNumberPickerPreference mDrawerGrid;

    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.drawer_preferences);

        mDrawerGrid = (DoubleNumberPickerPreference)
                findPreference(SettingsProvider.KEY_DRAWER_GRID);

        if (mProfile != null) {
            if (SettingsProvider.getCellCountX(getActivity(),
                    SettingsProvider.KEY_DRAWER_GRID, 0) < 1) {
                SettingsProvider.putCellCountX(getActivity(),
                        SettingsProvider.KEY_DRAWER_GRID, mProfile.allAppsNumCols);
                mDrawerGrid.setDefault2(mProfile.allAppsNumCols);
            }
            if (SettingsProvider.getCellCountY(getActivity(),
                    SettingsProvider.KEY_DRAWER_GRID, 0) < 1) {
                SettingsProvider.putCellCountY(getActivity(),
                        SettingsProvider.KEY_DRAWER_GRID, mProfile.allAppsNumRows);
                mDrawerGrid.setDefault1(mProfile.allAppsNumRows);
            }
        }
    }
}
