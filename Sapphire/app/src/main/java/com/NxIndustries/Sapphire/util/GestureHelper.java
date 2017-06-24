package com.NxIndustries.Sapphire.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.NxIndustries.Sapphire.AppsCustomizePagedView;
import com.NxIndustries.Sapphire.Launcher;
import com.NxIndustries.Sapphire.Workspace;
import com.NxIndustries.Sapphire.gesture.GestureLauncher;
import com.NxIndustries.Sapphire.material.BottomInterface;
import com.NxIndustries.Sapphire.sdrawer.SearchActivity;
import com.NxIndustries.Sapphire.settings.SettingsProvider;

import java.net.URISyntaxException;

public class GestureHelper {

    public static final String TAG = "GestureHelper";

    public static final String ACTION_DEFAULT_HOMESCREEN = "default_homescreen";
    public static final String ACTION_OPEN_APP_DRAWER = "open_app_drawer";
    public static final String ACTION_OVERVIEW_MODE = "show_previews";
    public static final String ACTION_LAUNCHER_SETTINGS = "show_settings";
    public static final String ACTION_GESTURE_LISTEN = "gesture_listen";
    public static final String ACTION_NEW_DRAWER = "new_drawer";
    public static final String ACTION_CUSTOM = "custom";

    static WindowManager wm;
    static Display display;
    static Point size;
    static int width;
    static int height;
    static int sector;

    static void init(Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        sector = width / 3;
    }

    public static void performGestureAction(Launcher launcher,
                                            String gestureAction, String gesture) {
        if (wm == null) {
            init(launcher);
        }
        Workspace workspace = launcher.getWorkspace();
        if (gestureAction.equals(ACTION_DEFAULT_HOMESCREEN)) {
            workspace.setCurrentPage(workspace.getPageIndexForScreenId(-1));
        } else if (gestureAction.equals(ACTION_OPEN_APP_DRAWER)) {
            launcher.showAllApps(true, AppsCustomizePagedView.ContentType.Applications, true);
        } else if (gestureAction.equals(ACTION_OVERVIEW_MODE)) {
            workspace.enterOverviewMode(true);
        } else if (gestureAction.equals(ACTION_LAUNCHER_SETTINGS)) {
            Intent preferences = new Intent().setClass(launcher, BottomInterface.class);
            preferences.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            launcher.startActivity(preferences);
        } else if (gestureAction.equals(ACTION_GESTURE_LISTEN)) {
            Intent preferences = new Intent().setClass(launcher, GestureLauncher.class);
            preferences.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            launcher.startActivity(preferences);
        } else if (gestureAction.equals(ACTION_NEW_DRAWER)) {
            Intent preferences = new Intent().setClass(launcher, SearchActivity.class);
            preferences.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            launcher.startActivity(preferences);
        } else if (gestureAction.equals(ACTION_CUSTOM)) {
            String uri = SettingsProvider.getString(launcher,
                    gesture + "_action_custom", "");
            if (!uri.equals("")) {
                try {
                    Intent intent = Intent.parseUri(uri, 0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launcher.startActivity(intent);
                } catch (URISyntaxException e) {
                    Log.e(TAG, "Unable to start gesture action " + gestureAction, e);
                }
            }
        }
    }

    public static Gesture identifyGesture(float upX, float upY, float downX, float downY) {

        if (isSwipeDOWN(upY, downY)) {

            if (isSwipeLEFT(downX)) {
                return Gesture.DOWN_LEFT;
            } else if (isSwipeRIGHT(downX)) {
                return Gesture.DOWN_RIGHT;
            } else if (isSwipeMIDDLE(downX)) {
                return Gesture.DOWN_MIDDLE;
            }
        } else if (isSwipeUP(upY, downY)) {
            if (isSwipeLEFT(downX)) {
                return Gesture.UP_LEFT;
            } else if (isSwipeRIGHT(downX)) {
                return Gesture.UP_RIGHT;
            } else if (isSwipeMIDDLE(downX)) {
                return Gesture.UP_MIDDLE;
            }
        }

        return Gesture.NONE;
    }

    public static boolean doesSwipeDownContainShowAllApps(Context context) {
        return (SettingsProvider.getString(context,
                SettingsProvider.LEFT_DOWN_GESTURE_ACTION, "").equals(ACTION_OPEN_APP_DRAWER)
                || SettingsProvider.getString(context,
                SettingsProvider.MIDDLE_DOWN_GESTURE_ACTION, "").equals(ACTION_OPEN_APP_DRAWER)
                || SettingsProvider.getString(context,
                SettingsProvider.RIGHT_DOWN_GESTURE_ACTION, "").equals(ACTION_OPEN_APP_DRAWER));
    }

    public static boolean doesSwipeUpContainShowAllApps(Context context) {
        return (SettingsProvider.getString(context,
                SettingsProvider.LEFT_UP_GESTURE_ACTION, "").equals(ACTION_OPEN_APP_DRAWER)
                || SettingsProvider.getString(context,
                SettingsProvider.MIDDLE_UP_GESTURE_ACTION, "").equals(ACTION_OPEN_APP_DRAWER)
                || SettingsProvider.getString(context,
                SettingsProvider.RIGHT_UP_GESTURE_ACTION, "").equals(ACTION_OPEN_APP_DRAWER));
    }

    public static boolean isSwipeDOWN(float upY, float downY) {
        return (upY - downY) > Workspace.MIN_UP_DOWN_GESTURE_DISTANCE;
    }

    public static boolean isSwipeUP(float upY, float downY) {
        return ((upY - downY) < -Workspace.MIN_UP_DOWN_GESTURE_DISTANCE);
    }

    public static boolean isSwipeLEFT(float downX) {
        return downX < sector;
    }

    public static boolean isSwipeMIDDLE(float downX) {
        return downX > sector && downX < (sector * 2);
    }

    public static boolean isSwipeRIGHT(float downX) {
        return downX > (sector * 2);
    }

    public enum Gesture {
        DOWN_LEFT,
        DOWN_MIDDLE,
        DOWN_RIGHT,
        UP_LEFT,
        UP_MIDDLE,
        UP_RIGHT,
        DOUBLE_TAP,
        NONE
    }
}
