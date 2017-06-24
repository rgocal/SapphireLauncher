package com.NxIndustries.Sapphire.layout;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.NxIndustries.Sapphire.LauncherAppState;
import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.material.BottomInterface;
import com.NxIndustries.Sapphire.material.Login;
import com.NxIndustries.Sapphire.sdrawer.SearchActivity;
import com.NxIndustries.Sapphire.settings.SettingsProvider;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ry on 4/8/16.
 */

//Add new menu item for google login
    //replaced edit tedt field with text field
public class Taskbar extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    ImageButton action, apps, voice;
    SearchView search;
    RelativeLayout sbadge;

    private int mSearchRadius;

    private View.OnTouchListener mHapticFeedbackTouchListener;
    private int i;

    public static int pxFromDp(float size, DisplayMetrics metrics) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                size, metrics));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.taskbar, container, false);

        final Resources resources = getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        
        action = (ImageButton) v.findViewById(R.id.action_button);
        apps = (ImageButton) v.findViewById(R.id.allApps);
        voice = (ImageButton) v.findViewById(R.id.voice);
        sbadge = (RelativeLayout) v.findViewById(R.id.s_badge);

        sbadge.setBackgroundResource(R.drawable.round_search);

        float DEFAULT_DP_SIZE = (SettingsProvider.getInt(getActivity(),
                SettingsProvider.KEY_ROUND_SEARCH, 8));
        mSearchRadius = pxFromDp(DEFAULT_DP_SIZE, displayMetrics);

        GradientDrawable drawable = (GradientDrawable) v.getBackground();
        if (i % 2 == 0) {
            drawable.setColor(SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_WIDGET_COLOR, R.color.widget_color));
            drawable.setCornerRadius(mSearchRadius);
        } else {
            drawable.setColor(SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_WIDGET_COLOR, R.color.widget_color));
            drawable.setCornerRadius(mSearchRadius);
        }

        action.setColorFilter(SettingsProvider.getInt(getActivity(),
                SettingsProvider.KEY_MENU_COLOR, R.color.white));
        voice.setColorFilter(SettingsProvider.getInt(getActivity(),
                SettingsProvider.KEY_VOICE_COLOR, R.color.white));
        apps.setColorFilter(SettingsProvider.getInt(getActivity(),
                SettingsProvider.KEY_APPS_COLOR, R.color.white));

        search = (SearchView) v.findViewById(R.id.searchView1);
        for (TextView textView : findChildrenByClass(search, TextView.class)) {
            textView.setTextColor(SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_SEARCHTEXT_COLOR, R.color.white));
            textView.setHintTextColor(SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_SEARCHHINTTEXT_COLOR, R.color.white));
        }
        search.setQueryHint("Google Search");

        if (ShowVoice()) {
            voice.setVisibility(View.VISIBLE);
        } else {
            voice.setVisibility(View.GONE);
        }

        if (ShowApps()) {
            apps.setVisibility(View.VISIBLE);
            } else {
            apps.setVisibility(View.GONE);
        }

        if (ShowMenu()) {
            action.setVisibility(View.VISIBLE);
                } else {
            action.setVisibility(View.GONE);
        }

        ImageButton action = (ImageButton) v.findViewById(R.id.action_button);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bottom = new Intent(getActivity(),
                        BottomInterface.class);
                startActivity(bottom);
            }
        });
        action.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                final PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(new PopupEventListener());
                final MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.launcher_menu, popup.getMenu());
                popup.show();
                return true;
            }
        });
        action.setOnTouchListener(getHapticFeedbackTouchListener());

        ImageButton voice = (ImageButton) v.findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleNowIntent = new Intent("android.intent.action.VOICE_ASSIST"); startActivity(googleNowIntent);
            }
        });
        voice.setOnTouchListener(getHapticFeedbackTouchListener());

        ImageButton apps = (ImageButton) v.findViewById(R.id.allApps);
        apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(getActivity(),
                        SearchActivity.class);
                startActivity(search);
            }
        });
        apps.setOnTouchListener(getHapticFeedbackTouchListener());

        int searchPlateId = search.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = search.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(Color.TRANSPARENT);
        }

        //*** setOnQueryTextFocusChangeListener ***
        search.onActionViewExpanded();
        search.clearFocus();
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    String term = search.getQuery().toString();
                    intent.putExtra(SearchManager.QUERY, term);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        search.setOnTouchListener(getHapticFeedbackTouchListener());
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            }
        });


        return v;
    }

    public static <V extends View> Collection<V> findChildrenByClass(ViewGroup viewGroup, Class<V> clazz) {

        return gatherChildrenByClass(viewGroup, clazz, new ArrayList<V>());
    }

    private static <V extends View> Collection<V> gatherChildrenByClass(ViewGroup viewGroup, Class<V> clazz, Collection<V> childrenFound) {

        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            final View child = viewGroup.getChildAt(i);
            if (clazz.isAssignableFrom(child.getClass())) {
                childrenFound.add((V)child);
            }
            if (child instanceof ViewGroup) {
                gatherChildrenByClass((ViewGroup) child, clazz, childrenFound);
            }
        }

        return childrenFound;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_1:
                Intent bottom = new Intent(getActivity(),
                        BottomInterface.class);
                startActivity(bottom);
                return true;
            case R.id.action_2:
                Intent log = new Intent(getActivity(),
                        Login.class);
                startActivity(log);
                return true;
            case R.id.action_3:
                final Intent intentManageApps = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                intentManageApps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentManageApps);
                return true;
            case R.id.action_4:
                final Intent intentSettings = new Intent(Settings.ACTION_SETTINGS);
                intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean ShowApps() {
        return SettingsProvider.getBoolean(getActivity(),
                SettingsProvider.KEY_SHOW_APPS, true);
    }

    public boolean ShowVoice() {
        return SettingsProvider.getBoolean(getActivity(),
                SettingsProvider.KEY_SHOW_VOICE, true);
    }

    public boolean ShowMenu() {
        return SettingsProvider.getBoolean(getActivity(),
                SettingsProvider.KEY_SHOW_MENU, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        LauncherAppState.setSettingsChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        SettingsProvider.get(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        SettingsProvider.get(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    class PopupEventListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return onOptionsItemSelected(item);
        }
    }
}
