package com.NxIndustries.Sapphire.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.gesture.AddGesture;
import com.NxIndustries.Sapphire.gesture.GestureMainActivity;
import com.NxIndustries.Sapphire.gesture.SketchPreferences;
import com.NxIndustries.Sapphire.settings.ButtonActivity;
import com.NxIndustries.Sapphire.settings.GestureButtonActivity;

/**
 * Created by ry on 10/21/16.
 */

public class FragmentThree extends Fragment {

    public static final String ARGS_INSTANCE = "com.NxIndustries.Sapphire.argsInstance";

    public static FragmentThree newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FragmentThree fragment = new FragmentThree();
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
        View v = inflater.inflate(R.layout.dashboard_three, container, false);
        setHasOptionsMenu(true);

        LinearLayout nav = (LinearLayout) v.findViewById(R.id.nav_buttons);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ButtonActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity());
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout gestures = (LinearLayout) v.findViewById(R.id.gesture_list);
        gestures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GestureButtonActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity());
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout draw = (LinearLayout) v.findViewById(R.id.draw_sketch);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGesture.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity());
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout sketches = (LinearLayout) v.findViewById(R.id.my_sketches);
        sketches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GestureMainActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity());
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout sketch_settings = (LinearLayout) v.findViewById(R.id.sketch_settings);
        sketch_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SketchPreferences.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity());
                startActivity(intent, options.toBundle());
            }
        });


        return v;
    }
}
