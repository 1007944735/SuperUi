package com.sgevf.superui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sgevf.ui.ScrollStarView;

import java.util.Arrays;

public class ScrollStarTestActivity extends AppCompatActivity {
    ScrollStarView mScrollStarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view_test);
        mScrollStarView = findViewById(R.id.scrollStarView);
        mScrollStarView.addStar(Arrays.asList(R.drawable.ico_ball, R.drawable.ico_ball, R.drawable.ico_ball, R.drawable.ico_ball, R.drawable.ico_ball));
    }
}
