package com.sgevf.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sgevf.ui.R;

import java.lang.reflect.Field;

public class ScrollingBanner extends FrameLayout implements ViewPager.OnPageChangeListener {
    public static final int START_ROLL = 0;
    public static final int STOP_ROLL = 1;
    public static final int SCROLLING = 2;

    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;

    private ViewPager mViewPager;
    private ScrollingAdapter mAdapter;
    private int currentItem;//当前页
    private Handler mHandler;
    private ScrollingScroller mScroller;
    private IndicatorAdapter mIndicatorAdapter;
    private View selectedIndicator;
    //自定义属性
    private int mDelayTime;//延迟时间
    private int mScrollTime;//滑动时间;
    private boolean mScrollState;//滚动状态
    private boolean mAutoScroll;//是否自动滚动
    private int gravity;//指示器的位置
    private ViewGroup box;//指示器盒子


    public ScrollingBanner(@NonNull Context context) {
        this(context, null);
    }

    public ScrollingBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollingBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        declareAttrs(attrs);
        initBanner();
        initViewPagerScroller();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, (int) (widthSize * 0.618));
        }
    }

    private void declareAttrs(AttributeSet attrs) {
        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollingBanner);
            mDelayTime = a.getInt(R.styleable.ScrollingBanner_delayTime, 3000);
            mScrollTime = a.getInt(R.styleable.ScrollingBanner_scrollTime, 800);
            mScrollState = a.getBoolean(R.styleable.ScrollingBanner_scrollState, false);
            mAutoScroll = a.getBoolean(R.styleable.ScrollingBanner_autoScroll, true);
        } finally {
            a.recycle();
        }
    }

    private void initBanner() {
        createView();
        if (mHandler == null) {
            mHandler = new ScrollingHandler(mViewPager, mDelayTime);
        }
    }

    private void createView() {
        mViewPager = new ViewPager(getContext());
        mViewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);
        addView(mViewPager);
    }

    //初始化指示器
    private void initIndicator() {
        //创建指示器的容器
        box = createIndicatorBox();
        for (int i = 0; i < mAdapter.getRelCount(); i++) {
            if (mIndicatorAdapter == null) {
                //新建默认指示器
                mIndicatorAdapter = new DefaultIndicatorAdapter();
            }
            if (mIndicatorAdapter.createIndicatorItem(getContext()) == null) {
                throw new RuntimeException("indicator is null");
            }
            box.addView(mIndicatorAdapter.createIndicatorItem(getContext()));
        }
        addView(box);
    }

    private ViewGroup createIndicatorBox() {
        LinearLayout box = new LinearLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        box.setLayoutParams(params);
        box.setOrientation(LinearLayout.HORIZONTAL);
        box.setPadding(10, 10, 10, 10);
        box.setGravity(Gravity.CENTER);
        return box;
    }

    public void setBannerAdapter(ScrollingAdapter adapter) {
        if (mViewPager == null) {
            return;
        }
        this.mAdapter = adapter;
        mViewPager.setAdapter(adapter);
        //初始化指示器
        initIndicator();
        setCurrentItem(0);
        if (mAutoScroll) {
            start();
        }
    }

    //开始滚动
    public void start() {
        if (mHandler != null && !mScrollState) {
            mHandler.sendMessage(createBundle(START_ROLL));
            mScrollState = true;
        }
    }

    private Message createBundle(int action) {
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt("action", action);
        msg.setData(bundle);
        return msg;
    }

    //停止滚动
    public void stop() {
        if (mHandler != null && mScrollState) {
            mHandler.sendMessage(createBundle(STOP_ROLL));
            mScrollState = false;
        }
    }

    //将viewpager中的scroller替换成ScrollingBanner
    private void initViewPagerScroller() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            mScroller = new ScrollingScroller(getContext(), new AccelerateDecelerateInterpolator());
            mScroller.setDuration(mScrollTime);
            scroller.set(mViewPager, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        Log.d("TAG", "onPageScrolled: " + i);
        Log.d("TAG", "onPageScrolled: " + v);
    }

    @Override
    public void onPageSelected(int i) {
        Log.d("TAG", "onPageSelected: " + i);
        currentItem = i;
        if (i == 0 || i == mAdapter.getCount() - 1) {
            return;
        }
        if (selectedIndicator != null) {
            mIndicatorAdapter.unselectedIndicator(selectedIndicator);
        }
        selectedIndicator = box.getChildAt(i - 1);
        mIndicatorAdapter.selectedIndicator(selectedIndicator);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        Log.d("TAG", "onPageScrollStateChanged: " + i);
        if (i == ViewPager.SCROLL_STATE_IDLE) {
            if (currentItem == 0) {
                mViewPager.setCurrentItem(mAdapter.getCount() - 2, false);
            } else if (currentItem == mAdapter.getCount() - 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }
    }

    public boolean isScrollState() {
        return mScrollState;
    }

    public boolean isAutoScroll() {
        return mAutoScroll;
    }


    public void setDelayTime(int delayTime) {
        this.mDelayTime = delayTime;
    }

    public void setScrollTime(int scrollTime) {
        this.mScrollTime = scrollTime;
        mScroller.setDuration(mScrollTime);
    }

    public void destroy() {
        if (mHandler != null) {
            if (mScrollState) {
                mHandler.sendMessage(createBundle(STOP_ROLL));
                mScrollState = false;
            }
            mHandler = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                break;
            case MotionEvent.ACTION_MOVE:
                return super.dispatchTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                start();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //设置当前页面
    public void setCurrentItem(int i) {
        setCurrentPos(i, false);
    }

    //设置当前页面
    public void setCurrentPos(int i, boolean smoothScroll) {
        if (i < 0 || i > mAdapter.getRelCount()) {
            return;
        }
        mViewPager.setCurrentItem(i + 1, smoothScroll);
        if (selectedIndicator != null) {
            mIndicatorAdapter.unselectedIndicator(selectedIndicator);
        }
        selectedIndicator = box.getChildAt(i);
        mIndicatorAdapter.selectedIndicator(selectedIndicator);
    }

    public void setIndicatorAdapter(IndicatorAdapter indicatorAdapter) {
        this.mIndicatorAdapter = indicatorAdapter;
        selectedIndicator=null;

    }
}
