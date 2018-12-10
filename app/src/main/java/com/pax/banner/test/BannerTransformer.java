package com.pax.banner.test;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


/**
 * Created by PAX on 2017/9/20.
 */
public class BannerTransformer implements ViewPager.PageTransformer {
    private final float MAX_SCALE = 10;
    private final float DIF_SCALE = 1;
    private final float MIN_SCALE = MAX_SCALE - DIF_SCALE;

    @Override
    public void transformPage(View view, float position) {
        Log.e("BannerTransformer","position="+position);

        int diffWidth = 18;

        if (position < -1) { // [-Infinity,-1)
            view.setScaleX(MIN_SCALE / MAX_SCALE);
            view.setScaleY(MIN_SCALE / MAX_SCALE);
            view.setTranslationX(diffWidth);
        } else if (position <= 0) { // [-1,0]
            view.setScaleX(DIF_SCALE + position / MAX_SCALE);
            view.setScaleY(DIF_SCALE + position / MAX_SCALE);
            view.setTranslationX((0 - position) * diffWidth);
        } else if (position <= 1) { // (0,1]
            view.setScaleX(DIF_SCALE - position / MAX_SCALE);
            view.setScaleY(DIF_SCALE - position / MAX_SCALE);
            view.setTranslationX((0 - position) * diffWidth);
        } else { // (1,+Infinity]
            view.setScaleX(MIN_SCALE / MAX_SCALE);
            view.setScaleY(MIN_SCALE / MAX_SCALE);
            view.setTranslationX(-diffWidth);
        }
    }
}