package com.sgevf.ui.RefreshLayout;

public interface IFooterCallBack {
    /**
     * FooterView显示
     */
    void show();

    /**
     * FooterView隐藏
     */
    void hide();

    /**
     * 正常状态
     */
    void onStateNormal();

    /**
     * 上拉刷新状态
     */
    void onStatePull();

    /**
     * 下拉释放状态
     */
    void onStateRelease();

    /**
     * 刷新状态
     * @param
     */
    void onStateRefreshing();

    /**
     * FooterView高度
     * @return
     */
    int getFooterHeight();
}
