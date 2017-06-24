package com.NxIndustries.Sapphire.sdrawer;

import android.content.res.Resources;


public class StatusBarColorHelper {

    public static int getStatusBarHeight(final Resources resources) {

        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    public static int getNavigationBarHeight(final Resources resources) {

        final int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }


}
