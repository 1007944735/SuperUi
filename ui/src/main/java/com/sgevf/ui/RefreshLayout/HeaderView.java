package com.sgevf.ui.RefreshLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sgevf.ui.R;

public class HeaderView extends LinearLayout implements IHeaderCallBack{
    private ViewGroup header;
    private TextView tvStatus;
    private ProgressBar progress;
    public HeaderView(Context context) {
        this(context,null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        header= (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view,this);
        tvStatus=header.findViewById(R.id.tv_status);
        progress=header.findViewById(R.id.progress);
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
    public void onStateRefreshing() {
        tvStatus.setVisibility(GONE);
        progress.setVisibility(VISIBLE);
        tvStatus.setText("正在刷新");
    }

    @Override
    public void onStateRelease() {
        tvStatus.setVisibility(VISIBLE);
        progress.setVisibility(GONE);
        tvStatus.setText("释放刷新");
    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }
}
