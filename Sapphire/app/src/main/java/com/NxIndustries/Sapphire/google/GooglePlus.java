package com.NxIndustries.Sapphire.google;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by ry on 10/13/15.
 */
public class GooglePlus extends Activity {

    @SuppressLint("ShowToast")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent("android.intent.action.MAIN");
        {
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.HomeActivity");
        }
        try {
            this.startActivity(intent);
        }
        catch (ActivityNotFoundException i) {
            Toast.makeText(this, ("Google Intent not found..."), (Toast.LENGTH_SHORT)).show();
        }
        this.finish();
    }
}