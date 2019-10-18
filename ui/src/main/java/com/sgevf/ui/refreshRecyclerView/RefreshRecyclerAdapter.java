package com.sgevf.ui.refreshRecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sgevf.ui.R;

public class RefreshRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean isShowFooter = false;
    private boolean hasMoreData = true;
    private RecyclerView.Adapter mAdapter;
    private Context mContext;

    private static final int TYPE_FOOTER = 1;

    public RefreshRecyclerAdapter(Context context, RecyclerView.Adapter adapter) {
        this(context, adapter, false, true);
    }

    public RefreshRecyclerAdapter(Context context, RecyclerView.Adapter adapter, boolean isShowFooter, boolean hasMoreData) {
        this.mContext = context;
        this.mAdapter = adapter;
        this.isShowFooter = isShowFooter;
        this.hasMoreData = hasMoreData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        switch (i) {
            case TYPE_FOOTER:
                viewHolder = new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_refresh_recycler_view_footer, viewGroup, false));
                break;
            default:
                viewHolder = mAdapter.onCreateViewHolder(viewGroup, i);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof FooterViewHolder) {
            FooterViewHolder mFooterViewHolder = (FooterViewHolder) viewHolder;
            mFooterViewHolder.itemFooterProgress.setVisibility(hasMoreData ? View.VISIBLE : View.GONE);
            mFooterViewHolder.itemFooterMessage.setText(hasMoreData ? "加载中" : "无更多数据");
        } else {
            mAdapter.onBindViewHolder(viewHolder, i);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowFooter && position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return isShowFooter ? mAdapter.getItemCount() + 1 : mAdapter.getItemCount();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar itemFooterProgress;
        private TextView itemFooterMessage;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemFooterProgress = itemView.findViewById(R.id.item_footer_progress);
            itemFooterMessage = itemView.findViewById(R.id.item_footer_message);
        }
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void setShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
        notifyDataSetChanged();
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }

    public void setHasMoreData(boolean moreData) {
        this.hasMoreData = moreData;
        notifyDataSetChanged();
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}
