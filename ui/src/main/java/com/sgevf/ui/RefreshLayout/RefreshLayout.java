package com.sgevf.ui.RefreshLayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RefreshLayout extends LinearLayout implements View.OnTouchListener {
    private View mHeaderView;
    private View mFooterView;
    private ListView mContentView;
    //最小滑动距离
    private int touchSlop;
    //onlayout是否加载过
    private boolean layoutOnce;
    //headerView高度
    private int mHeaderHeight;
    private LayoutParams headerViewParam;
    //是否可以下拉
    private boolean ableToPull;
    private float lastY;

    private RefreshStatus status;

    private RefreshListener listener;


    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        addHeaderView();
        status = RefreshStatus.STATUS_NORMAL;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !layoutOnce) {
            mHeaderHeight = ((IHeaderCallBack) mHeaderView).getHeaderHeight();
            headerViewParam = (LayoutParams) mHeaderView.getLayoutParams();
            headerViewParam.topMargin = -mHeaderHeight;
            mHeaderView.setLayoutParams(headerViewParam);
            mContentView = (ListView) getChildAt(1);
            mContentView.setOnTouchListener(this);
            layoutOnce = true;
        }
    }

    private void addHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = new HeaderView(getContext());
        }
        addView(mHeaderView, 0);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ableToPull = isAbleToPull(event);
        if (ableToPull) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveY = event.getRawY();
                    int distance = (int) (moveY - lastY);
                    if (distance <= 0 && headerViewParam.topMargin <= -mHeaderHeight) {
                        return false;
                    }
                    if (distance < touchSlop) {
                        return false;
                    }
                    if (status != RefreshStatus.STATUS_REFRESHING) {
                        status = headerViewParam.topMargin > 0 ? RefreshStatus.STATUS_RELEASE_TO_REFRESH : RefreshStatus.STATUS_PULL_TO_REFRESH;
                        headerViewParam.topMargin = distance / 2 - mHeaderHeight;
                        mHeaderView.setLayoutParams(headerViewParam);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (status == RefreshStatus.STATUS_RELEASE_TO_REFRESH) {
                        startRefreshAnimation();
                        //执行刷新任务
                        if(listener!=null){
                            listener.onRefresh();
                        }
                    } else if (status == RefreshStatus.STATUS_PULL_TO_REFRESH) {
                        //隐藏下拉头
                        startHideAnimation();
                    }
                    break;
            }
            if(status==RefreshStatus.STATUS_RELEASE_TO_REFRESH||status==RefreshStatus.STATUS_PULL_TO_REFRESH){
                updateHeaderView();
                mContentView.setPressed(false);
                mContentView.setFocusable(false);
                mContentView.setFocusableInTouchMode(false);
                return true;
            }
        }
        return false;
    }

    /**
     * 更新下拉头信息
     */
    private void updateHeaderView() {
        switch (status){
            case STATUS_NORMAL:
                ((IHeaderCallBack) mHeaderView).onStateNormal();
                break;
            case STATUS_REFRESHING:
                ((IHeaderCallBack) mHeaderView).onStateRefreshing();
                break;
            case STATUS_PULL_TO_REFRESH:
                ((IHeaderCallBack) mHeaderView).onStatePull();
                break;
            case STATUS_RELEASE_TO_REFRESH:
                ((IHeaderCallBack) mHeaderView).onStateRelease(true);
                break;
        }
    }

    //判断是否可以下拉
    private boolean isAbleToPull(MotionEvent event) {
        View firstChild = mContentView.getChildAt(0);
        if (mContentView != null) {
            int firstVisiblePos = mContentView.getFirstVisiblePosition();
            if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
                if (!ableToPull) {
                    lastY = event.getRawY();
                }
                ableToPull = true;
            } else {
                if (headerViewParam.topMargin != -mHeaderHeight) {
                    headerViewParam.topMargin = -mHeaderHeight;
                    mHeaderView.setLayoutParams(headerViewParam);
                }
                ableToPull = false;
            }
        } else {
            ableToPull = true;
        }
        return ableToPull;
    }

    public void finishRefreshing(){
        //隐藏下拉头
        startHideAnimation();
    }

    public void setRefreshListener(RefreshListener listener){
        this.listener=listener;
    }

    private void startHideAnimation(){
        status=RefreshStatus.STATUS_NORMAL;
        updateHeaderView();
        ValueAnimator animator=ValueAnimator.ofInt(headerViewParam.topMargin,-mHeaderHeight);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int a= (int) animation.getAnimatedValue();
                headerViewParam.topMargin=a;
                mHeaderView.setLayoutParams(headerViewParam);
            }
        });
    }

    private void startRefreshAnimation(){
        status=RefreshStatus.STATUS_REFRESHING;
        updateHeaderView();
        Log.d("TAG", "startRefreshAnimation: "+headerViewParam.topMargin);
        ValueAnimator animator=ValueAnimator.ofInt(headerViewParam.topMargin,0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int a= (int) animation.getAnimatedValue();
                headerViewParam.topMargin=a;
                mHeaderView.setLayoutParams(headerViewParam);
            }
        });
    }


}
