/* Copyright 2015 Hayai Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.NxIndustries.Sapphire.sdrawer;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class LaunchableActivity implements Comparable<LaunchableActivity> {

    private final ActivityInfo mActivityInfo;
    private final String mActivityLabel;
    private final ComponentName mComponentName;
    private Intent mLaunchIntent;
    private long lastLaunchTime;
    private boolean mShareable;
    private Drawable mActivityIcon;
    private boolean mFavorite;

    public LaunchableActivity(final ActivityInfo activityInfo, final String activityLabel,
                              final boolean isShareable) {
        this.mActivityInfo = activityInfo;
        this.mActivityLabel = activityLabel;
        mComponentName = new ComponentName(activityInfo.packageName, activityInfo.name);
        this.mShareable = isShareable;
    }
    public LaunchableActivity(final ComponentName componentName, final String label,
                              final Drawable activityIcon, final Intent launchIntent){
        this.mComponentName = componentName;
        this.mActivityLabel = label;
        this.mLaunchIntent = launchIntent;
        this.mActivityIcon = activityIcon;
        this.mActivityInfo = null;
    }

    public Intent getLaunchIntent(final String searchString) {
        if (mLaunchIntent != null)
            return mLaunchIntent;
        if (isShareable()) {
            final Intent launchIntent = ContentShare.shareTextIntent(searchString);
            launchIntent.setComponent(getComponent());
            return launchIntent;
        }
        final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchIntent.setComponent(mComponentName);
        return launchIntent;
    }

    public void setLaunchTime() {
        lastLaunchTime = System.currentTimeMillis() / 1000;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(final boolean favorite) {
        this.mFavorite = favorite;
    }

    public long getLaunchTime() {
        return lastLaunchTime;
    }

    public void setLaunchTime(long timestamp) {
        lastLaunchTime = timestamp;
    }

    public CharSequence getActivityLabel() {
        return mActivityLabel;
    }

    public boolean isIconLoaded() {
        return mActivityIcon != null;
    }

    public synchronized Drawable getActivityIcon(final PackageManager pm, final Context context,
                                                 final int iconSizePixels) {
        if (!isIconLoaded()) {
            Drawable _activityIcon = null;
                final ActivityManager activityManager =
                        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                final int iconDpi = activityManager.getLauncherLargeIconDensity();
                try {
                    //noinspection deprecation
                    _activityIcon =
                            pm.getResourcesForActivity(mComponentName).getDrawableForDensity(
                                    mActivityInfo.getIconResource(), iconDpi);

                } catch (PackageManager.NameNotFoundException | Resources.NotFoundException e) {
                    //if we get here, there's no icon to load.
                    //there's nothing to do, as the android default icon will be loaded
                }

                if (_activityIcon == null) {
                    //noinspection deprecation
                    _activityIcon = Resources.getSystem().getDrawable(
                            android.R.mipmap.sym_def_app_icon);
                }


            //rescaling the icon if it is bigger than the target size
            //TODO do this when it is not a bitmap drawable?
            if (_activityIcon instanceof BitmapDrawable) {
                if (_activityIcon.getIntrinsicHeight() > iconSizePixels &&
                        _activityIcon.getIntrinsicWidth() > iconSizePixels) {
                    //noinspection deprecation
                    _activityIcon = new BitmapDrawable(
                            Bitmap.createScaledBitmap(((BitmapDrawable) _activityIcon).getBitmap()
                                    , iconSizePixels, iconSizePixels, false));
                }
            }
            mActivityIcon = _activityIcon;
        }
        return mActivityIcon;
    }

    public ComponentName getComponent() {
        return mComponentName;
    }

    public String getClassName() {
        return mComponentName.getClassName();
    }

    public boolean isShareable() {
        return mShareable;
    }

    @Override
    public int compareTo(LaunchableActivity another) {
        return this.mActivityLabel.compareTo(another.mActivityLabel);
    }
}
