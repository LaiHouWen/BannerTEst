package com.pax.banner.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pax.banner.Banner;
import com.pax.banner.test.image.DisplayActivity;
import com.pax.banner.test.image.SviewPagerActivity;
import com.pax.banner.test.loader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Banner banner;
    private Banner banner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = (Banner) findViewById(R.id.banner);
        banner2 = (Banner) findViewById(R.id.banner2);

        findViewById(R.id.btn_chose).setOnClickListener(this);
        findViewById(R.id.btn_viewpager).setOnClickListener(this);


        //创建默认的ImageLoaderConfiguration
        ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(this);
        //初始化ImageLoader
        ImageLoader.getInstance().init(configuration);

//本地图片数据（资源文件）
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.b1);
        list.add(R.mipmap.b2);
        list.add(R.mipmap.b3);
//        list.add(R.mipmap.a);
//        list.add(R.mipmap.b);
//        list.add(R.mipmap.c);
//        list.add(R.mipmap.d);
//        list.add(R.mipmap.f);

//        banner_d.setImages(list).isAutoPlay(true).isGesturesScroll(true).isGesturesCycle(true)
//                .setImageLoader(new GlideImageLoader())
//                .start();

//        banner_d.setImages(list).isAutoPlay(true).isGesturesScroll(true).isGesturesCycle(false)
//                .setImageLoader(new GlideImageLoader())
//                .start();

//        banner_d.setImages(list).isAutoPlay(true).isGesturesScroll(false).isGesturesCycle(false)
//                .setImageLoader(new GlideImageLoader())
//                .start();

//        banner_d.setImages(list).isAutoPlay(false).isGesturesScroll(true).isGesturesCycle(false)
//                .setImageLoader(new GlideImageLoader())
//                .start();

//        banner_d.setImages(list).isAutoPlay(false).isGesturesScroll(true).
//                isGesturesCycle(false).setIndicatorVisiable(true).setIndicatorGravity(Gravity.RIGHT)
//                .setImageLoader(new GlideImageLoader())
//                .start();

//        banner.setImages(list).setImageLoader(new GlideImageLoader()).
//                setBannerAnimation(BannerTransformer.class).setPageMargin(0).setOffscreenPageLimit(3)
//                .start();

        //重写layout
//        banner2.setImages(list).setImageLoader(new GlideImageLoader()).
//                setBannerAnimation(BannerTransformer.class).setPageMargin(3).setOffscreenPageLimit(3)
//                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chose:
                openGallery();
                break;
            case R.id.btn_viewpager:
                Intent intent = new Intent(this, SviewPagerActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void openGallery() {
        //就一个按钮
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GET_IMAGE);
    }

    public static final int GET_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GET_IMAGE:
                    Uri uri = data.getData();
                    Intent intent = new Intent(this, DisplayActivity.class);
                    intent.putExtra("uri", uri);
                    startActivity(intent);
                    break;
            }
        }
    }


}
