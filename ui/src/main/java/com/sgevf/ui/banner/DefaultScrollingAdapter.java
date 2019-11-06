package com.sgevf.ui.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sgevf.ui.R;
import com.sgevf.ui.utils.ToastUtil;

import java.util.List;

/**
 * 默认数据加载器
 */
public class DefaultScrollingAdapter extends ScrollingAdapter<String> {
    private ImageView image;
    private DefScrollingItemClickListener mListener;

    public DefaultScrollingAdapter(Context context, List<String> datas, DefScrollingItemClickListener listener) {
        super(context, datas);
        this.mListener = listener;
    }

    @Override
    public int loadLayoutRes() {
        return R.layout.item_scrolling_banner;
    }

    @Override
    public void initView(View view, final int position, final String data) {
        image = view.findViewById(R.id.scrolling_banner_item);
        Glide.with(getContext()).load(data).placeholder(R.drawable.banner_default_pic).error(R.drawable.banner_default_pic).into(image);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(position, data);
                }
            }
        });
    }

    public interface DefScrollingItemClickListener {
        void onClick(int position, String data);
    }
}
