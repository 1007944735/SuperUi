package com.sgevf.ui.RefreshLayout;

public interface IHeaderCallBack {
    /**
     * 显示headerView
     */
    void show();

    /**
     * 隐藏headerView
     */
    void hide();

    /**
     * 正常状态
     */
    void onStateNormal();

    /**
     * 准备刷新状态，下拉未刷新
     */
    void onStatePull();

    /**
     * 刷新状态
     */
    void onStateRefreshing();

    /**
     * 刷新结束状态
     * @param success
     */
    void onStateRelease(boolean success);

    /**
     * 获取headerView高度
     * @return
     */
    int getHeaderHeight();
}
