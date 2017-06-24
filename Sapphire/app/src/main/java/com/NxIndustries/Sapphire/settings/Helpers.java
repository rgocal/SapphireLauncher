package com.NxIndustries.Sapphire.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ry on 1/21/16.
 */
public class Helpers {

    public static final String PREF_TOS_ACCEPTED = "pref_tos_accepted";
    public static final String PREF_SECOND_ACCEPTED = "pref_second_accepted";
    public static final String PREF_THIRD_ACCEPTED = "pref_third_accepted";

    public static void restartLauncher() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void resetLauncher() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear com.NxIndustries.Sapphire");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTosAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_TOS_ACCEPTED, false);
    }

    public static void markTosAccepted(final Context context, boolean newValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_TOS_ACCEPTED, newValue).apply();
    }

    public static boolean isSecondAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_SECOND_ACCEPTED, false);
    }

    public static void markSecondAccepted(final Context context, boolean newValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_SECOND_ACCEPTED, newValue).apply();
    }

    public static boolean isThirdAccepted(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_THIRD_ACCEPTED, false);
    }

    public static void markThirdAccepted(final Context context, boolean newValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_THIRD_ACCEPTED, newValue).apply();
    }

}
