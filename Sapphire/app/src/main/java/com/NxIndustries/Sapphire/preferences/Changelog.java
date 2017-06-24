package com.NxIndustries.Sapphire.preferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NxIndustries.Sapphire.R;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ry on 11/20/15.
 */
public class Changelog extends Fragment {

    public static final String ARGS_INSTANCE = "com.NxIndustries.Sapphire.argsInstance";

    public static Changelog newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        Changelog fragment = new Changelog();
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
        View v = inflater.inflate(R.layout.change_log, container, false);
        loadChangeLog(v);
        return v;
    }



    private void loadChangeLog(View v) {
        TextView tv = (TextView)v.findViewById(R.id.logText);
        StringBuilder sb = new StringBuilder("");

        try {
            InputStreamReader isr = new InputStreamReader(getActivity().getAssets().open("changelog.txt"));
            int c;
            while ( (c = isr.read()) != -1)
                sb.append((char)c);
            isr.close();

        } catch (IOException e) {
        }

        tv.setText(sb.toString());
    }
}