package com.sgevf.ui.superRefreshView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sgevf.ui.R;

public class HeaderView implements IHeaderView {
    Context context;
    View view;
    TextView headerText;

    public HeaderView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.layout_header, null);
        headerText = view.findViewById(R.id.header_text);
    }

    @Override
    public View getHeaderView() {
        return view;
    }

    @Override
    public void onPullReadyState() {
        headerText.setText("初始刷新");
    }

    @Override
    public void onPullDownState() {
        headerText.setText("下拉刷新");
    }

    @Override
    public void onRefreshState() {
        headerText.setText("正在刷新");
    }

    @Override
    public void onPullUpState() {
        headerText.setText("释放刷新");
    }

    @Override
    public void onRefreshFinishState() {
        headerText.setText("刷新结束");
    }

    @Override
    public int getRefreshHeight() {
        return view.getMeasuredHeight();
    }
}
