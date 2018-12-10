package com.pax.banner.test.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.pax.banner.test.R;
import com.pax.labs.SubsamplingScaleImageView;

/**
 * Created by Administrator on 2018-12-10.
 */

public class LoadSimageView extends RelativeLayout {

    private SubsamplingScaleImageView imageView;
    private ProgressBar progressBar;

    public LoadSimageView(Context context) {
        super(context);
        init(context);
    }

    public LoadSimageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadSimageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.scan_activity_layout,null);
        imageView = (SubsamplingScaleImageView) view.findViewById(R.id.scan_imageView);
        progressBar= (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(GONE);
        addView(view);
    }


    public SubsamplingScaleImageView getSubImageview() {
        return imageView;
    }

    public void showProgresBbar() {
        progressBar.setVisibility(VISIBLE);
    }

    public void dismissProgressBar() {
        progressBar.setVisibility(GONE);
    }

}
