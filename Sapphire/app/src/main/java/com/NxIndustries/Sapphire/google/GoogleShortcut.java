package com.NxIndustries.Sapphire.google;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.NxIndustries.Sapphire.R;

/**
 * Created by ry on 10/8/15.
 */
public class GoogleShortcut extends Activity {

    @SuppressLint("ShowToast")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        /**
         * Ok Google!
         */
        Intent intent = new Intent("android.intent.action.MAIN");
        {
            intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.SearchActivity");
        }
        try {
            this.startActivity(intent);
        }
        catch (ActivityNotFoundException i) {
            Toast.makeText((this), ("Google Play Services not found..."), (Toast.LENGTH_SHORT)).show();
        }
        this.finish();
    }
}
