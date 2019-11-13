package com.sgevf.ui.tabView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sgevf.ui.R;

import java.util.ArrayList;
import java.util.List;

//https://www.jianshu.com/p/83922d08250b
public class CustomTabLayout extends FrameLayout implements TabLayout.BaseOnTabSelectedListener {
    private static final int DEFAULT_TAB_TEXT_SIZE = 20;
    private static final int DEFAULT_TAB_TEXT_COLOR = Color.parseColor("#333333");
    private static final int DEFAULT_TAB_BACKGROUND_COLOR = Color.WHITE;
    private static final int DEFAULT_TAB_INDICATOR_HEIGHT = 10;
    private static final int DEFAULT_TAB_INDICATOR_WIDTH = 0;
    private static final int DEFAULT_TAB_INDICATOR_COLOR = Color.BLUE;
    private int mTabTextSize;//tab 字体大小
    private int mTabSelectedTextSize;//tab 选中字体大小
    private int mTabTextColor;//tab 字体颜色
    private int mTabSelectedTextColor;//tab 选中字体颜色
    private int mTabBackgroundColor;//tab 背景颜色
    private int mTabSelectedBackgroundColor;//tab 选中背景颜色
    private int mTabIndicatorHeight;//tab 下划线高度
    private int mTabIndicatorWidth;//tab 下划线宽度
    private int mTabIndicatorColor;//tab 下划线颜色
    private int mTabMode;//tab 是否滚动

    private TabLayout mTabLayout;

    private List<String> mTabTextLists;
    private List<View> mTabViewLists;

    public CustomTabLayout(@NonNull Context context) {
        this(context, null);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout);
            mTabTextSize = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabTextSize, DEFAULT_TAB_TEXT_SIZE);
            mTabSelectedTextSize = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabSelectedTextSize, DEFAULT_TAB_TEXT_SIZE);
            mTabTextColor = a.getColor(R.styleable.CustomTabLayout_tabTextColor, DEFAULT_TAB_TEXT_COLOR);
            mTabSelectedTextColor = a.getColor(R.styleable.CustomTabLayout_tabSelectedTextColor, DEFAULT_TAB_TEXT_COLOR);
            mTabBackgroundColor = a.getColor(R.styleable.CustomTabLayout_tabBackgroundColor, DEFAULT_TAB_BACKGROUND_COLOR);
            mTabSelectedBackgroundColor = a.getColor(R.styleable.CustomTabLayout_tabSelectedBackgroundColor, DEFAULT_TAB_BACKGROUND_COLOR);
            mTabIndicatorHeight = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabIndicatorHeight, DEFAULT_TAB_INDICATOR_HEIGHT);
            mTabIndicatorWidth = a.getDimensionPixelSize(R.styleable.CustomTabLayout_tabIndicatorWidth, DEFAULT_TAB_INDICATOR_WIDTH);
            mTabIndicatorColor = a.getColor(R.styleable.CustomTabLayout_tabIndicatorColor, DEFAULT_TAB_INDICATOR_COLOR);
            mTabMode = a.getInt(R.styleable.CustomTabLayout_tabType, 1);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tab_layout, this, true);
        mTabLayout = view.findViewById(R.id.custom_tab_layout);
        mTabTextLists = new ArrayList<>();
        mTabViewLists = new ArrayList<>();
        //设置属性
        mTabLayout.setTabMode(mTabMode == 1 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        mTabLayout.addOnTabSelectedListener(this);
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public void addTabItem(String tab) {
        if (TextUtils.isEmpty(tab)) return;
        mTabTextLists.add(tab);
        View item = getItemView(tab);
        mTabViewLists.add(item);


    }

    private View getItemView(String tab) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab_layout, null);
        LinearLayout llCustomTab=view.findViewById(R.id.ll_custom_tab);
        TextView mTabText=view.findViewById(R.id.custom_tab_text);
        mTabText.setText(tab);
        View mTabIndicator=view.findViewById(R.id.custom_tab_indicator);

        return view;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public void addOnTabSelectedListener(TabLayout.OnTabSelectedListener onTabSelectedListener) {
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }
}
