package com.NxIndustries.Sapphire.material;

/**
 * Created by ry on 7/14/16.
 */
import android.app.AlertDialog;
import android.app.ApplicationErrorReport;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.NxIndustries.Sapphire.LauncherAppState;
import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.development.Body;
import com.NxIndustries.Sapphire.preferences.Changelog;
import com.NxIndustries.Sapphire.settings.SettingsProvider;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.store.Store;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.psdev.licensesdialog.LicensesDialog;

public class BottomInterface extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    String email = "rgocal09@gmail.com";
    String subject = "Feedback";
    String body = "";
    String chooserTitle = "Contact via";

    private Drawer result = null;

    final CharSequence[] items = {"Basic License", "Premium License", "Onyx License"};
    final CharSequence[] bugs = {"Crash", "Application Not Responding", "Battery Consumption", "Running Background Services"};

    private int[] mTabsIcons = {
            R.drawable.bottombar_gen,
            R.drawable.bottombar_home,
            R.drawable.bottombar_gesture,
            R.drawable.menu_changelog};

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
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
    public void onResume() {
        super.onResume();
        SettingsProvider.get(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SettingsProvider.get(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_main);

        boolean basic = appInstalledOrNot("com.NxIndustries.Sapphire1NE");
        boolean premium = appInstalledOrNot("sapphireprem.nxindustries.com.sapphirepremium");
        boolean onyx = appInstalledOrNot("onyx.sapphire.nxindustires.com.suionyx");
        if (basic) {
            System.out.println("License Detected");
        }
        else if (premium) {
            System.out.println("License Detected");
        }
        else if (onyx) {
            System.out.println("License Detected");
        }
        else {
            //Donation Dialog
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getResources().getString(R.string.donation_title));
            alertDialog.setMessage(getResources().getString(R.string.donation_summary));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showDonation();
                        }
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.donation_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.show();
            System.out.println("Keys not found");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        AppCompatActivity activity = this;
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_toolbar);
        activity.getSupportActionBar().setTitle("Personalization");
        if (premium) {
            activity.getSupportActionBar().setSubtitle("Premium Subscriber");
        }
        else if (onyx) {
            activity.getSupportActionBar().setSubtitle("Onyx Subscriber!");
        }
        else {
            activity.getSupportActionBar().setSubtitle("Welcome");
        }

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));

        UpdateChecker checker = new UpdateChecker(this);
        UpdateChecker.setStore(Store.GOOGLE_PLAY);
        UpdateChecker.setNoticeIcon(R.mipmap.ic_launcher_two);
        UpdateChecker.start();

        result = new DrawerBuilder(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .withHeader(R.layout.settings_header)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Information"),
                        new PrimaryDrawerItem().withName("About").withIcon(R.drawable.menu_about).withIdentifier(1),
                        new PrimaryDrawerItem().withName("License").withIcon(R.drawable.menu_license).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Send Feedback").withIcon(R.drawable.menu_feedback).withIdentifier(4),
                        new PrimaryDrawerItem().withName("Bug Report").withIcon(R.drawable.menu_debug).withIdentifier(5),
                        new PrimaryDrawerItem().withName("Google Plus Login").withIcon(R.drawable.menu_feedback).withIdentifier(6)
                                ,
                        new ExpandableDrawerItem().withName("More").withIdentifier(0).withSelectable(false).withSubItems(
                        new PrimaryDrawerItem().withName("GSD").withDescription("GocalSD Web Home").withIdentifier(10),
                        new PrimaryDrawerItem().withName("Google Plus Group").withDescription("Discuss with the community").withIdentifier(11),
                        new PrimaryDrawerItem().withName("Privacy Policy").withDescription("Your data is secure with GSD").withIdentifier(13),
                        new PrimaryDrawerItem().withName("XDA Forums Thread").withDescription("Troubleshoot, Discuss, Request").withIdentifier(12)
                        )
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        long id = drawerItem.getIdentifier();
                        if (id == 1) {
                            Intent intent = new Intent(BottomInterface.this, Body.class);
                            startActivity(intent);
                        } else if (id == 3) {
                            new LicensesDialog.Builder(BottomInterface.this)
                                    .setNotices(R.raw.licenses)
                                    .build()
                                    .show();
                        } else if (id == 4) {
                            AlertDialog alertDialog = new AlertDialog.Builder(BottomInterface.this).create();
                            alertDialog.setTitle(getResources().getString(R.string.feedback_title));
                            alertDialog.setMessage(getResources().getString(R.string.feedback_summary));
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.email),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            sendEmail();
                                        }
                                    });
                            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getString(R.string.hangout),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startHangout();
                                        }
                                    });
                            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            alertDialog.show();
                        } else if (id == 5) {
                            showReport();
                        } else if (id == 6) {
                            Intent intent = new Intent(BottomInterface.this, Login.class);
                            startActivity(intent);
                        } else if (id == 10) {
                            Intent home = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gocalsd.weebly.com"));
                            startActivity(home);
                        } else if (id == 11) {
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/communities/115542523912843355119"));
                            startActivity(webIntent);
                        } else if (id == 12) {
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forum.xda-developers.com/android/apps-games/launcher-sapphire-ui-alphareboot-t3217519"));
                            startActivity(webIntent);
                        } else if (id == 13) {
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gocalsd.weebly.com/privacy.html"));
                            startActivity(webIntent);
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // Setup the viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
    }

    private void showReport() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BottomInterface.this);

        builder.setTitle("Report Log");
        builder.setItems(bugs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (bugs[item].equals("Crash")) {
                    try {
                        int i = 3 / 0;
                    } catch (Exception e) {
                        ApplicationErrorReport report = new ApplicationErrorReport();
                        report.packageName = report.processName = BottomInterface.this
                                .getPackageName();
                        report.time = System.currentTimeMillis();
                        report.type = ApplicationErrorReport.TYPE_CRASH;
                        report.systemApp = false;

                        ApplicationErrorReport.CrashInfo crash = new ApplicationErrorReport.CrashInfo();
                        crash.exceptionClassName = e.getClass().getSimpleName();
                        crash.exceptionMessage = e.getMessage();

                        StringWriter writer = new StringWriter();
                        PrintWriter printer = new PrintWriter(writer);
                        e.printStackTrace(printer);

                        crash.stackTrace = writer.toString();

                        StackTraceElement stack = e.getStackTrace()[0];
                        crash.throwClassName = stack.getClassName();
                        crash.throwFileName = stack.getFileName();
                        crash.throwLineNumber = stack.getLineNumber();
                        crash.throwMethodName = stack.getMethodName();

                        report.crashInfo = crash;

                        Intent intent = new Intent(Intent.ACTION_APP_ERROR);
                        intent.putExtra(Intent.EXTRA_BUG_REPORT, report);
                        startActivity(intent);
                    }
                } else if (bugs[item].equals("Application Not Responding")) {
                    try {
                        int i = 3 / 0;
                    } catch (Exception e) {
                        ApplicationErrorReport report = new ApplicationErrorReport();
                        report.packageName = report.processName = BottomInterface.this
                                .getPackageName();
                        report.time = System.currentTimeMillis();
                        report.type = ApplicationErrorReport.TYPE_ANR;
                        report.systemApp = false;

                        ApplicationErrorReport.CrashInfo crash = new ApplicationErrorReport.CrashInfo();
                        crash.exceptionClassName = e.getClass().getSimpleName();
                        crash.exceptionMessage = e.getMessage();

                        StringWriter writer = new StringWriter();
                        PrintWriter printer = new PrintWriter(writer);
                        e.printStackTrace(printer);

                        crash.stackTrace = writer.toString();

                        StackTraceElement stack = e.getStackTrace()[0];
                        crash.throwClassName = stack.getClassName();
                        crash.throwFileName = stack.getFileName();
                        crash.throwLineNumber = stack.getLineNumber();
                        crash.throwMethodName = stack.getMethodName();

                        report.crashInfo = crash;

                        Intent intent = new Intent(Intent.ACTION_APP_ERROR);
                        intent.putExtra(Intent.EXTRA_BUG_REPORT, report);
                        startActivity(intent);
                    }
                } else if (bugs[item].equals("Battery Consumption")) {
                    try {
                        int i = 3 / 0;
                    } catch (Exception e) {
                        ApplicationErrorReport report = new ApplicationErrorReport();
                        report.packageName = report.processName = BottomInterface.this
                                .getPackageName();
                        report.time = System.currentTimeMillis();
                        report.type = ApplicationErrorReport.TYPE_BATTERY;
                        report.systemApp = false;

                        ApplicationErrorReport.CrashInfo crash = new ApplicationErrorReport.CrashInfo();
                        crash.exceptionClassName = e.getClass().getSimpleName();
                        crash.exceptionMessage = e.getMessage();

                        StringWriter writer = new StringWriter();
                        PrintWriter printer = new PrintWriter(writer);
                        e.printStackTrace(printer);

                        crash.stackTrace = writer.toString();

                        StackTraceElement stack = e.getStackTrace()[0];
                        crash.throwClassName = stack.getClassName();
                        crash.throwFileName = stack.getFileName();
                        crash.throwLineNumber = stack.getLineNumber();
                        crash.throwMethodName = stack.getMethodName();

                        report.crashInfo = crash;

                        Intent intent = new Intent(Intent.ACTION_APP_ERROR);
                        intent.putExtra(Intent.EXTRA_BUG_REPORT, report);
                        startActivity(intent);
                    }
                } else if (bugs[item].equals("Running Background Services")) {
                    try {
                        int i = 3 / 0;
                    } catch (Exception e) {
                        ApplicationErrorReport report = new ApplicationErrorReport();
                        report.packageName = report.processName = BottomInterface.this
                                .getPackageName();
                        report.time = System.currentTimeMillis();
                        report.type = ApplicationErrorReport.TYPE_RUNNING_SERVICE;
                        report.systemApp = false;

                        ApplicationErrorReport.CrashInfo crash = new ApplicationErrorReport.CrashInfo();
                        crash.exceptionClassName = e.getClass().getSimpleName();
                        crash.exceptionMessage = e.getMessage();

                        StringWriter writer = new StringWriter();
                        PrintWriter printer = new PrintWriter(writer);
                        e.printStackTrace(printer);

                        crash.stackTrace = writer.toString();

                        StackTraceElement stack = e.getStackTrace()[0];
                        crash.throwClassName = stack.getClassName();
                        crash.throwFileName = stack.getFileName();
                        crash.throwLineNumber = stack.getLineNumber();
                        crash.throwMethodName = stack.getMethodName();

                        report.crashInfo = crash;

                        Intent intent = new Intent(Intent.ACTION_APP_ERROR);
                        intent.putExtra(Intent.EXTRA_BUG_REPORT, report);
                        startActivity(intent);
                    }
                }
            }
        });
        builder.show();
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 4;

        private final String[] mTabsTitle = {"General", "Homescreen", "Navigation", "Changelog"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(BottomInterface.this).inflate(R.layout.custom_tab, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return FragmentOne.newInstance(1);
                case 1:
                    return FragmentTwo.newInstance(2);
                case 2:
                    return FragmentThree.newInstance(3);
                case 3:
                    return Changelog.newInstance(4);
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }

    private void showDonation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

    private void startHangout(){
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "");
        this.startActivity(sendIntent);
    }

    protected void sendEmail() {
        ShareCompat.IntentBuilder.from(this)
                .setType("message/rfc822")
                .addEmailTo(email)
                .setSubject(subject)
                .setText(body)
                //.setHtmlText(body) //If you are using HTML in your body text
                .setChooserTitle(chooserTitle)
                .startChooser();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        LauncherAppState.setSettingsChanged();
    }
}