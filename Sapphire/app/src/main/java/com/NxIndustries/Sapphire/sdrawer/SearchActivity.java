/* Copyright 2015 Hayai Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.NxIndustries.Sapphire.sdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.NxIndustries.Sapphire.R;

public class SearchActivity extends Activity {

    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            Fragment newFragment = new SearchFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(CONTENT_VIEW_ID, newFragment).commit();
        }
    }
}