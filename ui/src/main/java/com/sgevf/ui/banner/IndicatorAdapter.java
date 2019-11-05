package com.sgevf.ui.banner;

import android.content.Context;
import android.view.View;

public abstract class IndicatorAdapter {

    //单个指示器的样式
    public abstract View createIndicatorItem(Context context);

    //选中指示器的样式
    public abstract void selectedIndicator(View item);

    //未选中指示器的样式
    public abstract void unselectedIndicator(View item);


}
