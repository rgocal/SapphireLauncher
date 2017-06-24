package com.NxIndustries.Sapphire.material;

/**
 * Created by ry on 6/14/16.
 */

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;

import com.NxIndustries.Sapphire.R;
import com.commit451.elasticdragdismisslayout.ElasticDragDismissFrameLayout;
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener;

public class AboutActivity extends Activity implements ElasticDragDismissListener {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ElasticDragDismissFrameLayout draggableLayout = (ElasticDragDismissFrameLayout) findViewById(R.id.draggable_frame);

        draggableLayout.addListener(this);
    }

    @Override public void onDrag(float elasticOffset, float elasticOffsetPixels, float rawOffset, float rawOffsetPixels) {

    }

    @Override public void onDragDismissed() {
        ElasticDragDismissFrameLayout draggableLayout = (ElasticDragDismissFrameLayout) findViewById(R.id.draggable_frame);

        if (draggableLayout.getTranslationY() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.explode));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}