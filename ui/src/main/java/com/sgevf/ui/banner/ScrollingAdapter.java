package com.sgevf.ui.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class ScrollingAdapter<T> extends PagerAdapter {
    private List<T> datas;
    private LayoutInflater inflater;
    private int count;//原集合个数
    private Context mContext;

    public ScrollingAdapter(Context context, List<T> datas) {
        if (datas == null || datas.isEmpty()) return;
        this.count = datas.size();
        this.mContext = context;
        this.datas = initData(datas);
        this.inflater = LayoutInflater.from(context);
    }

    private List<T> initData(List<T> imageUrls) {
        List<T> lists = new ArrayList<>();
        lists.add(imageUrls.get(count - 1));
        lists.addAll(imageUrls);
        lists.add(imageUrls.get(0));
        return lists;
    }

    public int getRelCount() {
        return count;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(loadLayoutRes(), container, false);
        initView(view, position, datas.get(position));
        container.addView(view);
        return view;
    }

    public abstract int loadLayoutRes();

    public abstract void initView(View view, int position, T data);

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public Context getContext() {
        return mContext;
    }
}
