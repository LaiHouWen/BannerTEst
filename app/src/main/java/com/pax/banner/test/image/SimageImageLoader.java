package com.pax.banner.test.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pax.banner.loader.ImageLoaderInterface;
import com.pax.labs.ImageSource;
import com.pax.labs.SubsamplingScaleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018-12-07.
 */
//SubsamplingScaleImageView
public class SimageImageLoader implements ImageLoaderInterface<LoadSimageView> {

    SubsamplingScaleImageView imageViews;

    Map<String,SubsamplingScaleImageView> map = new HashMap<>();
    Map<String,String> list =  new HashMap<>();

    String pathI = "/sdcard/test/";

//    @Override
//    public void displayImage(Context context, final Object path,final SubsamplingScaleImageView imageView) {
//        if (path instanceof Integer) {//resource
//            imageView.setImage(ImageSource.resource((int) path));//图片路径
//        }else if ((path instanceof String) && ((String) path).startsWith("http")){
//            map.put((String)path,imageView);
//            DownImg(context, (String) path, imageView);
//        }else if ((path instanceof String) && !(new File((String)path)).exists()) {//asset
//            imageView.setImage(ImageSource.asset((String) path));//图片路径
//        }else if((path instanceof String)&& (new File((String)path)).exists()){//sdcard路径
//            imageView.setImage(ImageSource.uri((String) path));//图片路径
//        } else if ((path instanceof String) && ((String) path).startsWith("http")) {

//            try {
//                FutureTarget<File> future = (FutureTarget<File>) Glide.with(context).load((String) path).asBitmap();
//                String paths = future.get().getAbsolutePath();
//                Log.i("SimageImageLoader","path="+paths);
//                imageView.setImage(ImageSource.uri((String) paths));//图片路径
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            imageViews = imageView;
//            Log.i("SimageImageLoader","path="+path);
//            Glide.with(context).load((String) path).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    Log.i("SimageImageLoader","Bitmap getWidth:"+resource.getWidth()+" ,getHeight:"+ resource.getHeight());
//                    imageViews.setImage(ImageSource.bitmap(resource));//图片路径
//                }
//            });
//            map.put((String)path,imageView);
//            DownImg(context, (String) path, imageView);

//            DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
//            ImageLoader.getInstance().loadImage((String) path, options, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    Log.i("SimageImageLoader", "onLoadingStarted path=" + imageUri);
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    Log.i("SimageImageLoader", "onLoadingFailed path=" + imageUri);
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    Log.i("SimageImageLoader", "onLoadingComplete path=" + imageUri +
//                            ",loadedImage.getWidth:" + loadedImage.getWidth() +
//                            ",getHeight:" + loadedImage.getHeight());
//                    imageView.setImage(ImageSource.bitmap(loadedImage));//图片路径
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//                    Log.i("SimageImageLoader", "onLoadingCancelled path=" + imageUri);
//                }
//            });


//            Bitmap b = ImageLoader.getInstance().loadImageSync((String) path, options);
//            Log.i("SimageImageLoader", " path=" + b +
//                    ",b.getWidth:" + b.getWidth() +
//                    ",getHeight:" + b.getHeight());
//            imageView.setImage(ImageSource.bitmap(b));//图片路径


//        }

//        Glide.with(context.getApplicationContext())
//                .load(path);


//    }

//    @Override
//    public SubsamplingScaleImageView createImageView(Context context) {
//        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(context);
//        return imageView;
//    }


    @Override
    public void displayImage(Context context, final Object path,final LoadSimageView view) {
        if (path instanceof Integer) {//resource
            view.getSubImageview().setImage(ImageSource.resource((int) path));//图片路径
        } else if ((path instanceof String) && ((String) path).startsWith("http")) {
            map.put((String) path, view.getSubImageview());
            DownImg(context, (String) path, view);
        } else if ((path instanceof String) && !(new File((String) path)).exists()) {//asset
            view.getSubImageview().setImage(ImageSource.asset((String) path));//图片路径
        } else if ((path instanceof String) && (new File((String) path)).exists()) {//sdcard路径
            view.getSubImageview().setImage(ImageSource.uri((String) path));//图片路径
        }
    }

    @Override
    public LoadSimageView createImageView(Context context) {
        LoadSimageView view = new LoadSimageView(context);
        return view;
    }
//    LoadSimageView


    public void DownImg(Context context, final String path, final LoadSimageView simageView) {
//        Log.i("SimageImageLoader", "path=" + path);
//        Glide.with(context).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Log.i("SimageImageLoader", "Bitmap getWidth:" + resource.getWidth() + " ,getHeight:" + resource.getHeight());
//                imageVie.setImage(ImageSource.bitmap(resource));//图片路径
//            }
//        });

        DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
        ImageLoader.getInstance().loadImage(path, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.i("SimageImageLoader", "onLoadingStarted path=" + imageUri);
                simageView.showProgresBbar();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.i("SimageImageLoader", "onLoadingFailed path=" + imageUri);
                simageView.dismissProgressBar();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.i("SimageImageLoader", "onLoadingComplete path=" + imageUri +
                        " ,loadedImage.getWidth:" + loadedImage.getWidth() +
                        " ,getHeight:" + loadedImage.getHeight());
                simageView.dismissProgressBar();
                int i = imageUri.lastIndexOf("/");
                String name = imageUri.substring(i+1);
                Log.i("SimageImageLoader","name:"+name);
                saveBitmap(name,loadedImage);
//                FileUtils.getInstance().writeFileToSdcardByFileOutputStream(pathI,name,getBytesByBitmapS(loadedImage));
                list.put(imageUri,pathI+name);

                simageView.getSubImageview().setImage(ImageSource.uri(pathI+name));

//               if (map.containsKey(imageUri))
//                map.get(imageUri).setImage(ImageSource.bitmap(loadedImage));//图片路径
//                ((SubsamplingScaleImageView)view).setImage(ImageSource.bitmap(loadedImage));//图片路径
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.i("SimageImageLoader", "onLoadingCancelled path=" + imageUri);
                simageView.dismissProgressBar();
            }
        });
    }
    public byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        return buffer.array();
    }

    public byte[] getBytesByBitmapS(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    /** 保存方法 */
    public void saveBitmap(String picName,Bitmap bm) {
        Log.e("SimageImageLoader", "保存图片");
        File f = FileUtils.getInstance().makeFile(pathI,picName);
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("SimageImageLoader", "已经保存 path="+f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
