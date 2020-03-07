package com.sgevf.superui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sgevf.ui.horizontalScrollerView.HorizontalScrollerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HorizontalScrollerView horizontalScroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        horizontalScroll = findViewById(R.id.horizontal_scroll);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i + "");
        }
        Adapter adapter = new Adapter(this, list);
        horizontalScroll.setAdapter(adapter);

    }


    class Adapter extends RecyclerView.Adapter<VH> {
        private Context context;
        private List<String> datas;

        public Adapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = datas;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new VH(LayoutInflater.from(context).inflate(R.layout.item_scroll, null));
        }

        @Override
        public void onBindViewHolder(@NonNull VH vh, int i) {
            vh.content.setText(datas.get(i));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        private TextView content;

        public VH(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
