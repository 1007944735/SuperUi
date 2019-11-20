package com.sgevf.ui.superRefreshView;

import android.view.View;

public interface IHeaderView {
    View getHeaderView();

    //初始状态
    void onPullReadyState();

    //下拉状态
    void onPullDownState();

    //刷新状态
    void onRefreshState();

    //释放上滑状态
    void onPullUpState();

    //刷新结束状态
    void onRefreshFinishState();

    //刷新的高度,一般的是控件的高度
    int getRefreshHeight();
}
