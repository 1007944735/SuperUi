package com.sgevf.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.sgevf.ui.refreshRecyclerView.RefreshLoadListener;
import com.sgevf.ui.refreshRecyclerView.RefreshRecyclerAdapter;

public class RefreshRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout rlNoData;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;

    private View mView;
    //是否显示加载更多
    private boolean isShowFooter = false;
    //是否有更多数据
    private boolean hasMoreData = true;
    //是否初始化加载
//    private boolean isInitLoad = true;
    private int noDataViewResId;
    private View noDataView;

    private RefreshLoadListener mRefreshLoadListener;

    private RefreshRecyclerAdapter mAdapter;

    public RefreshRecyclerView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(attrs);
        initView();
        initListener();
        showData(true);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
        try {
            noDataViewResId = a.getResourceId(R.styleable.RefreshRecyclerView_noDataSrc, R.layout.layout_refresh_recycler_no_data_default);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_recycler_view, this, true);
        rlNoData = mView.findViewById(R.id.rl_no_data);
        swipeRefresh = mView.findViewById(R.id.swipe_refresh);
        recyclerView = mView.findViewById(R.id.recycler_view);
        noDataView = LayoutInflater.from(getContext()).inflate(noDataViewResId, rlNoData, true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && hasMoreData && !isShowFooter && lastVisibleItem == mAdapter.getItemCount() - 1 && null != mRefreshLoadListener && !swipeRefresh.isRefreshing()) {
                    setShowFooter(true);
                    mRefreshLoadListener.loadMore();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                }
            }
        });

        swipeRefresh.setOnRefreshListener(this);
    }

    /**
     * 停止刷新
     *
     * @param moreData 是否有更多数据
     */
    public void stopRefresh(boolean moreData) {
        setHasMoreData(moreData);
        showData(mAdapter.getAdapter().getItemCount() != 0);
        swipeRefresh.setRefreshing(false);
        setShowFooter(!moreData);
    }

    public void showData(boolean moreData) {
        rlNoData.setVisibility(moreData ? GONE : VISIBLE);
        recyclerView.setVisibility(moreData ? VISIBLE : GONE);
    }

    public void setRefreshLoadListener(RefreshLoadListener listener) {
        this.mRefreshLoadListener = listener;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mAdapter == null) {
            mAdapter = new RefreshRecyclerAdapter(getContext(), adapter);
        } else {
            mAdapter.setAdapter(adapter);
        }
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始加载
     */
    public void firstLoad() {
//        if (isInitLoad) {
        swipeRefresh.setRefreshing(true);
        onRefresh();
//        }
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void setShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
        mAdapter.setShowFooter(isShowFooter);
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }

    public void setHasMoreData(boolean moreData) {
        this.hasMoreData = moreData;
        mAdapter.setHasMoreData(hasMoreData);
    }

//    public void setInitLoad(boolean initLoad) {
//        isInitLoad = initLoad;
//    }

    public SwipeRefreshLayout getSwipeRefresh() {
        return swipeRefresh;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public View getNoDataView() {
        return noDataView;
    }

    @Override
    public void onRefresh() {
        if (null != mRefreshLoadListener) {
            setHasMoreData(true);
            setShowFooter(false);
            mRefreshLoadListener.loadRefresh();
        }
    }
}
