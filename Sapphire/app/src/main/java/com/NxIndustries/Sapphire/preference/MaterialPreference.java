package com.NxIndustries.Sapphire.preference;

/**
 * Created by ry on 5/1/16.
 */
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NxIndustries.Sapphire.R;

public class MaterialPreference extends PreferenceCategory {

    public MaterialPreference(Context context) {
        super(context);
    }

    public MaterialPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialPreference(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {

        TextView categoryTitle = (TextView) super.onCreateView(parent);

        categoryTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
        categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        categoryTitle.setPadding(40, 20, 0, 20);
        categoryTitle.setAllCaps(true);

        return categoryTitle;
    }
}
