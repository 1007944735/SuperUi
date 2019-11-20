package com.sgevf.ui.superRefreshView;

import android.view.View;

public interface IFooterView {
    View getFooterView();

    //初始状态
    void onSlideReadyState();

    //上拉状态
    void onSlideUpState();

    //刷新状态
    void onRefreshState();

    //释放下拉状态
    void onSlideDownState();

    //刷新结束状态
    void onRefreshFinishState();

    //刷新的高度,一般的是控件的高度
    int getRefreshHeight();
}
