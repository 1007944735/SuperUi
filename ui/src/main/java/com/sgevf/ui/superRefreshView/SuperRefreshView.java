package com.sgevf.ui.superRefreshView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SuperRefreshView extends LinearLayout {
    private IHeaderView headerView;
    private IFooterView footerView;

    private Scroller mScroller;

    private RecyclerView mRecyclerView;
    private float lastY;
    private float moveY;

    private int headerHeight;
    private int footerHeight;

    private boolean canScrollHeader;
    private boolean canScrollFooter;

    private boolean isRecyclerScroll;

    public SuperRefreshView(Context context) {
        super(context);
        init();
    }

    public SuperRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SuperRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SuperRefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = 0;
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
//            int childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin - paddingLeft - paddingRight, MeasureSpec.EXACTLY);
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                    paddingLeft + paddingRight, getMeasuredWidth() - lp.leftMargin - lp.rightMargin);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                    paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
            heightSize += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        }
        setMeasuredDimension(widthSize, heightSize);
        if (headerView != null) {
            headerHeight = headerView.getRefreshHeight();
        }
        if (footerView != null) {
            footerHeight = footerView.getRefreshHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = -headerHeight;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(0, top, child.getMeasuredWidth(), top + child.getMeasuredHeight());
            top += child.getMeasuredHeight();
            Log.d("TAG", "onLayout: " + top);
        }
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(getContext(), new LinearInterpolator());
        addHeaderView();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView = (RecyclerView) getChildAt(1);
//                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//
//                        Log.d("TAG", "onScrolled: "+canScrollFooter);
//                    }
//                });
                addFooterView();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void addHeaderView() {
        if (headerView == null) {
            headerView = new HeaderView(getContext());
        }
        if (indexOfChild(headerView.getHeaderView()) == -1) {
            ViewGroup parent = (ViewGroup) headerView.getHeaderView().getParent();
            if (parent != null) {
                parent.removeView(headerView.getHeaderView());
            }
            addView(headerView.getHeaderView(), 0);
        }
    }

    private void addFooterView() {
        if (footerView == null) {
            footerView = new FooterView(getContext());
        }
        if (indexOfChild(footerView.getFooterView()) == -1) {
            ViewGroup parent = (ViewGroup) footerView.getFooterView().getParent();
            if (parent != null) {
                parent.removeView(footerView.getFooterView());
            }
            addView(footerView.getFooterView(), 2);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        canScrollHeader = mRecyclerView.canScrollVertically(-1);
        Log.d("TAG", "canScrollHeader: " + canScrollHeader);
        canScrollFooter = mRecyclerView.canScrollVertically(1);
        Log.d("TAG", "canScrollFooter: " + canScrollFooter);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                if (canScrollHeader && canScrollFooter) {
                    isRecyclerScroll = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float move = ev.getY() - lastY;
//                Log.d("TAG", "dispatchTouchEvent: " + getScrollY());
                if (move > 0) {
                    //下拉
                    if ((!canScrollHeader && getScrollY() <= 0)) {
                        if (isRecyclerScroll) {
                            lastY = ev.getY();
                            Log.d("TAG", "dispatchTouchEvent: " + lastY);
                            isRecyclerScroll = false;
                        }
                        moveY += ev.getY() - lastY;
                        Log.d("TAG", "dispatchTouch1: " + getTop());
                        scrollTo(getLeft(), (int) (getTop() - moveY));
                        lastY = ev.getY();
                        return true;
                    }
                } else {
                    //上滑
                    if ((!canScrollFooter && getScaleY() >= 0)) {
                        if (isRecyclerScroll) {
                            lastY = ev.getY();
                            Log.d("TAG", "dispatchTouchEvent: " + lastY);
                            isRecyclerScroll = false;
                        }
                        moveY += ev.getY() - lastY;
                        scrollTo(getLeft(), (int) (getTop() - moveY));
                        lastY = ev.getY();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getScaleY() != 0) {
                    mScroller.startScroll(getScrollX(), getScrollY(), 0, 0 - getScrollY(), 500);
                    invalidate();
                }
                moveY = 0;
                break;
        }
        return mRecyclerView.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
