package com.sgevf.superui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sgevf.ui.refreshLayout.RefreshLayoutTestActivity;
import com.sgevf.ui.refreshRecyclerView.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RefreshRecyclerView refreshRecyclerView;
    TextView btnTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshRecyclerView = findViewById(R.id.refreshRecyclerView);
        btnTest = findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RefreshLayoutTestActivity.class));
            }
        });
        List<String> lists = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            lists.add(i + "");
        }
        Adapter adapter = new Adapter(this, lists);
        refreshRecyclerView.setAdapter(adapter);
        refreshRecyclerView.setHasMoreData(true);
        refreshRecyclerView.setShowFooter(true);
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
            return new VH(LayoutInflater.from(context).inflate(R.layout.item_scroll, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final VH vh, int i) {
            vh.content.setText(datas.get(i));
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, datas.get(vh.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        private TextView content;
        private View itemView;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            content = itemView.findViewById(R.id.content);

        }
    }
}
