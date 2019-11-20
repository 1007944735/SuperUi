package com.sgevf.ui.superRefreshView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class SuperRefreshView extends LinearLayout {
    private IHeaderView headerView;
    private IFooterView footerView;

    private RecyclerView mRecyclerView;

    public SuperRefreshView(Context context) {
        super(context);
        init(context);
    }

    public SuperRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SuperRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SuperRefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        headerView = new HeaderView(context);
        footerView = new FooterView(context);
        addView(headerView.getHeaderView(), 0);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addView(footerView.getFooterView());
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
