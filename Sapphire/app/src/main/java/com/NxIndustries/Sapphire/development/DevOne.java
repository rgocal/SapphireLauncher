package com.NxIndustries.Sapphire.development;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.NxIndustries.Sapphire.R;
import com.koushikdutta.ion.Ion;

/**
 * Created by ry on 1/2/17.
 */
public class DevOne extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static DevOne newInstance(int sectionNumber) {
        DevOne fragment = new DevOne();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DevOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dev_one, container, false);
        setHasOptionsMenu(true);

        ImageView avatar_one = (ImageView) rootView.findViewById(R.id.avatar_one);
        ImageView banner_one = (ImageView) rootView.findViewById(R.id.banner_one);

        LinearLayout ry_website = (LinearLayout) rootView.findViewById(R.id.ry_website);
        LinearLayout ry_gplay = (LinearLayout) rootView.findViewById(R.id.ry_gplay);
        LinearLayout ry_twitter = (LinearLayout) rootView.findViewById(R.id.ry_twitter);
        LinearLayout ry_gplus = (LinearLayout) rootView.findViewById(R.id.ry_gplus);

        ry_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gocalsd.weebly.com/"));
                startActivity(intent);
            }
        });
        ry_gplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Ryan+Gocal"));
                startActivity(intent);
            }
        });
        ry_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Rgocal"));
                startActivity(intent);

            }
        });
        ry_gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+Ryangocal"));
                startActivity(intent);

            }
        });

        Ion.with(avatar_one)
                .placeholder(R.mipmap.ic_launcher_two)
                .error(R.mipmap.ic_settings)
                .animateLoad(R.anim.task_open_enter)
                .animateIn(R.anim.task_open_enter)
                .smartSize(true)
                .load("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/13615483_501560690039595_8644094335531376079_n.jpg?oh=5b25d5b54b5d356082371947d9998e01&oe=58DC5646");

        Ion.with(banner_one)
                .animateLoad(R.anim.task_open_enter)
                .animateIn(R.anim.task_open_enter)
                .smartSize(true)
                .load("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/15170982_1340452562655388_3162152732441839512_n.jpg?oh=e11e70222c75f7d5e22a8ae00062a914&oe=58ECA042");

        return rootView;
    }
}
