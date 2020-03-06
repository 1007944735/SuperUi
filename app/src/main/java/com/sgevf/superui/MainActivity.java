package com.sgevf.superui;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sgevf.ui.banner.DefaultIndicatorAdapter;
import com.sgevf.ui.banner.ScrollingBanner;
import com.sgevf.ui.banner.DefaultScrollingAdapter;
import com.sgevf.ui.superRefreshView.SuperRefreshView;
import com.sgevf.ui.tabView.CustomTabLayout;
import com.sgevf.ui.utils.NotificationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    private SuperRefreshView superRefreshView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private ScrollingBanner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        superRefreshView = findViewById(R.id.superRefreshView);
        recyclerView = findViewById(R.id.recyclerView);
        banner = findViewById(R.id.banner);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i + "");
        }
        recyclerViewAdapter = new RecyclerViewAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
        banner.setIndicatorAdapter(new DefaultIndicatorAdapter());
        List<String> datas = new ArrayList<>();
        datas.add("http://file02.16sucai.com/d/file/2014/0704/e53c868ee9e8e7b28c424b56afe2066d.jpg");
        datas.add("http://file02.16sucai.com/d/file/2014/0704/e53c868ee9e8e7b28c424b56afe2066d.jpg");
        datas.add("http://file02.16sucai.com/d/file/2014/0704/e53c868ee9e8e7b28c424b56afe2066d.jpg");
        datas.add("http://file02.16sucai.com/d/file/2014/0704/e53c868ee9e8e7b28c424b56afe2066d.jpg");
        banner.setBannerAdapter(new DefaultScrollingAdapter(this, datas, null));
    }
}
