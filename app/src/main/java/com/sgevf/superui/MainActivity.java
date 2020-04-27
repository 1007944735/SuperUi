package com.sgevf.superui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sgevf.ui.AudioProgressView.AudioProgressView;
import com.sgevf.ui.horizontalScrollerView.HorizontalScrollerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HorizontalScrollerView horizontalScroll;
    private AudioProgressView audioProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        horizontalScroll = findViewById(R.id.horizontal_scroll);
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add(i + "");
//        }
//        Adapter adapter = new Adapter(this, list);
//        horizontalScroll.setAdapter(adapter);
        audioProgressView = findViewById(R.id.audioProgressView);
        audioProgressView.setImageUrl("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg");
        audioProgressView.setMax(4);
        audioProgressView.setProgress(4);

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
        public void onBindViewHolder(@NonNull VH vh, final int i) {
            vh.content.setText(datas.get(i));
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, i + "", Toast.LENGTH_SHORT).show();
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
