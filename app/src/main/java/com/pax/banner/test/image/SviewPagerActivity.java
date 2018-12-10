package com.pax.banner.test.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pax.banner.Banner;
import com.pax.banner.test.BannerTransformer;
import com.pax.banner.test.R;
import com.pax.banner.test.loader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-12-07.
 */

public class SviewPagerActivity extends AppCompatActivity {

    private Banner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_v_a);

        banner = (Banner) findViewById(R.id.banner_s);

        //本地图片数据（资源文件）
        List<String> list = new ArrayList<>();
        list.add("ffff_3.jpg");//https://github.com/LaiHouWen/image/blob/master/ditei.jpg
        //list.add("ff1.jpg");//https://github.com/LaiHouWen/image/blob/master/20181207145808.jpg
        //list.add("x1080.jpg");//https://github.com/LaiHouWen/image/blob/master/ff1.jpg
       // list.add("ditei.jpg");//https://github.com/LaiHouWen/image/blob/master/ffff_3.jpg

//        list.add("https://raw.githubusercontent.com/LaiHouWen/image/master/ditei.jpg");
//        list.add("https://raw.githubusercontent.com/LaiHouWen/image/master/ffff_3.jpg");
        list.add("https://raw.githubusercontent.com/LaiHouWen/image/master/20181207145808.jpg");
//        list.add("https://raw.githubusercontent.com/LaiHouWen/image/master/ff1.jpg");

        list.add("/sdcard/test/ditei.jpg");

        banner.setImages(list).isAutoPlay(false).setImageLoader(new SimageImageLoader())
                .setPageMargin(0).setOffscreenPageLimit(3)
                .start();

    }


}
