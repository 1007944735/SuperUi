package com.sgevf.ui.superRefreshView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sgevf.ui.R;

public class FooterView implements IFooterView {
    Context context;
    View view;
    TextView footerText;

    public FooterView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.layout_footer, null);
        footerText = view.findViewById(R.id.footer_text);
    }

    @Override
    public View getFooterView() {
        return view;
    }

    @Override
    public void onSlideReadyState() {
        footerText.setText("初始刷新");
    }

    @Override
    public void onSlideUpState() {
        footerText.setText("上拉刷新");
    }

    @Override
    public void onRefreshState() {
        footerText.setText("正在刷新");
    }

    @Override
    public void onSlideDownState() {
        footerText.setText("释放刷新");
    }

    @Override
    public void onRefreshFinishState() {
        footerText.setText("刷新结束");
    }

    @Override
    public int getRefreshHeight() {
        return view.getMeasuredHeight();
    }
}
