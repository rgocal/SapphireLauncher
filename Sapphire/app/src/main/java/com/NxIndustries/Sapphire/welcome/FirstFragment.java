package com.NxIndustries.Sapphire.welcome;

/**
 * Created by ry on 2/14/17.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NxIndustries.Sapphire.R;

/**
 * The Terms of Service fragment in the welcome screen.
 */
public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.welcome_tos_fragment, container, false);
    }
}
