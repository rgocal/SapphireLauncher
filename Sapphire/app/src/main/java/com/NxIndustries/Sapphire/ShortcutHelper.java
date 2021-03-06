package com.NxIndustries.Sapphire;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class ShortcutHelper {

    public static final String SAPPHIRE_SHORTCUT = "sapphire_shortcut";
    public static final String SHORTCUT_VALUE = "shortcut_value";

    public static final String SHORTCUT_ALL_APPS = "**open_app_drawer**";
    public static final String SHORTCUT_OVERVIEW = "**overview_mode**";
    public static final String SHORTCUT_SETTINGS = "**launcher_settings**";
    public static final String SHORTCUT_DEFAULT_PAGE = "**default_homescreen**";
    public static final String SHORTCUT_GESTURE = "**launcher_gesture**";
    public static final String SHORTCUT_SDRAWER = "**open_sdrawer**";

    public static Bitmap getIcon(Context context, String value) {
        if (value.equals(SHORTCUT_ALL_APPS)) {
            return drawableToBitmap(
                    context.getResources()
                            .getDrawable(R.mipmap.ic_all_apps));
        } else if (value.equals(SHORTCUT_OVERVIEW)) {
            return drawableToBitmap(
                    context.getResources()
                            .getDrawable(R.mipmap.ic_overview));
        } else if (value.equals(SHORTCUT_SETTINGS)) {
            return drawableToBitmap(context.getResources()
                    .getDrawable(R.mipmap.ic_settings));
        } else if (value.equals(SHORTCUT_GESTURE)) {
            return drawableToBitmap(context.getResources()
                    .getDrawable(R.mipmap.ic_gesture));
        } else if (value.equals(SHORTCUT_SDRAWER)) {
            return drawableToBitmap(context.getResources()
                    .getDrawable(R.mipmap.ic_drawer));
        } else if (value.equals(SHORTCUT_DEFAULT_PAGE)) {
            return drawableToBitmap(context.getResources()
                    .getDrawable(R.mipmap.ic_homescreen));
        }
        return drawableToBitmap(
                context.getResources().getDrawable(R.mipmap.ic_launcher_home));
    }

    public static Bitmap drawableToBitmap(Drawable d) {
        if (d == null)
            return null;
        if (d instanceof BitmapDrawable) {
            return ((BitmapDrawable) d).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),
                d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return bitmap;
    }
}