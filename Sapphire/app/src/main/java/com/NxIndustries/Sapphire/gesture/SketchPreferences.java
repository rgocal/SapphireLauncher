package com.NxIndustries.Sapphire.gesture;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.NxIndustries.Sapphire.LauncherAppState;
import com.NxIndustries.Sapphire.settings.SettingsProvider;

/**
 * Created by ry on 2/12/17.
 */

public class SketchPreferences extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            Fragment newFragment = new SketchSettings();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(CONTENT_VIEW_ID, newFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        LauncherAppState.setSettingsChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        SettingsProvider.get(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SettingsProvider.get(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
