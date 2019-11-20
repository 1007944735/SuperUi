package com.sgevf.superui;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sgevf.ui.banner.ScrollingBanner;
import com.sgevf.ui.banner.DefaultScrollingAdapter;
import com.sgevf.ui.superRefreshView.SuperRefreshView;
import com.sgevf.ui.tabView.CustomTabLayout;
import com.sgevf.ui.utils.NotificationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SuperRefreshView superRefreshView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        superRefreshView = findViewById(R.id.superRefreshView);
        recyclerView = findViewById(R.id.recyclerView);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(i + "");
        }
        recyclerViewAdapter = new RecyclerViewAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

    }
}
