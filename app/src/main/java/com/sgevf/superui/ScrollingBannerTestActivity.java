package com.sgevf.superui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sgevf.ui.banner.ScrollingBanner;
import com.sgevf.ui.banner.DefaultScrollingAdapter;

import java.util.ArrayList;

public class ScrollingBannerTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scrolling_banner_test);
        ScrollingBanner banner = findViewById(R.id.banner);
        ArrayList<String> list = new ArrayList<>();
        list.add("333");
        list.add("333");
        list.add("333");
        list.add("333");
        Log.d("TAG", "onCreate: ");
        banner.setBannerAdapter(new DefaultScrollingAdapter(this, list));
    }
}
