package com.NxIndustries.Sapphire.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.NxIndustries.Sapphire.R;

/**
 * Created by ry on 4/29/16.
 */
public class CardCategory extends Preference {

    public CardCategory(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = null;
        try {
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        View layout = View.inflate(getContext(), R.layout.card_shadow,
                null);
        return layout;
    }
}