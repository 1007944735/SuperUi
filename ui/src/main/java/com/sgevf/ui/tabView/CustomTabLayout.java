package com.sgevf.ui.tabView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sgevf.ui.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

//https://www.jianshu.com/p/83922d08250b
public class CustomTabLayout extends FrameLayout implements TabLayout.BaseOnTabSelectedListener {
    private static final int DEFAULT_TAB_TEXT_SIZE = 20;
    private static final int DEFAULT_TAB_TEXT_COLOR = Color.parseColor("#333333");
    private static final int DEFAULT_TAB_BACKGROUND_COLOR = Color.WHITE;
    private static final int DEFAULT_TAB_INDICATOR_HEIGHT = 10;
    private static final int DEFAULT_TAB_INDICATOR_WIDTH = ViewGroup.LayoutParams.MATCH_PARENT;
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
        this(context, attrs, defStyleAttr, 0);
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
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(item));
    }

    private View getItemView(String tab) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab_layout, null);
        LinearLayout llCustomTab = view.findViewById(R.id.ll_custom_tab);
        //背景色
//        llCustomTab.setBackgroundColor(mTabBackgroundColor);
        //字体
        TextView mTabText = view.findViewById(R.id.custom_tab_text);
        mTabText.setText(tab);
        mTabText.setTextSize(Dimension.PX, mTabTextSize);
        mTabText.setTextColor(mTabTextColor);
        //下划线
        View mTabIndicator = view.findViewById(R.id.custom_tab_indicator);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabIndicator.getLayoutParams();
        lp.width = 100;
        lp.height = mTabIndicatorHeight;
        mTabIndicator.setLayoutParams(lp);
        return view;
    }


    public List<View> getTabViewLists() {
        return mTabViewLists;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //改变每个tab 的状态
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View view = mTabLayout.getTabAt(i).getCustomView();
            if (view == null) {
                return;
            }
            TextView mTabText = view.findViewById(R.id.custom_tab_text);
            View mTabIndicator = view.findViewById(R.id.custom_tab_indicator);
            LinearLayout llCustomTab = view.findViewById(R.id.ll_custom_tab);
            if (i == tab.getPosition()) {
                //选中
                mTabText.setTextSize(Dimension.PX, mTabSelectedTextSize);
                mTabText.setTextColor(mTabSelectedTextColor);
                mTabIndicator.setVisibility(VISIBLE);
                mTabIndicator.setBackgroundColor(mTabIndicatorColor);
//                llCustomTab.setBackgroundColor(mTabSelectedBackgroundColor);
            } else {
                mTabText.setTextSize(Dimension.PX, mTabTextSize);
                mTabText.setTextColor(mTabTextColor);
                mTabIndicator.setVisibility(INVISIBLE);
//                llCustomTab.setBackgroundColor(mTabBackgroundColor);
            }
        }
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

    //关联viewpager
    public void setupWithViewPager(ViewPager viewPager) {
        mTabLayout.addOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager, this));
    }

    private class ViewPagerOnTabSelectedListener implements TabLayout.BaseOnTabSelectedListener {
        private ViewPager mViewPager;
        private WeakReference<CustomTabLayout> mTabLayout;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager, CustomTabLayout tabLayout) {
            this.mViewPager = viewPager;
            mTabLayout = new WeakReference<>(tabLayout);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition());
            CustomTabLayout customTabLayout = mTabLayout.get();
            if (customTabLayout != null) {
                List<View> tabs = customTabLayout.getTabViewLists();
                if (tabs == null || tabs.isEmpty()) {
                    return;
                }
                for (int i = 0; i < tabs.size(); i++) {
                    View view = tabs.get(i);
                    if (view == null) {
                        return;
                    }
                    TextView mTabText = view.findViewById(R.id.custom_tab_text);
                    View mTabIndicator = view.findViewById(R.id.custom_tab_indicator);
                    LinearLayout llCustomTab = view.findViewById(R.id.ll_custom_tab);
                    if (i == tab.getPosition()) {
                        //选中
                        mTabText.setTextSize(Dimension.PX, mTabSelectedTextSize);
                        mTabText.setTextColor(mTabSelectedTextColor);
                        mTabIndicator.setVisibility(VISIBLE);
                        mTabIndicator.setBackgroundColor(mTabIndicatorColor);
                        llCustomTab.setBackgroundColor(mTabSelectedBackgroundColor);
                    } else {
                        mTabText.setTextSize(Dimension.PX, mTabTextSize);
                        mTabText.setTextColor(mTabTextColor);
                        mTabIndicator.setVisibility(INVISIBLE);
                        llCustomTab.setBackgroundColor(mTabBackgroundColor);
                    }
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
