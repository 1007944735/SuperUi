package com.sgevf.ui.banner;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class DefaultIndicatorAdapter extends IndicatorAdapter {
    @Override
    public View createIndicatorItem(Context context) {
        TextView tv = new TextView(context);
        tv.setHeight(20);
        tv.setWidth(20);
        tv.setBackgroundColor(Color.BLUE);
        return tv;
    }

    @Override
    public void selectedIndicator(View item) {
        item.setBackgroundColor(Color.RED);
    }

    @Override
    public void unselectedIndicator(View item) {
        item.setBackgroundColor(Color.BLUE);
    }
}
