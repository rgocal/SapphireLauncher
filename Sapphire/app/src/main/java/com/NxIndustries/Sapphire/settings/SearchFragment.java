package com.NxIndustries.Sapphire.settings;

/**
 * Created by ry on 6/20/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.NxIndustries.Sapphire.R;

public class SearchFragment extends SettingsPreferenceFragmentStock{

    final CharSequence[] items = {"Basic License", "Premium License", "Onyx License"};

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.search_widget);

        boolean basic = appInstalledOrNot("com.NxIndustries.Sapphire1NE");
        boolean premium = appInstalledOrNot("sapphireprem.nxindustries.com.sapphirepremium");
        boolean onyx = appInstalledOrNot("onyx.sapphire.nxindustires.com.suionyx");
        if (basic) {
            System.out.println("License Detected");
        } else if (premium) {
            System.out.println("License Detected");
        } else if (onyx) {
            System.out.println("License Detected");
        } else {
            //Donation Snackbar
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content), R.string.snack_support, Snackbar.LENGTH_LONG)
                    .setAction(R.string.donate, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDonation();
                        }
                    });
            snackbar.setActionTextColor(getResources().getColor(R.color.white));
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.blue));
            TextView textView = (TextView)
                    snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.white));
            snackbar.show();

            System.out.println("Keys not found");
        }

        Preference apps = findPreference(SettingsProvider.KEY_SEARCH_APPLY);
        apps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Helpers.restartLauncher();
                return true;
            }
        });
    }

    private void showDonation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Purchase License");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Basic License")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.NxIndustries.Sapphire1NE"));
                    startActivity(intent);
                } else if (items[item].equals("Premium License")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=sapphireprem.nxindustries.com.sapphirepremium"));
                    startActivity(intent);
                } else if (items[item].equals("Onyx License")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=onyx.sapphire.nxindustires.com.suionyx"));
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }
}