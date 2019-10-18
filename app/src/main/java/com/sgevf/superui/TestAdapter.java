package com.sgevf.superui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.VH> {
    private List<String> datas;

    public TestAdapter(List<String> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.text1.setText(datas.get(i));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addData(int flag, List<String> datas) {
        if (flag == 0) {
            this.datas.clear();
        }
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView text1;

        public VH(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
        }
    }
}
