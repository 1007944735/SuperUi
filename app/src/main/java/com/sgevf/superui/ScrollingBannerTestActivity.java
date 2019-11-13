package com.sgevf.superui;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sgevf.ui.banner.ScrollingBanner;
import com.sgevf.ui.banner.DefaultScrollingAdapter;
import com.sgevf.ui.tabView.CustomTabLayout;
import com.sgevf.ui.utils.NotificationUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class ScrollingBannerTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scrolling_banner_test);
        ScrollingBanner banner = findViewById(R.id.banner);
        ArrayList<String> list = new ArrayList<>();
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573018311073&di=824694e3c72cb66bed0f1a73d312c6dc&imgtype=0&src=http%3A%2F%2Fpic27.nipic.com%2F20130324%2F9252150_152129329000_2.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573018311073&di=8b7bca27b6e5e50c28af5ed9327cb1e2&imgtype=0&src=http%3A%2F%2Fpic44.nipic.com%2F20140723%2F18505720_094503373000_2.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573018311072&di=015702db26baca91b457d23292f1d825&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201308%2F08%2F20130808131501_LnLhw.jpeg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573018311072&di=8e53b3c30902fde343cf2ebbacd53f1d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg");
        Log.d("TAG", "onCreate: ");
        banner.setBannerAdapter(new DefaultScrollingAdapter(this, list, new DefaultScrollingAdapter.DefScrollingItemClickListener() {
            @Override
            public void onClick(int position, String data) {

            }
        }));
        NotificationUtil.init(getApplication(), Arrays.asList(new NotificationUtil.NotificationChannelInfo("0", "name", NotificationManager.IMPORTANCE_HIGH)));
        NotificationUtil.create(1, "0", true, "asd", "asd", "asd", R.mipmap.ic_launcher, null);

        CustomTabLayout tabLayout=findViewById(R.id.tabLayout);
        tabLayout.addTabItem("123");
        tabLayout.addTabItem("123");
        tabLayout.addTabItem("123");
        tabLayout.addTabItem("123");
        tabLayout.addTabItem("123");
        tabLayout.addTabItem("123");
        tabLayout.addTabItem("123");
    }
}
