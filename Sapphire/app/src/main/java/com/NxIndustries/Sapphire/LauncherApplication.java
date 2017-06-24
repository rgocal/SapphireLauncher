package com.NxIndustries.Sapphire;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.util.Log;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class LauncherApplication extends Application {

    public static boolean LAUNCHER_SHOW_UNREAD_NUMBER;
    private static final String TAG = "LauncherCrash";

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        CustomActivityOnCrash.setRestartActivityClass(Launcher.class);
        CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.mipmap.ic_launcher_two);
        CustomActivityOnCrash.install(this);


        LAUNCHER_SHOW_UNREAD_NUMBER = getResources().getBoolean(
                R.bool.config_launcher_show_unread_number);
        LauncherAppState.setApplicationContext(this);
        LauncherAppState.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LauncherAppState.getInstance().onTerminate();
    }

}