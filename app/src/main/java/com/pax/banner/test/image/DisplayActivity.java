package com.pax.banner.test.image;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.pax.banner.test.R;
import com.pax.labs.ImageSource;
import com.pax.labs.SubsamplingScaleImageView;


/**
 * Created by zmc on 2017/7/18.
 */

public class DisplayActivity extends AppCompatActivity {
    private String TAG = "DisplayActivity";
    private Uri uri;//怎么部分重构？
    private SubsamplingScaleImageView mScanImageView;
    private ProgressBar seek_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.scan_activity_layout);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //
            //finish();
        }

        seek_bar = (ProgressBar) findViewById(R.id.progress_bar);
        seek_bar.setVisibility(View.VISIBLE);

        uri = getIntent().getParcelableExtra("uri");
        Log.i(TAG,"uri:"+(uri!=null?uri.toString():"null"));
//file:///scard/picture.jpg
        mScanImageView = (SubsamplingScaleImageView) findViewById(R.id.scan_imageView);
        mScanImageView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {
                Log.i(TAG,"onReady()");
            }

            @Override
            public void onImageLoaded() {
                Log.i(TAG,"onImageLoaded()");
                seek_bar.setVisibility(View.GONE);
            }

            @Override
            public void onPreviewLoadError(Exception e) {
                Log.i(TAG,"onPreviewLoadError()");
            }

            @Override
            public void onImageLoadError(Exception e) {
                Log.i(TAG,"onImageLoadError()");
            }

            @Override
            public void onTileLoadError(Exception e) {
                Log.i(TAG,"onTileLoadError()");
            }

            @Override
            public void onPreviewReleased() {
                Log.i(TAG,"onPreviewReleased()");
            }
        });
        mScanImageView.setDebug(true);//x1080.jpg ditei.jpg
        //ImageSource.asset("x1080.jpg")
        mScanImageView.setImage(ImageSource.uri(uri));//ffff_3.jpg

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

}
