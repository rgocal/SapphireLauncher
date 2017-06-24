package com.NxIndustries.Sapphire.gesture;

/**
 * Created by ry on 5/9/16.
 */
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.settings.SettingsProvider;

public class GestureQuickLaunch extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gesture_notification);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            //remove priority preference (not supported)
            PreferenceCategory notificationCategory =
                    (PreferenceCategory) findPreference("pref_gest_category_notification");
            ListPreference priorityPreference =
                    (ListPreference) findPreference("pref_gest_notification_priority");
            notificationCategory.removePreference(priorityPreference);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(SettingsProvider.KEY_GEST_NOTIFICATION) || key.equals(SettingsProvider.KEY_GEST_NOTIFICATION_PRIORITY)) {
            boolean notificationEnabled =
                    sharedPreferences.getBoolean(SettingsProvider.KEY_GEST_NOTIFICATION, false);
            GestureNotificationManager shortcutNotificationManager = new GestureNotificationManager();
            shortcutNotificationManager.cancelNotification(this);
            if (notificationEnabled) {
                final String strPriority =
                        sharedPreferences.getString(SettingsProvider.KEY_GEST_NOTIFICATION_PRIORITY,
                                "low");
                final int priority = GestureNotificationManager.getPriorityFromString(strPriority);
                shortcutNotificationManager.showNotification(this, priority);
            }
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPause() {
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
        finish();
    }

}