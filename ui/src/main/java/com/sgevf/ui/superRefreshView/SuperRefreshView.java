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
            int childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin - paddingLeft - paddingRight, MeasureSpec.EXACTLY);
//            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
//                    paddingLeft + paddingRight, getMeasuredWidth() - lp.leftMargin - lp.rightMargin);
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
        int top = 0;
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
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        canScrollHeader = recyclerView.canScrollHorizontally(-1);
                        canScrollFooter = recyclerView.canScrollHorizontally(1);
                    }
                });
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY += ev.getY() - lastY;
                scrollTo(getLeft(), (int) (getTop() - moveY));
                lastY = ev.getY();
                break;
        }
        ;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
