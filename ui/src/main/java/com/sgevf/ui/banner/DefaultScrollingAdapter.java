package com.sgevf.ui.banner;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.sgevf.ui.R;
import com.sgevf.ui.utils.ToastUtils;

import java.util.List;

public class DefaultScrollingAdapter extends ScrollingAdapter<String> {
    public DefaultScrollingAdapter(Context context, List<String> datas) {
        super(context, datas);
    }

    @Override
    public int loadLayoutRes() {
        return R.layout.item_scrolling_banner;
    }

    @Override
    public void initView(View view, int position, final String data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(getContext(), data, Toast.LENGTH_SHORT);
            }
        });
    }
}
