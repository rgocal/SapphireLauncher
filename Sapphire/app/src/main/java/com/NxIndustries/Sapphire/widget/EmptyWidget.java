package com.NxIndustries.Sapphire.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.NxIndustries.Sapphire.R;

import java.util.Arrays;

/**
 * Created by ry on 8/12/16.
 * Created to allow users to add extra screens to aosp launcher for wallpaper scrolling behavior
 */
public class EmptyWidget extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.i("ClockWidget", "Updating widget" + Arrays.asList(appWidgetIds));

        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_blank);

            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
