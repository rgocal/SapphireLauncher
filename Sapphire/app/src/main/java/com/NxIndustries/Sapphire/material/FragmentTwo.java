package com.NxIndustries.Sapphire.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.settings.DockActivity;
import com.NxIndustries.Sapphire.settings.DrawerActivity;
import com.NxIndustries.Sapphire.settings.EhsActivity;
import com.NxIndustries.Sapphire.settings.FolderActivity;
import com.NxIndustries.Sapphire.settings.HomescreenActivity;
import com.NxIndustries.Sapphire.settings.SearchActivity;

/**
 * Created by ry on 5/31/16.
 */

public class FragmentTwo extends Fragment {

    public static final String ARGS_INSTANCE = "com.NxIndustries.Sapphire.argsInstance";

    public static FragmentTwo newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FragmentTwo fragment = new FragmentTwo();
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
        View v = inflater.inflate(R.layout.dashboard_two, container, false);
        setHasOptionsMenu(true);

        final ImageView cust_icon = (ImageView) v.findViewById(R.id.cust_icon);
        final ImageView imers = (ImageView) v.findViewById(R.id.imers);

        LinearLayout home = (LinearLayout) v.findViewById(R.id.homescreen);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomescreenActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), cust_icon, "customization");
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout dock = (LinearLayout) v.findViewById(R.id.dock);
        dock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DockActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), cust_icon, "customization");
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout drawer = (LinearLayout) v.findViewById(R.id.drawer);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DrawerActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), cust_icon, "customization");
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout folder = (LinearLayout) v.findViewById(R.id.folders);
        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FolderActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), cust_icon, "customization");
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout ehs = (LinearLayout) v.findViewById(R.id.ehs);
        ehs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EhsActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), imers, "immersive");
                startActivity(intent, options.toBundle());
            }
        });

        LinearLayout search = (LinearLayout) v.findViewById(R.id.s_widget);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), imers, "immersive");
                startActivity(intent, options.toBundle());
            }
        });

        return v;
    }
}
