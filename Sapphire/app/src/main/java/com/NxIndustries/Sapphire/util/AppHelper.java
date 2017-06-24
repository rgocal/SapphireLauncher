package com.NxIndustries.Sapphire.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.NxIndustries.Sapphire.R;

import java.net.URISyntaxException;

public class AppHelper {

    private static final String SETTINGS_METADATA_NAME = "com.android.settings";

    public static String getProperSummary(Context context, PackageManager pm,
                                          Resources settingsResources, String action, String values, String entries) {

        if (pm == null || settingsResources == null || action == null) {
            return context.getResources().getString(R.string.error_message_title);
        }

        if (values != null && entries != null) {
            int resIdEntries = -1;
            int resIdValues = -1;

            resIdEntries = settingsResources.getIdentifier(
                    SETTINGS_METADATA_NAME + ":array/" + entries, null, null);

            resIdValues = settingsResources.getIdentifier(
                    SETTINGS_METADATA_NAME + ":array/" + values, null, null);

            if (resIdEntries > 0 && resIdValues > 0) {
                try {
                    String[] entriesArray = settingsResources.getStringArray(resIdEntries);
                    String[] valuesArray = settingsResources.getStringArray(resIdValues);
                    for (int i = 0; i < valuesArray.length; i++) {
                        if (action.equals(valuesArray[i])) {
                            return entriesArray[i];
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return getFriendlyNameForUri(context, pm, action);
    }

    private static String getFriendlyActivityName(Context context,
                                                  PackageManager pm, Intent intent, boolean labelOnly) {
        ActivityInfo ai = intent.resolveActivityInfo(pm, PackageManager.GET_ACTIVITIES);
        String friendlyName = null;

        if (ai != null) {
            friendlyName = ai.loadLabel(pm).toString();
        }

        if (friendlyName == null || friendlyName.startsWith("#Intent;")) {
            return context.getResources().getString(R.string.error_message_title);
        }
        return friendlyName;
    }

    public static String getFriendlyShortcutName(
            Context context, PackageManager pm, Intent intent) {
        String activityName = getFriendlyActivityName(context, pm, intent, true);
        String name = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

        if (activityName == null || activityName.startsWith("#Intent;")) {
            return context.getResources().getString(R.string.error_message_title);
        }
        if (name != null) {
            return activityName + ": " + name;
        }
        return intent.toUri(0);
    }

    public static String getFriendlyNameForUri(
            Context context, PackageManager pm, String uri) {
        if (uri == null || uri.startsWith("**")) {
            return null;
        }

        try {
            Intent intent = Intent.parseUri(uri, 0);
            if (Intent.ACTION_MAIN.equals(intent.getAction())) {
                return getFriendlyActivityName(context, pm, intent, false);
            }
            return getFriendlyShortcutName(context, pm, intent);
        } catch (URISyntaxException ignored) {
        }

        return uri;
    }

    public static String getShortcutPreferred(
            Context context, PackageManager pm, String uri) {
        if (uri == null || uri.startsWith("**")) {
            return null;
        }
        try {
            Intent intent = Intent.parseUri(uri, 0);
            String name = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
            if (name == null || name.startsWith("#Intent;")) {
                return getFriendlyActivityName(context, pm, intent, false);
            }
            return name;
        } catch (URISyntaxException ignored) {
        }

        return uri;
    }

}