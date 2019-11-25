package com.sgevf.superui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sgevf.ui.tabView.CustomTabLayout;

public class TabViewActivity extends AppCompatActivity {
    private CustomTabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);
        tabLayout=findViewById(R.id.tabLayout);
        tabLayout.addTabItem("21");
        tabLayout.addTabItem("21");
        tabLayout.addTabItem("21");
        tabLayout.addTabItem("21");
        tabLayout.addTabItem("21");
        tabLayout.addTabItem("21");
        tabLayout.addTabItem("21");
        tabLayout.addTabItem("21");
    }
}
