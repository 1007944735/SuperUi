package com.sgevf.ui.RefreshLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sgevf.ui.R;

public class FooterView extends LinearLayout implements IFooterCallBack {
    private View footer;
    private ProgressBar progress;
    private TextView tvStatus;
    public FooterView(Context context) {
        this(context,null);
    }

    public FooterView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FooterView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        footer= LayoutInflater.from(context).inflate(R.layout.layout_refresh_footer_view,this);
        progress=footer.findViewById(R.id.progress);
        tvStatus=footer.findViewById(R.id.tv_status);
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void onStateNormal() {
        tvStatus.setVisibility(VISIBLE);
        progress.setVisibility(GONE);
        tvStatus.setText("下拉刷新");
    }

    @Override
    public void onStatePull() {
        tvStatus.setVisibility(VISIBLE);
        progress.setVisibility(GONE);
        tvStatus.setText("下拉刷新");
    }

    @Override
    public void onStateRelease() {
        tvStatus.setVisibility(VISIBLE);
        progress.setVisibility(GONE);
        tvStatus.setText("释放刷新");
    }

    @Override
    public void onStateRefreshing() {
        tvStatus.setVisibility(GONE);
        progress.setVisibility(VISIBLE);
        tvStatus.setText("正在刷新");
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
