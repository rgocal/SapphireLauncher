package com.NxIndustries.Sapphire.development;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NxIndustries.Sapphire.R;

/**
 * Created by ry on 1/2/17.
 */

public class TheTeam extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static TheTeam newInstance(int sectionNumber) {
        TheTeam fragment = new TheTeam();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TheTeam() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.the_team, container, false);
        setHasOptionsMenu(true);

        return rootView;
    }
}
