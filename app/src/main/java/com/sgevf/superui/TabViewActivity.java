package com.sgevf.superui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sgevf.ui.tabView.CustomTabLayout;

public class TabViewActivity extends AppCompatActivity {
    private CustomTabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTabItem("1");
        tabLayout.addTabItem("12");
        tabLayout.addTabItem("123");
        tabLayout.addTabItem("asd");
        tabLayout.addTabItem("zxca");
        tabLayout.addTabItem("ww");
        tabLayout.addTabItem("gerw我");
        Fragment[] fragments = new Fragment[5];
        fragments[0] = new TestFragment();
        fragments[1] = new TestFragment();
        fragments[2] = new TestFragment();
        fragments[3] = new TestFragment();
        fragments[4] = new TestFragment();
        ViewPagerFragment adapter = new ViewPagerFragment(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
