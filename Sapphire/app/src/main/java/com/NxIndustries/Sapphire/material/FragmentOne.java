package com.NxIndustries.Sapphire.material;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.NxIndustries.Sapphire.IconPackHelper;
import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.settings.GeneralActivity;
import com.NxIndustries.Sapphire.settings.Helpers;
import com.NxIndustries.Sapphire.welcome.Intro;

/**
 * Created by ry on 5/31/16.
 */

public class FragmentOne extends Fragment {

    public static final String ARGS_INSTANCE = "com.NxIndustries.Sapphire.argsInstance";

    public static FragmentOne newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FragmentOne fragment = new FragmentOne();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard_one, container, false);
        setHasOptionsMenu(true);

        final ImageView util = (ImageView) v.findViewById(R.id.util);

        LinearLayout gen = (LinearLayout) v.findViewById(R.id.general);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), util, "utillity");
                startActivity(intent, options.toBundle());
            }
        });
        LinearLayout ip = (LinearLayout) v.findViewById(R.id.icon_packs);
        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconPackHelper.pickIconPack(getActivity(), false);
            }
        });

        LinearLayout wp = (LinearLayout) v.findViewById(R.id.papers);
        wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallpaperChooser();
            }
        });

        LinearLayout home = (LinearLayout) v.findViewById(R.id.home_manage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intentSettings = new Intent(Settings.ACTION_HOME_SETTINGS);
                intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentSettings);
            }
        });
        LinearLayout home_set = (LinearLayout) v.findViewById(R.id.default_launcher);
        home_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getActivity().getPackageManager();
                ComponentName componentName = new ComponentName(getActivity(), com.NxIndustries.Sapphire.util.FakeLauncherActivity.class);
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                Intent selector = new Intent(Intent.ACTION_MAIN);
                selector.addCategory(Intent.CATEGORY_HOME);
                selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(selector);

                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
            }
        });
        LinearLayout startup = (LinearLayout) v.findViewById(R.id.startup);
        startup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Intro.class);
                startActivity(intent);
            }
        });
        LinearLayout restart = (LinearLayout) v.findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.restartLauncher();
            }
        });
        LinearLayout reset = (LinearLayout) v.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getResources().getString(R.string.reset_title));
                alertDialog.setMessage(getResources().getString(R.string.reset_summary));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Helpers.resetLauncher();
                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });
        LinearLayout assist = (LinearLayout) v.findViewById(R.id.tap);
        assist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS);
                startActivity(intent);

                try {
                    getActivity().startActivity(intent);
                } catch (ActivityNotFoundException i) {
                    showWarning();
                }
            }
        });

        return v;
    }

    private void showWarning() {
        Toast.makeText(getActivity(), "Firmware isn't supported",
                Toast.LENGTH_LONG).show();
    }

    private void wallpaperChooser() {
            Intent paper = new Intent("android.intent.action.MAIN");
            {
                paper.setClassName("sapphyx.gsd.com.drywall", "sapphyx.gsd.com.drywall.MainActivity");
            }
        try {
            startActivity(paper);
        }
        catch (ActivityNotFoundException i) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(getResources().getString(R.string.drywall_notice));
            alertDialog.setMessage(getResources().getString(R.string.drywall_summary));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=sapphyx.gsd.com.drywall"));
                            startActivity(browserIntent);
                        }
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.show();
        }
    }
}