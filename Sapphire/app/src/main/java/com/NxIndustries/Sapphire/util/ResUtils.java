package com.NxIndustries.Sapphire.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.IllegalFormatException;
import java.util.Locale;

public class ResUtils {

    @NonNull
    public static String getString(@NonNull Context context, @StringRes int id, Object... formatArgs) {
        return getString(context.getResources(), id, formatArgs);
    }

    @NonNull
    public static String getString(@NonNull Resources resources, @StringRes int id, Object... formatArgs) {
        try {
            return resources.getString(id, formatArgs);
        } catch (IllegalFormatException e) {
            final String message = "Failed to format the string resource! The user locale is:"
                    + Locale.getDefault().toString();
            throw new IllegalArgumentException(message, e);
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableRes) {
        return Device.hasLollipopApi()
                ? context.getResources().getDrawable(drawableRes, context.getTheme())
                : context.getResources().getDrawable(drawableRes);
    }

}
