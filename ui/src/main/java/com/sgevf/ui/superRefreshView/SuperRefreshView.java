package com.sgevf.ui.superRefreshView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        headerHeight = headerView.getRefreshHeight();
        footerHeight = footerView.getRefreshHeight();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(getContext(), new LinearInterpolator());

        addHeaderView();
        footerView = new FooterView(getContext());
        addView(headerView.getHeaderView(), 0);
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
                addView(footerView.getFooterView());
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void addHeaderView(){
        if(headerView==null){
            headerView = new HeaderView(getContext());
        }
        if(indexOfChild(headerView.getHeaderView())==-1){

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
    public boolean onTouchEvent(MotionEvent event) {
        scrollTo(getLeft(), (int) (getTop() + headerHeight - moveY));
        Log.d("TAG", "onTouchEvent: " + (getTop() + headerHeight - moveY));
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
