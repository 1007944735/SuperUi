package com.sgevf.ui.horizontalScrollerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class HorizontalScrollerView extends RecyclerView {
    private LinearLayoutManager layoutManager;
    private int offset;

    public HorizontalScrollerView(@NonNull Context context) {
        this(context, null);
    }

    public HorizontalScrollerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
        addOnScrollListener(mOnScrollListener);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private final OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                smoothScrollBy(offset, 0, new DecelerateInterpolator());
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            int position = layoutManager.findFirstVisibleItemPosition();
            View view = layoutManager.findViewByPosition(position);
            int left = view.getLeft();
            int width = view.getWidth();
            if (Math.abs(left) <= width / 2) {
                offset = left;
            } else {
                if (position == getChildCount() - 1) {
                    offset = left;
                } else {
                    offset = width + left;
                }
            }
        }
    };
}
