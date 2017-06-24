package com.NxIndustries.Sapphire.welcome;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.NxIndustries.Sapphire.R;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by ry on 2/16/17.
 */

public class Intro extends AppIntro {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setImmersiveMode(true);
        setGoBackLock(true);
        showSkipButton(false);

        setProgressButtonEnabled(true);

        setBarColor(getResources().getColor(R.color.blue));
        setSeparatorColor(getResources().getColor(R.color.htc_grey));

        setFadeAnimation();

        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS , Manifest.permission.BLUETOOTH}, 2);

        addSlide(new FirstFragment());
        addSlide(new SecondFragment());
        addSlide(new FinalFragment());
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finish();
    }
}