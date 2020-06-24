package com.sgevf.ui.refreshLayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class RefreshLayout extends FrameLayout {
    private static final String TAG = RefreshLayout.class.getName();
    private int touchSlop;
    private Scroller mScroller;
    private int lastX = 0;
    private int lastY = 0;
    private int offsetY = 0;
    private boolean intercept = false;

    public RefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext(), new LinearInterpolator());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE && intercept) {
            return true;
        }
        if (super.onInterceptTouchEvent(ev)) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                lastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getRawY();
                Log.d(TAG, "onInterceptTouchEvent: " + canScrollVertically(1));
                intercept = y != lastY;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getRawY();
                offsetY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getRawY();
                int scrollY = y - lastY;
                offsetY += scrollY;
                if (offsetY >= 0) {
                    if (canScrollVertically(-1)) {
                        Log.d(TAG, "onTouchEvent: ");
                        getChildAt(0).onTouchEvent(ev);
                    } else {
                        scrollBy(0, -scrollY);
                    }
                } else {
                    if (canScrollVertically(1)) {
                        getChildAt(0).onTouchEvent(ev);
                    } else {
                        scrollBy(0, -scrollY);
                    }
                }
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollY() != 0) {
                    Log.d(TAG, "onTouchEvent: " + getScrollY());
                    mScroller.startScroll(getScrollX(), getScrollY(), 0, -getScrollY());
                    invalidate();
                } else {
                    getChildAt(0).onTouchEvent(ev);
                }
                break;

        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        View view = getChildAt(0);
        if (view instanceof RecyclerView) {
            return view.canScrollVertically(direction);
        }
        return true;
    }
}
