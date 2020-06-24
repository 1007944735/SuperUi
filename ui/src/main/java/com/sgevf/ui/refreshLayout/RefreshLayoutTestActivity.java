package com.sgevf.ui.refreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sgevf.ui.R;

import java.util.ArrayList;
import java.util.List;

public class RefreshLayoutTestActivity extends AppCompatActivity {
    private TextView tvBox;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_layout_test);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + "");
        }
        Adapter adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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
            return new VH(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false));
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
            content = itemView.findViewById(android.R.id.text1);

        }
    }
}
