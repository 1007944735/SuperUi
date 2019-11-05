package com.sgevf.ui.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ScrollingScroller extends Scroller {
    private int mDuration;
    public ScrollingScroller(Context context) {
        super(context);
    }

    public ScrollingScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }
}
