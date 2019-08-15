package com.sgevf.ui.RefreshLayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MenuCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RefreshLayout extends LinearLayout implements View.OnTouchListener {
    private static final String TAG = "RefreshLayout";
    private View mHeaderView;
    private View mFooterView;
    private ListView mContentView;
    //最小滑动距离
    private int touchSlop;
    //onlayout是否加载过
    private boolean layoutOnce;
    //headerView高度
    private int mHeaderHeight;
    //footerView高度
    private int mFooterHeight;
    private LayoutParams headerViewParam;
    private LayoutParams footerViewParam;
    private LayoutParams contentViewParam;
    //是否可以下拉
    private boolean ableToPull;
    //是否可以上拉
    private boolean ableToRelease;
    private float lastY;
    private int mContentViewCount;

    private RefreshStatus headerStatus;
    private RefreshStatus footerStatus;

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
        addFooterView();
        headerStatus = RefreshStatus.STATUS_NORMAL;
        footerStatus = RefreshStatus.STATUS_NORMAL;
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
            mContentViewCount = mContentView.getCount();
            mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);

            contentViewParam = (LayoutParams) mContentView.getLayoutParams();
//            contentViewParam.weight = 1;
//            contentViewParam.height = 0;
//            mContentView.setLayoutParams(contentViewParam);

            mContentView.setOnTouchListener(this);
            layoutOnce = true;
        }
    }

    private void addHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = new HeaderView(getContext());
        }
        ViewGroup parent = (ViewGroup) mHeaderView.getParent();
        if (parent != null) {
            parent.removeView(mHeaderView);
        }
        addView(mHeaderView, 0);
    }

    private void addFooterView() {
        if (mFooterView == null) {
            mFooterView = new FooterView(getContext());
        }
        ViewGroup parent = (ViewGroup) mFooterView.getParent();
        if (parent != null) {
            parent.removeView(mFooterView);
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addView(mFooterView, 2);
                mFooterView.measure(0, 0);
                mFooterHeight = ((IFooterCallBack) mFooterView).getFooterHeight();
                footerViewParam = (LayoutParams) mFooterView.getLayoutParams();
//                footerViewParam.bottomMargin = -mFooterHeight;
                mFooterView.setLayoutParams(footerViewParam);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ableToPull = isAbleToPull(event);
        ableToRelease = isAbleToRelease(event);
        if (ableToPull) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveY = event.getRawY();
                    int distance = (int) (moveY - lastY);
                    if (Math.abs(distance) < touchSlop) {
                        return false;
                    }
                    if (distance < 0 && headerViewParam.topMargin < -mHeaderHeight) {
                        return false;
                    }
                    if (headerStatus != RefreshStatus.STATUS_REFRESHING) {
                        headerStatus = headerViewParam.topMargin > 0 ? RefreshStatus.STATUS_RELEASE_TO_REFRESH : RefreshStatus.STATUS_PULL_TO_REFRESH;
                        headerViewParam.topMargin = distance / 2 - mHeaderHeight;
                        mHeaderView.setLayoutParams(headerViewParam);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (headerStatus == RefreshStatus.STATUS_RELEASE_TO_REFRESH) {
                        startHeaderRefreshAnimation();
                        //执行刷新任务
                        if (listener != null) {
                            listener.onRefresh();
                        }
                    } else if (headerStatus == RefreshStatus.STATUS_PULL_TO_REFRESH) {
                        //隐藏下拉头
                        startHeaderHideAnimation();
                    }
                    break;
            }
            if (headerStatus == RefreshStatus.STATUS_RELEASE_TO_REFRESH || headerStatus == RefreshStatus.STATUS_PULL_TO_REFRESH) {
                updateHeaderView();
                mContentView.setPressed(false);
                mContentView.setFocusable(false);
                mContentView.setFocusableInTouchMode(false);
                return true;
            }
        }

        if (ableToRelease) {
            Log.d(TAG, "footerStatus: " + footerStatus);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveY = event.getRawY();
                    int distance = (int) (moveY - lastY);
                    Log.d(TAG, "distance: " + distance);
                    mContentView.offsetTopAndBottom(-5);
                    mFooterView.offsetTopAndBottom(-5);
                    break;
                case MotionEvent.ACTION_UP:
                default:

                    break;
            }
        }
        return false;
    }

    /**
     * 判断是否上拉
     *
     * @param event
     * @return
     */
    private boolean isAbleToRelease(MotionEvent event) {
        if (mContentView != null) {
            int lastVisiblePos = mContentView.getLastVisiblePosition();
            int firstVisiblePos = mContentView.getFirstVisiblePosition();
            View lastView = mContentView.getChildAt(lastVisiblePos - firstVisiblePos);
            if (lastVisiblePos == mContentViewCount - 1 && mContentView.getHeight() >= lastView.getBottom()) {
//                if (!ableToRelease) {
//                    lastY = event.getRawY();
//                }
                return true;
            } else {
                if (footerViewParam.bottomMargin != -mFooterHeight) {
                    footerViewParam.bottomMargin = -mFooterHeight;
                    mFooterView.setLayoutParams(footerViewParam);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 更新下拉头信息
     */
    private void updateHeaderView() {
        switch (headerStatus) {
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
                ((IHeaderCallBack) mHeaderView).onStateRelease();
                break;
        }
    }

    /**
     * 更新上拉头信息
     */
    private void updateFooterView() {
        switch (headerStatus) {
            case STATUS_NORMAL:
                ((IFooterCallBack) mFooterView).onStateNormal();
                break;
            case STATUS_REFRESHING:
                ((IFooterCallBack) mFooterView).onStateRefreshing();
                break;
            case STATUS_PULL_TO_REFRESH:
                ((IFooterCallBack) mFooterView).onStatePull();
                break;
            case STATUS_RELEASE_TO_REFRESH:
                ((IFooterCallBack) mHeaderView).onStateRelease();
                break;
        }
    }

    //判断是否可以下拉
    private boolean isAbleToPull(MotionEvent event) {
        if (mContentView != null) {
            View firstChild = mContentView.getChildAt(0);
            int firstVisiblePos = mContentView.getFirstVisiblePosition();
            if (firstVisiblePos == 0 && firstChild.getTop() >= 0) {
//                if (!ableToPull) {
//                    lastY = event.getRawY();
//                }
                Log.d(TAG, "firstVisiblePos: " + firstVisiblePos);
                Log.d(TAG, "firstChild.getTop(): " + firstChild.getTop());
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

    public void finishHeadRefreshing() {
        //隐藏下拉头
        startHeaderHideAnimation();
    }

    public void finishFootRefreshing() {
        //隐藏下拉头
        startFooterHideAnimation();
    }

    public void setRefreshListener(RefreshListener listener) {
        this.listener = listener;
    }

    private void startHeaderHideAnimation() {
        headerStatus = RefreshStatus.STATUS_NORMAL;
        updateHeaderView();
        ValueAnimator animator = ValueAnimator.ofInt(headerViewParam.topMargin, -mHeaderHeight);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int a = (int) animation.getAnimatedValue();
                headerViewParam.topMargin = a;
                mHeaderView.setLayoutParams(headerViewParam);
            }
        });
    }

    private void startFooterHideAnimation() {
        footerStatus = RefreshStatus.STATUS_NORMAL;
        updateFooterView();
        ValueAnimator animator = ValueAnimator.ofInt(mFooterView.getTop(), getHeight());
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int a = (int) animation.getAnimatedValue();
                mFooterView.setTop(a);
            }
        });
    }

    private void startHeaderRefreshAnimation() {
        headerStatus = RefreshStatus.STATUS_REFRESHING;
        updateHeaderView();
        ValueAnimator animator = ValueAnimator.ofInt(headerViewParam.topMargin, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int a = (int) animation.getAnimatedValue();
                headerViewParam.topMargin = a;
                mHeaderView.setLayoutParams(headerViewParam);
            }
        });
    }


    private void startFooterRefreshAnimation() {
        footerStatus = RefreshStatus.STATUS_REFRESHING;
        updateFooterView();
        ValueAnimator animator = ValueAnimator.ofInt(mFooterView.getTop(), getHeight() - mFooterView.getHeight());
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int a = (int) animation.getAnimatedValue();
                mFooterView.setTop(a);
            }
        });
    }

}
