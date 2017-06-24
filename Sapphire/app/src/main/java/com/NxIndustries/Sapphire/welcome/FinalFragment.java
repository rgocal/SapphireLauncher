package com.NxIndustries.Sapphire.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NxIndustries.Sapphire.R;

/**
 * Created by ry on 2/14/17.
 */
public class FinalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.welcome_final_fragment, container, false);
    }
}