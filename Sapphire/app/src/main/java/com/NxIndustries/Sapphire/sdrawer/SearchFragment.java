package com.NxIndustries.Sapphire.sdrawer;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NxIndustries.Sapphire.LauncherAppState;
import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.settings.SettingsProvider;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int sNavigationBarHeightMultiplier = 1;
    private static final int sGridViewTopRowExtraPaddingInDP = 56;
    private static final int sMarginFromNavigationBarInDp = 16;
    private static final int sGridItemHeightInDp = 96;
    private static final int sInitialArrayListSize = 300;

    private final Pattern mPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private int mStatusBarHeight;
    private ArrayList<LaunchableActivity> mActivityInfos;
    private ArrayList<LaunchableActivity> mShareableActivityInfos;
    private Trie<LaunchableActivity> mTrie;
    private ArrayAdapter<LaunchableActivity> mArrayAdapter;
    private HashMap<String, List<LaunchableActivity>> mLaunchableActivityPackageNameHashMap;
    private LaunchableActivityPrefs mLaunchableActivityPrefs;
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private Drawable mDefaultAppIcon;
    private SimpleTaskConsumerManager mImageLoadingConsumersManager;
    private ImageLoadingTask.SharedData mImageTasksSharedData;
    private int mIconSizePixels;
    private EditText mSearchEditText;
    private int numOfCores;
    private BroadcastReceiver packageChangedReceiver;
    private final TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            updateVisibleApps();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private InputMethodManager mInputMethodManager;
    private AdapterView mAppListView;
    private PackageManager mPm;
    private int mColumnCount;
    private RelativeLayout rl;
    private LinearLayout ll;

    private StringBuilder mWordSinceLastSpaceBuilder;
    private StringBuilder mWordSinceLastCapitalBuilder;

    private int mGridViewTopRowHeight;
    private int mGridViewBottomRowHeight;

    public static int pxFromDp(float size, DisplayMetrics metrics) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                size, metrics));
    }

    private View.OnTouchListener mHapticFeedbackTouchListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_search_two, container, false);

        mPm = getActivity().getPackageManager();
        LauncherAppState app = LauncherAppState.getInstance();

        final Resources resources = getResources();

        //fields:
        mLaunchableActivityPackageNameHashMap = new HashMap<>();
        mShareableActivityInfos = new ArrayList<>(sInitialArrayListSize);
        mActivityInfos = new ArrayList<>(sInitialArrayListSize);
        mTrie = new Trie<>();
        mWordSinceLastSpaceBuilder = new StringBuilder(64);
        mWordSinceLastCapitalBuilder = new StringBuilder(64);

        rl = (RelativeLayout) v.findViewById(R.id.masterLayout);
        ll = (LinearLayout) v.findViewById(R.id.customActionBar);

        mSearchEditText = (EditText) v.findViewById(R.id.editText1);
        mAppListView = (GridView) v.findViewById(R.id.appsContainer);

        mContext = getActivity().getApplicationContext();
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mStatusBarHeight = StatusBarColorHelper.getStatusBarHeight(resources);
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        final float displayDensity = displayMetrics.density;

        final int gridViewTopRowExtraPaddingInPixels =
                Math.round(displayDensity * sGridViewTopRowExtraPaddingInDP);
        final int marginFromNavigationBarInPixels =
                Math.round(displayDensity * sMarginFromNavigationBarInDp);
        final int gridItemHeightInPixels =
                Math.round(displayDensity * sGridItemHeightInDp);
        mGridViewTopRowHeight = mStatusBarHeight +
                gridViewTopRowExtraPaddingInPixels;
        mGridViewBottomRowHeight = gridItemHeightInPixels + sNavigationBarHeightMultiplier *
                StatusBarColorHelper.getNavigationBarHeight(getResources()) +
                marginFromNavigationBarInPixels;

        //Column Customization numbers
        float dpWidth = displayMetrics.widthPixels / displayDensity;
        final float itemWidth = 72;


        mColumnCount = (int) (dpWidth / itemWidth) - 1;

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mLaunchableActivityPrefs = new LaunchableActivityPrefs(getActivity());

        //noinspection deprecation
        mDefaultAppIcon = resources.getDrawable(R.mipmap.ic_all_apps);
        float DEFAULT_ICON_SIZE_DP = (SettingsProvider.getInt(mContext,
                SettingsProvider.KEY_EXTENDEDHOME_ICON_SIZE, 48));
        mIconSizePixels = pxFromDp(DEFAULT_ICON_SIZE_DP, displayMetrics);

        numOfCores = Runtime.getRuntime().availableProcessors();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");
        packageChangedReceiver = new PackageChangedReceiver();
        getActivity().registerReceiver(packageChangedReceiver, filter);
        loadLaunchableApps();
        setupImageLoadingThreads(resources);
        setupViews();

        return v;

    }

    @Override
    public void onResume() {super.onResume();
        mSearchEditText.clearFocus();
        packageChangedReceiver = new PackageChangedReceiver();
    }

    @Override
    public void onDestroy() {
        try{
            if(packageChangedReceiver!=null)
                getActivity().unregisterReceiver(packageChangedReceiver);
        }catch(Exception ignored)
        {

        }
        super.onDestroy();

    }

    private void setupViews() {
        rl.setBackgroundColor(SettingsProvider.getInt(getActivity(),
                SettingsProvider.KEY_EDRAWER_BACKGROUND, R.color.htc_grey));
        ll.setBackgroundColor(SettingsProvider.getInt(getActivity(),
                SettingsProvider.KEY_EBAR_BACKGROUND, R.color.blue));

            mSearchEditText.addTextChangedListener(mTextWatcher);
            mSearchEditText.setTextColor((SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_SEARCH_ACTIVE_BACKGROUND, R.color.black)));
            mSearchEditText.setHintTextColor((SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_SEARCH_UNACTIVE_BACKGROUND, R.color.white)));

            mSearchEditText.setImeActionLabel(getString(R.string.launch), EditorInfo.IME_ACTION_GO);
            mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        Log.d("KEYBOARD", "ACTION_GO");
                        return openFirstActivity();
                    }
                    return false;
                }
            });
            mSearchEditText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        Log.d("KEYBOARD", "ENTER_KEY");
                        return openFirstActivity();
                    }
                    return false;
                }
            });
            mSearchEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    return false;
                }
            });

            registerForContextMenu(mAppListView);
            ((GridView) mAppListView).setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState != SCROLL_STATE_IDLE) {
                        hideKeyboard();
                        switch (scrollState) {
                            case 2: // SCROLL_STATE_FLING
                                break;

                            case 1: // SCROLL_STATE_TOUCH_SCROLL
                                break;

                            case 0: // SCROLL_STATE_IDLE
                                break;

                            default:
                                break;
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                }
            });
            //noinspection unchecked
            mAppListView.setAdapter(mArrayAdapter);

            mAppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= mColumnCount) {
                        launchActivity(mActivityInfos.get(position - mColumnCount));
                    }
                }

            });
        mAppListView.setOnTouchListener(getHapticFeedbackTouchListener());
        }

    public View.OnTouchListener getHapticFeedbackTouchListener() {
        if (mHapticFeedbackTouchListener == null) {
            mHapticFeedbackTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    }
                    return false;
                }
            };
        }
        return mHapticFeedbackTouchListener;
    }

    private boolean openFirstActivity() {
        if (!mActivityInfos.isEmpty()) {
            launchActivity(mActivityInfos.get(0));
            return true;
        }
        return false;
    }

    private void setupImageLoadingThreads(final Resources resources) {

        mImageLoadingConsumersManager =
                new SimpleTaskConsumerManager(getOptimalNumberOfThreads(resources),
                        mActivityInfos.size());
        mImageTasksSharedData = new ImageLoadingTask.SharedData(getActivity(), mPm, mContext, mIconSizePixels);
    }


    private int getOptimalNumberOfThreads(final Resources resources) {
        final int maxThreads = resources.getInteger(R.integer.max_imageloading_threads);
        int numThreads = numOfCores - 1;
        //clamp numThreads
        if (numThreads < 1) numThreads = 1;
        else if (numThreads > maxThreads) numThreads = maxThreads;
        return numThreads;
    }

    private void updateApps(final List<LaunchableActivity> updatedActivityInfos, boolean addToTrie) {

        for (LaunchableActivity launchableActivity : updatedActivityInfos) {
            final String packageName = launchableActivity.getComponent().getPackageName();
            mLaunchableActivityPackageNameHashMap.remove(packageName);
        }

        final String thisClassCanonicalName=this.getClass().getCanonicalName();
        for (LaunchableActivity launchableActivity : updatedActivityInfos) {
            final String className = launchableActivity.getComponent().getClassName();
            //don't show this activity in the launcher
            if (className.equals(thisClassCanonicalName)) {
                continue;
            }

            if (addToTrie) {
                final String activityLabel = launchableActivity.getActivityLabel().toString();
                final List<String> subwords = getAllSubwords(stripAccents(activityLabel));
                for (String subword : subwords) {
                    mTrie.put(subword, launchableActivity);
                }
            }
            final String packageName = launchableActivity.getComponent().getPackageName();

            List<LaunchableActivity> launchableActivitiesToUpdate =
                    mLaunchableActivityPackageNameHashMap.remove(packageName);
            if (launchableActivitiesToUpdate == null) {
                launchableActivitiesToUpdate = new LinkedList<>();
            }
            launchableActivitiesToUpdate.add(launchableActivity);
            mLaunchableActivityPackageNameHashMap.put(packageName, launchableActivitiesToUpdate);
        }
        Log.d("SearchActivity", "updated activities: " + updatedActivityInfos.size());
        mLaunchableActivityPrefs.setAllPreferences(updatedActivityInfos);
        updateVisibleApps();
    }

    private List<String> getAllSubwords(String line) {
        final ArrayList<String> subwords = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            final char character = line.charAt(i);

            if (Character.isUpperCase(character) || Character.isDigit(character)) {
                if (mWordSinceLastCapitalBuilder.length() > 1) {
                    subwords.add(mWordSinceLastCapitalBuilder.toString().toLowerCase());
                }
                mWordSinceLastCapitalBuilder.setLength(0);

            }
            if (Character.isSpaceChar(character)) {
                subwords.add(mWordSinceLastSpaceBuilder.toString().toLowerCase());
                if (mWordSinceLastCapitalBuilder.length() > 1 &&
                        mWordSinceLastCapitalBuilder.length() !=
                                mWordSinceLastSpaceBuilder.length()) {
                    subwords.add(mWordSinceLastCapitalBuilder.toString().toLowerCase());
                }
                mWordSinceLastCapitalBuilder.setLength(0);
                mWordSinceLastSpaceBuilder.setLength(0);
            } else {
                mWordSinceLastCapitalBuilder.append(character);
                mWordSinceLastSpaceBuilder.append(character);
            }
        }
        if (mWordSinceLastSpaceBuilder.length() > 0) {
            subwords.add(mWordSinceLastSpaceBuilder.toString().toLowerCase());
        }
        if (mWordSinceLastCapitalBuilder.length() > 1
                && mWordSinceLastCapitalBuilder.length() != mWordSinceLastSpaceBuilder.length()) {
            subwords.add(mWordSinceLastCapitalBuilder.toString().toLowerCase());
        }
        mWordSinceLastSpaceBuilder.setLength(0);
        mWordSinceLastCapitalBuilder.setLength(0);
        return subwords;
    }

    private void updateVisibleApps() {
        final HashSet<LaunchableActivity> infoList =
                mTrie.getAllStartingWith(stripAccents(mSearchEditText.getText()
                        .toString().toLowerCase().trim()));
        mActivityInfos.clear();
        mActivityInfos.addAll(infoList);
        mActivityInfos.addAll(mShareableActivityInfos);
        Collections.sort(mActivityInfos);
        Log.d("DEBUG_SEARCH", mActivityInfos.size() + "");

        mArrayAdapter.notifyDataSetChanged();
    }

    private void removeActivitiesFromPackage(String packageName) {
        final List<LaunchableActivity> launchableActivitiesToRemove =
                mLaunchableActivityPackageNameHashMap.remove(packageName);
        if (launchableActivitiesToRemove == null) {
            return;
        }
        boolean activityListChanged = false;

        for (LaunchableActivity launchableActivityToRemove : launchableActivitiesToRemove) {
            final String className = launchableActivityToRemove.getClassName();
            Log.d("SearchActivity", "removing activity " + className);
            String activityLabel = launchableActivityToRemove.getActivityLabel().toString();
            final List<String> subwords = getAllSubwords(stripAccents(activityLabel));
            for (String subword : subwords) {
                mTrie.remove(subword, launchableActivityToRemove);
            }
            if (mActivityInfos.remove(launchableActivityToRemove))
                activityListChanged = true;
            //TODO DEBUGME if uncommented the next line causes a crash.
            //mLaunchableActivityPrefs.deletePreference(className);
        }

        if (activityListChanged)
            mArrayAdapter.notifyDataSetChanged();
    }

    private String stripAccents(final String s) {
        return mPattern.matcher(Normalizer.normalize(s, Normalizer.Form.NFKD)).replaceAll("");
    }

    private void loadLaunchableApps() {

        List<ResolveInfo> infoList = ContentShare.getLaunchableResolveInfos(mPm);
        mArrayAdapter = new ActivityInfoArrayAdapter(getActivity(),
                R.layout.app_grid_item, mActivityInfos);
        ArrayList<LaunchableActivity> launchablesFromResolve = new ArrayList<>(infoList.size());

        if (numOfCores <= 1) {
            for (ResolveInfo info : infoList) {
                final LaunchableActivity launchableActivity = new LaunchableActivity(
                        info.activityInfo, info.activityInfo.loadLabel(mPm).toString(), false);
                launchablesFromResolve.add(launchableActivity);
            }
        } else {
            SimpleTaskConsumerManager simpleTaskConsumerManager =
                    new SimpleTaskConsumerManager(numOfCores,infoList.size());

            LoadLaunchableActivityTask.SharedData sharedAppLoadData =
                    new LoadLaunchableActivityTask.SharedData(mPm, launchablesFromResolve);
            for (ResolveInfo info : infoList) {
                LoadLaunchableActivityTask loadLaunchableActivityTask =
                        new LoadLaunchableActivityTask(info, sharedAppLoadData);
                simpleTaskConsumerManager.addTask(loadLaunchableActivityTask);
            }

            //Log.d("MultithreadStartup","waiting for completion of all tasks");
            simpleTaskConsumerManager.destroyAllConsumers(true, true);
            //Log.d("MultithreadStartup", "all tasks ok");
        }
        updateApps(launchablesFromResolve, true);
    }

    private void showKeyboard() {
        mInputMethodManager.showSoftInput(mSearchEditText, 0);
    }

    private void hideKeyboard() {
        mInputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
    }

    private void handlePackageChanged() {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        final String[] packageChangedNames = mSharedPreferences.getString("package_changed_name", "")
                .split(" ");
        editor.putString("package_changed_name", "");
        editor.apply();

        for (String packageName : packageChangedNames) {
            packageName = packageName.trim();
            if (packageName.isEmpty()) continue;

            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(packageName);
            Log.d("SearchActivity", "changed: " + packageName);
            final List<ResolveInfo> infoList = mPm.queryIntentActivities(intent, 0);

            //we don't actually need to run removeActivitiesFromPackage if the package
            // is being installed
            removeActivitiesFromPackage(packageName);


            if (infoList.isEmpty()) {
                Log.d("SearchActivity", "No activities in list. Uninstall detected!");
                updateVisibleApps();
            } else {
                Log.d("SearchActivity", "Activities in list. Install/update detected!");
                ArrayList<LaunchableActivity> launchablesFromResolve = new ArrayList<>(infoList.size());
                for (ResolveInfo info : infoList) {
                    final LaunchableActivity launchableActivity = new LaunchableActivity(
                            info.activityInfo, info.activityInfo.loadLabel(mPm).toString(), false);
                    launchablesFromResolve.add(launchableActivity);
                }
                updateApps(launchablesFromResolve, true);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("package_changed_name") && !sharedPreferences.getString(key, "").isEmpty()) {
            //does this need to run in uiThread?
            handlePackageChanged();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
            AdapterView.AdapterContextMenuInfo adapterMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(
                    ((LaunchableActivity) adapterMenuInfo.targetView
                            .findViewById(R.id.appIcon).getTag()).getActivityLabel());
        }
        final MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.app, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final View itemView = info.targetView;
        final LaunchableActivity launchableActivity =
                (LaunchableActivity) itemView.findViewById(R.id.appIcon).getTag();
        switch (item.getItemId()) {
            case R.id.appmenu_launch:
                launchActivity(launchableActivity);
                return true;
            case R.id.appmenu_info:
                final Intent intent = new Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:"
                        + launchableActivity.getComponent().getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.appmenu_onplaystore:
                final Intent intentPlayStore = new Intent(Intent.ACTION_VIEW);
                intentPlayStore.setData(Uri.parse("market://details?id=" +
                        launchableActivity.getComponent().getPackageName()));
                startActivity(intentPlayStore);
                return true;
            default:
                return false;
        }

    }

    public void launchActivity(final LaunchableActivity launchableActivity) {

        hideKeyboard();
        try {
            startActivity(launchableActivity.getLaunchIntent(mSearchEditText.getText().toString()));
            //Causes app to go first to list
            launchableActivity.setLaunchTime();
            mLaunchableActivityPrefs.writePreference(launchableActivity.getClassName(),
                    launchableActivity.getLaunchTime(), false);
            //
            //
            Collections.sort(mActivityInfos);
            mArrayAdapter.notifyDataSetChanged();
        } catch (ActivityNotFoundException e) {
            //this should only happen when the launcher still hasn't updated the file list after
            //an activity removal.
            Toast.makeText(mContext, getString(R.string.activity_not_found),
                    Toast.LENGTH_SHORT).show();
        }


    }

    class PopupEventListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return onOptionsItemSelected(item);
        }
    }

    class ActivityInfoArrayAdapter extends ArrayAdapter<LaunchableActivity> {
        final LayoutInflater inflater;

        public ActivityInfoArrayAdapter(final Context context, final int resource,
                                        final List<LaunchableActivity> activityInfos) {

            super(context, resource, activityInfos);
            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return super.getCount() + mColumnCount;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final View view =
                    convertView != null ?
                            convertView : inflater.inflate(R.layout.app_grid_item, parent, false);
            final AbsListView.LayoutParams params =
                    (AbsListView.LayoutParams) view.getLayoutParams();

            if (position < mColumnCount) {
                params.height = mGridViewTopRowHeight;
                view.setLayoutParams(params);
                view.setVisibility(View.INVISIBLE);
            } else {
                if (position == (getCount() - 1)) {
                    params.height = mGridViewBottomRowHeight;
                } else {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                view.setLayoutParams(params);
                view.setVisibility(View.VISIBLE);
                final LaunchableActivity launchableActivity = getItem(position - mColumnCount);
                final CharSequence label = launchableActivity.getActivityLabel();

                final TextView appLabelView = (TextView) view.findViewById(R.id.appLabel);
                appLabelView.setTextColor(SettingsProvider.getInt(getActivity(),
                        SettingsProvider.KEY_TEXT_BACKGROUND, R.color.white));

                final ImageView appIconView = (ImageView) view.findViewById(R.id.appIcon);
                final View appShareIndicator = view.findViewById(R.id.appShareIndicator);

                appLabelView.setText(label);

                appIconView.setTag(launchableActivity);
                if (!launchableActivity.isIconLoaded()) {
                    appIconView.setImageDrawable(mDefaultAppIcon);
                    mImageLoadingConsumersManager.addTask(
                            new ImageLoadingTask(appIconView, launchableActivity,
                                    mImageTasksSharedData));
                } else {
                    appIconView.setImageDrawable(
                            launchableActivity.getActivityIcon(mPm, mContext, mIconSizePixels));
                }
                appShareIndicator.setVisibility(
                        launchableActivity.isShareable() ? View.VISIBLE : View.GONE);
            }
            return view;
        }
    }
}