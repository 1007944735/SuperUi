package com.sgevf.ui.banner;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sgevf.ui.R;
import com.sgevf.ui.utils.DensityUtil;

/**
 * 默认指示器
 */
public class DefaultIndicatorAdapter extends IndicatorAdapter {
    @Override
    public View createIndicatorItem(Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 8));
        params.setMargins(10, 0, 10, 0);
        tv.setLayoutParams(params);
        tv.setBackgroundResource(R.drawable.style_indicator_circle_selector);
        return tv;
    }

    @Override
    public void selectedIndicator(View item) {
        item.setSelected(true);
    }

    @Override
    public void unselectedIndicator(View item) {
        item.setSelected(false);
    }
}
