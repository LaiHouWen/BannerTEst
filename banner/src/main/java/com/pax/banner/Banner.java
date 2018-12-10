package com.pax.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pax.banner.listener.OnBannerListener;
import com.pax.banner.listener.OnDefaultImgListener;
import com.pax.banner.loader.ImageLoaderInterface;
import com.pax.banner.view.BannerScroller;
import com.pax.banner.view.BannerViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-11-28.
 * 轮播图
 * 最简单的调用方法
 * banner.setImages(list).setImageLoader(new GlideImageLoader()).start();
 * <p>
 * banner.setImages(list).isAutoPlay(false).isGesturesScroll(true).
 * isGesturesCycle(false).setIndicatorVisiable(true).setIndicatorGravity(Gravity.RIGHT)
 * .setImageLoader(new GlideImageLoader())
 * .start();
 * <p>
 * GlideImageLoader 图片加载需要自己继承ImageLoader实现
 *
 * 在xml中viewpager及父容器使用android:clipChildren="false" ，可以让viewpager在一个页面显示多个控件
 * 需要在viewpager添加属性，layout_marginLeft layout_marginRight 加动画，可实现2边显示图片的效果
 *
 *
 *
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {

    /**
     * banner
     */
    public static final int PADDING_SIZE = 5;
    public static final int TIME = 2000;
    public static final int DURATION = 800;
    public static final boolean IS_AUTO_PLAY = true;
    public static final boolean IS_SCROLL = true;

    public static final String tag = Banner.class.getSimpleName();
    private Context context;
    private BannerViewPager viewPager;
    private LinearLayout indicator;//小圆点
    private ImageView bannerDefaultImage;

    private List<String> titles;
    private List imageUrls;
    private List<View> imageViews;
    private List<ImageView> indicatorImages;//小圆点
    private int count = 0;
    private int currentItem;
    private int gravity = Gravity.CENTER;
    private int lastPosition = 1;
    private int scaleType = 1;//图片的Type
    private int delayTime = TIME;//自动轮播时间时间
    private boolean isAutoPlay = true;//是否自动轮播
    private boolean isGesturesScroll = true;//是否手动轮播
    private boolean isGesturesCycle = true;//是否手动轮播 循环

    private int mIndicatorMargin = PADDING_SIZE;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int indicatorSize;
    private int mIndicatorSelectedResId = R.drawable.gray_radius;
    private int mIndicatorUnselectedResId = R.drawable.white_radius;
    private boolean isIndicatorVisiable = true;//是否显示小圆点

    private int marginPixels = 0;

    private int mLayoutResId = R.layout.banner;

    private int bannerBackgroundImage;

    private int scrollTime = DURATION;

    private ImageLoaderInterface imageLoader;
    private BannerPagerAdapter adapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private BannerScroller mScroller;
    private OnBannerListener listener;
    private OnDefaultImgListener defaultImgListener;
    private DisplayMetrics dm;

    private WeakHandler handler = new WeakHandler();

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        titles = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        dm = context.getResources().getDisplayMetrics();
        indicatorSize = dm.widthPixels / 80;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        imageViews.clear();
        handleTypedArray(context, attrs);//定义xml属性
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);
        viewPager = (BannerViewPager) view.findViewById(R.id.bannerDViewPager);
        indicator = (LinearLayout) view.findViewById(R.id.circleIndicator);
        bannerDefaultImage = (ImageView) view.findViewById(R.id.bannerDefaultImage);

        viewPager.setFocusable(false);
        viewPager.setFocusableInTouchMode(false);

        bannerDefaultImage.setImageResource(bannerBackgroundImage);

        initViewPagerScroll();
    }

    /**
     * 定义xml属性
     * attr.xml 定义Banner属性 在布局文件上引用
     *
     * @param context
     * @param attrs
     */
    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorSize);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorSize);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, PADDING_SIZE);
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_selected, R.drawable.gray_radius);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_unselected, R.drawable.white_radius);
        scaleType = typedArray.getInt(R.styleable.Banner_image_scale_type, scaleType);
        delayTime = typedArray.getInt(R.styleable.Banner_delay_time, TIME);
        scrollTime = typedArray.getInt(R.styleable.Banner_scroll_time, DURATION);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_is_auto_play, IS_AUTO_PLAY);
        mLayoutResId = typedArray.getResourceId(R.styleable.Banner_banner_layout, mLayoutResId);
        bannerBackgroundImage = typedArray.getResourceId(R.styleable.Banner_banner_default_image, R.drawable.no_banner);
        typedArray.recycle();
    }

    /**
     * 对 viewpager 重设置 mScroller 属性
     */
    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(viewPager.getContext(), sInterpolator);
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    /**
     * 设置banner图片点击事件
     *
     * @param listener
     * @return
     */
    public Banner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 默认图片点击事件
     *
     * @param listener
     * @return
     */
    public Banner setOnDefaultImgListener(OnDefaultImgListener listener) {
        this.defaultImgListener = listener;
        return this;
    }

    /**
     * 对viewpager的变化监听
     *
     * @param listener
     * @return
     */
    public Banner setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
        return this;
    }

    /**
     * 是否显示小圆点
     *
     * @param visiable
     * @return
     */
    public Banner setIndicatorVisiable(boolean visiable) {
        this.isIndicatorVisiable = visiable;
        return this;
    }

    /**
     * 设置小圆点 局左  居中 局右
     * Gravity.LEFT | Gravity.CENTER_VERTICAL;
     * Gravity.CENTER;
     * Gravity.RIGHT | Gravity.CENTER_VERTICAL;
     *
     * @param gravityType
     * @return
     */
    public Banner setIndicatorGravity(int gravityType) {
        this.gravity = gravityType;
        return this;
    }

    /**
     * 设置切换动画
     *
     * @param transformer
     * @return
     */
    public Banner setBannerAnimation(Class<? extends ViewPager.PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(tag, "Please set the PageTransformer class");
        }
        return this;
    }

    /**
     * Set a {@link ViewPager.PageTransformer} that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return Banner
     */
    public Banner setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    /**
     * Set the margin between pages.
     *
     * @param marginPixels Distance between adjacent pages in pixels
     * @see # setPageMarginDrawable(int)
     */
    public Banner setPageMargin(int marginPixels) {
        this.marginPixels = marginPixels;
        return this;
    }

    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    public Banner setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    public Banner setImages(List<?> imageUrls) {
        this.imageUrls = imageUrls;
        this.count = imageUrls.size();
        return this;
    }

    public Banner setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public void update(List<?> imageUrls, List<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
        update(imageUrls);
    }

    public void update(List<?> imageUrls) {
        this.imageUrls.clear();
        this.imageViews.clear();
        this.indicatorImages.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        start();
    }

    /**
     * 是否自动轮播
     *
     * @param isAutoPlay
     * @return
     */
    public Banner isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    /**
     * 是否手动轮播
     *
     * @param isScroll
     * @return
     */
    public Banner isGesturesScroll(boolean isScroll) {
        this.isGesturesScroll = isScroll;
        return this;
    }

    /**
     * 是否手动轮播 循环
     *
     * @param isCycle
     * @return
     */
    public Banner isGesturesCycle(boolean isCycle) {
        this.isGesturesCycle = isCycle;
        return this;
    }

    /**
     * 开始配置图片
     *
     * @return
     */
    public Banner start() {
        setBannerStyleUI();
        setImageList(imageUrls);
        setData();
        return this;
    }

    /**
     * 小圆点显示
     */
    private void setBannerStyleUI() {
        int visibility = isIndicatorVisiable && (count > 1) ? View.VISIBLE : View.GONE;
        indicator.setVisibility(visibility);
        indicator.setGravity(gravity);
        lastPosition = isGesturesCycle ? 1 : 0;
        viewPager.setPageMargin(marginPixels);

    }

    /**
     * 创建小圆点
     */
    private void createIndicator() {
        indicatorImages.clear();
        indicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            indicatorImages.add(imageView);
            indicator.addView(imageView, params);
        }
    }

    /**
     * 根据图片创建图片列表view
     *
     * @param imagesUrl
     */
    private void setImageList(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            bannerDefaultImage.setVisibility(VISIBLE);
            if (defaultImgListener != null)
                defaultImgListener.OnDefaultImgClick();
            Log.e(tag, "The image data set is empty.");
            return;
        }
        bannerDefaultImage.setVisibility(GONE);
        initImages();//小圆点
        for (int i = 0; i <= count + 1; i++) {
            View imageView = null;
            if (imageLoader != null) {
                imageView = imageLoader.createImageView(context);
            }
            if (imageView == null) {
                imageView = new ImageView(context);
            }
            setScaleType(imageView);
            Object url = null;
            if (i == 0) {
                url = imagesUrl.get(count - 1);
            } else if (i == count + 1) {
                url = imagesUrl.get(0);
            } else {
                url = imagesUrl.get(i - 1);
            }
            imageViews.add(imageView);
            if (imageLoader != null)
                imageLoader.displayImage(context, url, imageView);
            else
                Log.e(tag, "Please set images loader.");
        }
        if (!isGesturesCycle) {
            imageViews.remove(count + 1);
            imageViews.remove(0);
        }
    }

    /**
     * 设置 小圆点 或者 数字提示
     */
    private void initImages() {
        createIndicator();
    }

    private void setData() {
        currentItem = isGesturesCycle ? 1 : 0;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
        }
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(isGesturesScroll);//是否手动轮播
        viewPager.setCurrentItem(currentItem);
        if (isGesturesScroll && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        if (isAutoPlay)
            startAutoPlay();
    }

    private void setScaleType(View imageView) {
        if (imageView instanceof ImageView) {
            ImageView view = ((ImageView) imageView);
            switch (scaleType) {
                case 0:
                    view.setScaleType(ImageView.ScaleType.CENTER);
                    break;
                case 1:
                    view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 2:
                    view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    break;
                case 3:
                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    break;
                case 4:
                    view.setScaleType(ImageView.ScaleType.FIT_END);
                    break;
                case 5:
                    view.setScaleType(ImageView.ScaleType.FIT_START);
                    break;
                case 6:
                    view.setScaleType(ImageView.ScaleType.FIT_XY);
                    break;
                case 7:
                    view.setScaleType(ImageView.ScaleType.MATRIX);
                    break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(tag, ev.getAction() + "--isAutoPlay=" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(toRealPosition(position));
        }
        Log.i(tag, "currentItem=: " + position);

        if (isGesturesCycle) {
            indicatorImages.get((lastPosition - 1 + count) % count).setImageResource(mIndicatorUnselectedResId);
            indicatorImages.get((position - 1 + count) % count).setImageResource(mIndicatorSelectedResId);
        } else {
            indicatorImages.get((lastPosition + count) % count).setImageResource(mIndicatorUnselectedResId);
            indicatorImages.get((position + count) % count).setImageResource(mIndicatorSelectedResId);
        }
        lastPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        Log.i(tag, "onPageScrollStateChanged currentItem: " + currentItem + "__state=" + state);
        if (!isGesturesCycle) return;
        //实现循环逻辑
        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = (position - 1) % count;
        if (realPosition < 0)
            realPosition += count;
        return isGesturesCycle ? realPosition : position;
    }

    /**
     * 开始自动轮播
     */
    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = isGesturesCycle ? currentItem % (count + 1) + 1 : (currentItem + 1) % count;
                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (isGesturesCycle && currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };

    /**
     * 图片适配器
     */
    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(imageViews.get(position));
            View view = imageViews.get(position);

            if (listener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(tag, "position = " + position + "点击了");
                        listener.OnBannerClick(toRealPosition(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
