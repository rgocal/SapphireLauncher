/*
 * Copyright (C) 2015 Kevin Barry, TeslaCoil Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.NxIndustries.Sapphire.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.NxIndustries.Sapphire.R;

/**
 * Created by kevin on 4/28/15.
 */
public class CardPreferenceBorder extends Preference {
    private boolean mTopShadow;
    private boolean mBottomShadow;

    public CardPreferenceBorder(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CardPreferenceBorder);
        mTopShadow = a.getBoolean(R.styleable.CardPreferenceBorder_topShadow, true);
        mBottomShadow = a.getBoolean(R.styleable.CardPreferenceBorder_bottomShadow, true);
        a.recycle();

        setLayoutResource(R.layout.preference_card_border);
        setEnabled(false);
        setSelectable(false);
    }

    public CardPreferenceBorder(Context context, boolean topShadow, boolean bottomShadow) {
        super(context);

        mTopShadow = topShadow;
        mBottomShadow = bottomShadow;

        setLayoutResource(R.layout.preference_card_border);
        setEnabled(false);
        setSelectable(false);
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        View view = super.getView(convertView, parent);
        view.findViewById(R.id.top).setVisibility(mTopShadow ? View.VISIBLE : View.INVISIBLE);
        view.findViewById(R.id.bottom).setVisibility(mBottomShadow ? View.VISIBLE : View.INVISIBLE);
        return view;
    }

}
