package com.NxIndustries.Sapphire.preferences;

import android.app.Activity;
import android.os.Bundle;

import com.NxIndustries.Sapphire.R;
import com.google.android.gms.plus.PlusOneButton;

/**
 * Created by ry on 1/8/16.
 */
public class Features_Review extends Activity {

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static final String URL = "https://play.google.com/store/apps/details?id=com.NxIndustries.Sapphire";
    private PlusOneButton mPlusOneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_review);

        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);

    }

    protected void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
        mPlusOneButton.initialize(URL, PLUS_ONE_REQUEST_CODE);
    }
}
