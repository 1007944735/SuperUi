package com.sgevf.superui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sgevf.ui.horizontalScrollerView.HorizontalScrollerView;

import java.util.ArrayList;
import java.util.List;

public class ScrollerActivity extends AppCompatActivity {
    private HorizontalScrollerView scrollerTabLayout;
    private RecyclerViewAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        scrollerTabLayout = findViewById(R.id.scroller_tab_layout);
        list = new ArrayList<>();
        adapter = new RecyclerViewAdapter(this, list);
        scrollerTabLayout.setAdapter(adapter);
    }

    public void add(View view) {
        List<String> list = new ArrayList<>();
        list.add("131");
        list.add("141");
        list.add("151");
        list.add("161");
        list.add("171");
        list.add("181");
        list.add("911");
        list.add("011");
        list.add("1r1");
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
